package com.autocomple.mosaic;

import com.autocomple.mosaic.command.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A mosaic tile which itself contains a mosaic.
 * The mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) is changed on resize.
 */
public class MosaicTile extends Tile {
    private static final int DEFAULT_INTERNAL_WIDTH_IN_UNITS = 100;
    private static final int DEFAULT_INTERNAL_HEIGHT_IN_UNITS = 200;

    private int internalWidthInUnits = DEFAULT_INTERNAL_WIDTH_IN_UNITS;
    private int internalHeightInUnits = DEFAULT_INTERNAL_HEIGHT_IN_UNITS;

    double unitWidth;
    double unitHeight;

    private Mosaic mosaic;

    private List<Tile> tileList = new ArrayList<>();
    private HashMap<Tile, Mosaic.Position> tilePositions = new HashMap<>();

    /**
     * @param commandEventBus the event bus used to provide command events
     * @param internalWidthInUnits the width of inner mosaic in units
     * @param internalHeightInUnits the height of inner mosaic in units
     */
    public MosaicTile(EventBus commandEventBus, int internalWidthInUnits, int internalHeightInUnits) {
        super(commandEventBus);

        this.internalWidthInUnits = internalWidthInUnits;
        this.internalHeightInUnits = internalHeightInUnits;

        init();
    }

    /**
     * @param commandEventBus the event bus used to provide command events
     */
    public MosaicTile(EventBus commandEventBus) {
        super(commandEventBus);

        init();
    }

    private void init() {
        this.mosaic = new Mosaic(internalHeightInUnits, internalWidthInUnits);

        addAddCommandHandler();
        addRemoveCommandHandler();
    }

    @Override
    public void onResize() {
        unitWidth = (double)getOffsetWidth() / internalWidthInUnits;
        unitHeight = (double)getOffsetHeight() / internalHeightInUnits;

        Scheduler.get().scheduleDeferred(this::rearrangeTiles);

        super.onResize();
    }

    private void rearrangeTiles() {
        mosaic.clear();

        for (Tile tile : tileList) {
            renderTile(tile);
        }
    }

    protected void addTile(Tile tile, int index) {
        insert(tile, index);

        tileList.add(index, tile);

        clearTilesFrom(index + 1);
        for (int i = index; i < getWidgetCount(); i++) {
            renderTile(tileList.get(i));
        }
    }

    private void clearTilesFrom(int index) {
        for (int i = index; i < getWidgetCount(); i++) {
            Tile tile = tileList.get(i);
            if (tilePositions.containsKey(tile)) {
                mosaic.removeTile(tile.getMatrix(unitHeight, unitWidth), tilePositions.get(tile));
            }
        }
    }

    private void renderTile(Tile tile) {
        Mosaic.UnitMatrix tileMatrix = tile.getMatrix(unitHeight, unitWidth);

        Mosaic.Position tilePosition = mosaic.placeTile(tileMatrix);

        if (tilePosition != null) {
            tilePositions.put(tile, tilePosition);

            setWidgetLeftWidth(tile, tilePosition.getLeft() * unitWidth, Style.Unit.PX,
                    tile.getPlacerWidth().getValue(), tile.getPlacerWidth().getUnit());

            setWidgetTopHeight(tile, tilePosition.getTop() * unitHeight, Style.Unit.PX,
                    tile.getPlacerHeight().getValue(), tile.getPlacerHeight().getUnit());
        } else {
            tilePositions.remove(tile);
        }
    }

    protected void addAddCommandHandler() {
        addCommandHandler(command -> {
            if (command instanceof AppendCommand) {
                addTile(command.getTile(), getWidgetCount());
            } else if (command instanceof PrependCommand) {
                addTile(command.getTile(), 0);
            } else if (command instanceof AddToIndexCommand) {
                addTile(command.getTile(), ((AddToIndexCommand)command).getIndex());
            }
        }, AddCommand.TYPE);
    }

    protected void addRemoveCommandHandler() {
        addCommandHandler(command -> removeTile(command.getTile()), RemoveCommand.TYPE);
    }

    protected void removeTile(Tile tile) {
        remove(tile);

        tileList.remove(tile);

        rearrangeTiles();
    }
}
