package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.command.*;
import com.autocomple.superwidget.layout.FlowLayoutStrategy;
import com.autocomple.superwidget.layout.LayoutStrategy;
import com.autocomple.superwidget.util.Container;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * A tile that can contain other tiles.
 * Its mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) changed on resize.
 */
public class CompositeTile extends Tile {

    private LayoutStrategy layoutStrategy;

    private CompositeTilePanel panel;

    private List<Tile> tileList = new ArrayList<>();

    public CompositeTile(EventBus commandEventBus) {
        super(new CompositeTilePanel(), commandEventBus);

        this.panel = (CompositeTilePanel) getWidget();

        panel.addAttachHandler((e) -> onPanelAttach());
    }

    protected void onPanelAttach() {
        initLayoutStrategy();
        onResize();
    }

    protected void initLayoutStrategy() {
        setLayoutStrategy(new FlowLayoutStrategy(getParent().getElement()));
    }

    @Override
    protected void addCommandHandlers() {
        addAdditionCommandHandlers();
        addRemovalCommandHandlers();
    }

    @Override
    public void onResize() {
        layoutStrategy.getRuler().adjust();

        Scheduler.get().scheduleDeferred(this::rearrangeTiles);

        panel.onResize();
    }

    private void rearrangeTiles() {
        clearLayout();

        for (Tile tile : tileList) {
            renderTile(tile);
        }
    }

    protected void addTile(Tile tile, int index) {
        panel.insert(tile, index);

        tileList.add(index, tile);

        clearTilesFrom(index + 1);

        for (int i = index; i < panel.getWidgetCount(); i++) {
            final Tile tileToRender = tileList.get(i);
            Scheduler.get().scheduleFinally(() -> {renderTile(tileToRender);});
        }
    }

    private void clearTilesFrom(int index) {
        for (int i = index; i < panel.getWidgetCount(); i++) {
            Tile tile = tileList.get(i);

            layoutStrategy.remove(panel.getChildContainer(tile));
        }
    }

    private void renderTile(Tile tile) {

        applyContainerStyle(tile);

        Container tileContainer = panel.getChildContainer(tile);

        LayoutStrategy.Position tilePosition = layoutStrategy.place(tile, tileContainer);

        Style tileStyle = tile.getElement().getStyle();

        if (tilePosition != null) {

            if (tileStyle.getDisplay().equals(Style.Display.NONE.getCssName())) {
                tileStyle.clearDisplay();
            }

            panel.setChildLeft(tile,
                    tilePosition.getLeft() * layoutStrategy.getRuler().getUnitWidth(),
                    Style.Unit.PX);

            panel.setChildTop(tile,
                    tilePosition.getTop() * layoutStrategy.getRuler().getUnitHeight(),
                    Style.Unit.PX);

        } else {
            tileStyle.setDisplay(Style.Display.NONE);
        }
    }

    private void applyContainerStyle(Tile child) {
        ContainerStyle childContainerStyle = child.getContainerStyle();

        panel.setChildContainerHeight(child, childContainerStyle.getHeight());

        panel.setChildContainerWidth(child, childContainerStyle.getWidth());

        panel.setChildContainerClassName(child, childContainerStyle.getClassName());
    }

    protected void addAdditionCommandHandlers() {
        addCommandHandler(AppendCommand.TYPE,
                (command) -> addTile(command.getTile(), panel.getWidgetCount())
        );

        addCommandHandler(PrependCommand.TYPE,
                (command) -> addTile(command.getTile(), 0)
        );

        addCommandHandler(AddToIndexCommand.TYPE,
                (command) -> addTile(command.getTile(), command.getIndex()));

    }

    protected void addRemovalCommandHandlers() {
        addCommandHandler(RemoveTileCommand.TYPE,
                (command) -> removeTile(command.getTile()));

        addCommandHandler(RemoveFromIndexCommand.TYPE,
                (command) -> {
                    int index = command.getIndex();
                    if (index >= 0 && index < tileList.size()) {
                        removeTile(tileList.get(index));
                    }
                });

        addCommandHandler(ClearCommand.TYPE,
                (command) -> {
                    clearLayout();

                    clearChildren();
                });
    }

    protected void removeTile(Tile tile) {
        panel.remove(tile);

        tileList.remove(tile);

        rearrangeTiles();
    }

    private void clearLayout() {
        layoutStrategy.clear();

        for (Tile tile : tileList) {
            panel.getChildContainer(tile).setLayoutStrategyData(null);
        }
    }

    private void clearChildren() {
        tileList.clear();
    }

    public void setLayoutStrategy(LayoutStrategy layoutStrategy) {
        this.layoutStrategy = layoutStrategy;
    }
}
