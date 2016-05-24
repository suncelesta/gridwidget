package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.Mosaic;
import com.autocomple.superwidget.command.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.user.client.ui.LayoutPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A tile that can contain other tiles.
 * Its mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) changed on resize.
 */
public class CompositeTile extends Tile {
    private static final int DEFAULT_INTERNAL_WIDTH_IN_UNITS = 100;
    private static final int DEFAULT_INTERNAL_HEIGHT_IN_UNITS = 200;

    private int internalWidthInUnits;
    private int internalHeightInUnits;

    private static final int SCROLLBAR_WIDTH = getScrollBarWidth();

    double unitWidth;
    double unitHeight;

    private Mosaic mosaic;
    private LayoutPanel layoutPanel;

    private List<Tile> tileList = new ArrayList<>();
    private HashMap<Tile, Mosaic.Position> tilePositions = new HashMap<>();

    public CompositeTile() {
        this(DEFAULT_INTERNAL_WIDTH_IN_UNITS, DEFAULT_INTERNAL_HEIGHT_IN_UNITS);
    }

    /**
     * @param internalWidthInUnits the width of inner mosaic in units
     * @param internalHeightInUnits the height of inner mosaic in units
     */
    public CompositeTile(int internalWidthInUnits,
                         int internalHeightInUnits) {
        this(new LayoutPanel(), internalWidthInUnits, internalHeightInUnits);
    }

    private CompositeTile(LayoutPanel layoutPanel,
                          int internalWidthInUnits,
                          int internalHeightInUnits) {
        super(layoutPanel);

        this.layoutPanel = layoutPanel;

        this.internalWidthInUnits = internalWidthInUnits;
        this.internalHeightInUnits = internalHeightInUnits;

        init();
    }

    private void init() {
        this.mosaic = new Mosaic(internalHeightInUnits, internalWidthInUnits);

        getLayoutPanel().addAttachHandler((e) -> onResize());
    }

    @Override
    protected void addCommandHandlers() {
        addAddCommandHandler();
        addRemoveCommandHandler();
    }

    @Override
    public void onResize() {
        Element containerParent = getContainerElement().getParentElement();

        unitWidth = (double) getDiscountedDimension(containerParent, false) / internalWidthInUnits;
        unitHeight = (double) getDiscountedDimension(containerParent, true) / internalHeightInUnits;

        Scheduler.get().scheduleDeferred(this::rearrangeTiles);

        getLayoutPanel().onResize();
    }

    //todo: getting through parent panel?
    private Element getContainerElement() {
        return getElement().getParentElement();
    }

    private void rearrangeTiles() {
        mosaic.clear();

        for (Tile tile : tileList) {
            renderTile(tile);
        }
    }

    protected void addTile(Tile tile, int index) {
        getLayoutPanel().insert(tile, index);

        tileList.add(index, tile);

        clearTilesFrom(index + 1);

        for (int i = index; i < getLayoutPanel().getWidgetCount(); i++) {
            final Tile tileToRender = tileList.get(i);
            Scheduler.get().scheduleFinally(() -> {renderTile(tileToRender);});
        }
    }

    private void clearTilesFrom(int index) {
        for (int i = index; i < getLayoutPanel().getWidgetCount(); i++) {
            Tile tile = tileList.get(i);
            if (tilePositions.containsKey(tile)) {
                boolean isHeight = true;
                mosaic.removeTile(tile.getMatrix(
                        getMatrixDimension(tile, isHeight),
                        getMatrixDimension(tile, !isHeight)), tilePositions.get(tile));
            }
        }
    }

    private void renderTile(Tile tile) {
        Element tileContainer = getChildContainerElement(tile);

        ContainerSettings containerSettings = tile.getContainerSettings();

        tileContainer.addClassName(containerSettings.getClassName());

        //todo: only if changed
        SafeStyles inlineContainerStyle = containerSettings.getStyles();
        Style tileContainerStyle = tileContainer.getStyle();
        tileContainerStyle.clearWidth();
        tileContainerStyle.clearHeight();
        appendStyle(tileContainer, inlineContainerStyle);

        boolean isHeight = true;
        Mosaic.UnitMatrix tileMatrix = tile.getMatrix(
                getMatrixDimension(tile, isHeight),
                getMatrixDimension(tile, !isHeight));

        Mosaic.Position tilePosition = mosaic.placeTile(tileMatrix);

        if (tilePosition != null) {
            tilePositions.put(tile, tilePosition);

            Style tileStyle = tile.getElement().getStyle();
            if (tileStyle.getDisplay().equals(Style.Display.NONE.getCssName())) {
                tileStyle.clearDisplay();
            }

            setChildLeftWidth(tile, tileContainer.getStyle().getWidth(), tilePosition.getLeft());

            setChildTopHeight(tile, tileContainer.getStyle().getHeight(), tilePosition.getTop());

        } else {
            tilePositions.remove(tile);
            tile.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

    private static void appendStyle(Element element, SafeStyles styleToAppend) {
        String currentStyle = element.getAttribute("style");

        element.setAttribute("style", currentStyle + styleToAppend.asString());
    }

    //todo: this should be with mosaic – should be configurable using a parameter
    private int getMatrixDimension(Tile tile, boolean vertical) {
        Element containerElement = getChildContainerElement(tile);
        int dimension = getDimension(containerElement, vertical);
        double normalizationParameter = getNormalizationParameter(containerElement.getParentElement(), vertical);

        double unitSize = vertical ? unitHeight : unitWidth;

        return round(dimension * normalizationParameter / unitSize);
    }

    private Element getChildContainerElement(Tile child) {
        return getLayoutPanel().getWidgetContainerElement(child);
    }

    private int getDiscountedDimension(Element element, boolean vertical) {
        return discount(getDimension(element, vertical));
    }

    private double getNormalizationParameter(Element element, boolean vertical) {
        int dimension = getDimension(element, vertical);

        return (double) discount(dimension) / dimension;
    }

    private int getDimension(Element element, boolean vertical) {
        return vertical ?
                element.getOffsetHeight() :
                element.getOffsetWidth();
    }

    private int discount(int dimension) {
        return dimension - SCROLLBAR_WIDTH;
    }

    // this is more correct for tile placement than simply ceiling,
    // as we care about at least half a pixel
    private int round(double value) {
        return new BigDecimal(value).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private void setChildLeftWidth(Tile child, String width, int left) {
        double widthValue = width.isEmpty() ?
                -1 :
                parseValue(width);

        com.google.gwt.dom.client.Style.Unit widthUnit = parseUnit(width);

        getLayoutPanel().setWidgetLeftWidth(child,
                left * unitWidth, com.google.gwt.dom.client.Style.Unit.PX,
                widthValue, widthUnit);
    }

    private void setChildTopHeight(Tile child, String height, int top) {
        double heightValue = height.isEmpty() ?
                -1 :
                parseValue(height);

        com.google.gwt.dom.client.Style.Unit heightUnit = parseUnit(height);

        getLayoutPanel().setWidgetTopHeight(child,
                top * unitHeight, com.google.gwt.dom.client.Style.Unit.PX,
                heightValue, heightUnit);
    }

    private static double parseValue(String dimensionAsString) {
        return Double.parseDouble(dimensionAsString.replaceFirst("[^0-9.]+", ""));
    }

    private static com.google.gwt.dom.client.Style.Unit parseUnit(String dimensionAsString) {
        com.google.gwt.dom.client.Style.Unit parsedUnit = null;

        if (!dimensionAsString.isEmpty()) {

            for (com.google.gwt.dom.client.Style.Unit unit : com.google.gwt.dom.client.Style.Unit.values()) {
                if (dimensionAsString.endsWith(unit.getType())) {
                    parsedUnit = unit;
                }
            }

        } else {
            parsedUnit = com.google.gwt.dom.client.Style.Unit.PX;
        }

        return parsedUnit;
    }

    //todo: extract interface, use visitor
    protected void addAddCommandHandler() {
        addCommandHandler(command -> {
            if (command instanceof AppendCommand) {
                addTile(command.getTile(), getLayoutPanel().getWidgetCount());
            } else if (command instanceof PrependCommand) {
                addTile(command.getTile(), 0);
            } else if (command instanceof AddToIndexCommand) {
                addTile(command.getTile(), ((AddToIndexCommand)command).getIndex());
            }
        }, AddCommand.TYPE);
    }

    protected void addRemoveCommandHandler() {
        addCommandHandler(command -> {
            if (command instanceof RemoveTileCommand) {
                removeTile(((RemoveTileCommand)command).getTile());
            } else if (command instanceof RemoveFromIndexCommand) {
                int index = ((RemoveFromIndexCommand)command).getIndex();
                if (index >= 0 && index < tileList.size()) {
                    removeTile(tileList.get(index));
                }
            } else if (command instanceof ClearCommand) {
                mosaic.clear();

                tilePositions.clear();

                tileList.clear();
            }
        }, RemoveCommand.TYPE);
    }

    protected void removeTile(Tile tile) {
        getLayoutPanel().remove(tile);

        tileList.remove(tile);

        rearrangeTiles();
    }

    public LayoutPanel getLayoutPanel() {
        return layoutPanel;
    }

    private static native int getScrollBarWidth() /*-{
        var scr = null;
        var inn = null;
        var wNoScroll = 0;
        var wScroll = 0;

        // Outer scrolling div
        scr = $wnd.document.createElement('div');
        scr.style.position = 'absolute';
        scr.style.top = '-1000px';
        scr.style.left = '-1000px';
        scr.style.width = '100px';
        scr.style.height = '50px';
        // Start with no scrollbar
        scr.style.overflow = 'hidden';

        // Inner content div
        inn = $wnd.document.createElement('div');
        inn.style.width = '100%';
        inn.style.height = '200px';

        // Put the inner div in the scrolling div
        scr.appendChild(inn);
        // Append the scrolling div to the doc

        $wnd.document.body.appendChild(scr);

        // Width of the inner div sans scrollbar
        wNoScroll = inn.offsetWidth;
        // Add the scrollbar
        scr.style.overflow = 'auto';
        // Width of the inner div width scrollbar
        wScroll = inn.offsetWidth;

        // Remove the scrolling div from the doc
        $wnd.document.body.removeChild(
            $wnd.document.body.lastChild);

        // Pixel width of the scroller
        return (wNoScroll - wScroll);
    }-*/;
}
