package com.autocomple.superwidget.tile;

import com.autocomple.superwidget.Mosaic;
import com.autocomple.superwidget.command.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.user.client.ui.LayoutPanel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A mosaic tile which itself contains a mosaic.
 * The mosaic is divided into fixed number of rows and columns,
 * with height and width of cells (units) is changed on resize.
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
        this(getDefaultResources());
    }

    /**
     * @param resources tile resources
     */
    public CompositeTile(Resources resources) {
        this(resources, DEFAULT_INTERNAL_WIDTH_IN_UNITS, DEFAULT_INTERNAL_HEIGHT_IN_UNITS);
    }

    /**
     * @param resources tile resources
     * @param internalWidthInUnits the width of inner mosaic in units
     * @param internalHeightInUnits the height of inner mosaic in units
     */
    public CompositeTile(Resources resources,
                         int internalWidthInUnits,
                         int internalHeightInUnits) {
        this(new LayoutPanel(), resources, internalWidthInUnits, internalHeightInUnits);
    }

    private CompositeTile(LayoutPanel layoutPanel,
                          Resources resources,
                          int internalWidthInUnits,
                          int internalHeightInUnits) {
        super(layoutPanel, resources);

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
        addClearCommandHandler();
    }

    @Override
    public void onResize() {
        Element containerParent = getContainerElement().getParentElement();

        unitWidth = (double) getNormalizedWidth(containerParent) / internalWidthInUnits;
        unitHeight = (double) getNormalizedHeight(containerParent) / internalHeightInUnits;

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
                mosaic.removeTile(tile.getMatrix(getMatrixHeight(tile), getMatrixWidth(tile)), tilePositions.get(tile));
            }
        }
    }

    private void renderTile(Tile tile) {
        Element tileContainer = getChildContainerElement(tile);
        //Element tileSizeEstimator = tile.getSizeEstimatorElement();

        ContainerSettings containerSettings = tile.getContainerSettings();

        //todo: do this on attach? so that we can have widgets based on simple tiles
        tileContainer.setClassName(containerSettings.getClassName()); //tileSizeEstimator.getClassName());

        SafeStyles inlineContainerStyle = containerSettings.getStyles();
        com.google.gwt.dom.client.Style tileContainerStyle = tileContainer.getStyle();
        tileContainerStyle.clearWidth();
        tileContainerStyle.clearHeight();
        appendStyle(tileContainer, inlineContainerStyle);
        //tileContainer.getStyle().setProperty("width", tileSizeEstimator.getStyle().getWidth());
        //tileContainer.getStyle().setProperty("height", tileSizeEstimator.getStyle().getHeight());

        Mosaic.UnitMatrix tileMatrix = tile.getMatrix(getMatrixHeight(tile), getMatrixWidth(tile));

        Mosaic.Position tilePosition = mosaic.placeTile(tileMatrix);

        if (tilePosition != null) {
            tilePositions.put(tile, tilePosition);

            setChildLeftWidth(tile, tileContainer.getStyle().getWidth(), tilePosition.getLeft());

            setChildTopHeight(tile, tileContainer.getStyle().getHeight(), tilePosition.getTop());

        } else {
            //todo: if tile can't be placed should not be shown!
            tilePositions.remove(tile);
        }
    }

    private static void appendStyle(Element element, SafeStyles styleToAppend) {
        String currentStyle = element.getAttribute("style");

        element.setAttribute("style", currentStyle + styleToAppend.asString());
    }

    private int getMatrixHeight(Tile tile) {
        return round(getChildContainerElement(tile).getOffsetHeight() / unitHeight);
    }

    private int getMatrixWidth(Tile tile) {

        return round(getChildContainerElement(tile).getOffsetWidth() / unitWidth);
    }

    private Element getChildContainerElement(Tile child) {
        return getLayoutPanel().getWidgetContainerElement(child);
    }

    private int getNormalizedHeight(Element element) {
        return normalize(element.getOffsetHeight());
    }

    private int getNormalizedWidth(Element element) {
        return normalize(element.getOffsetWidth());
    }

    // TODO: 20.05.16 conditional normalization: only if parent has scrollbar
    private int normalize(int dimension) {
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
                //todo: check bounds
                removeTile(tileList.get(index));
            }
        }, RemoveCommand.TYPE);
    }

    protected void addClearCommandHandler() {
        addCommandHandler(command -> {
            mosaic.clear();

            tilePositions.clear();

            tileList.clear();

        }, ClearCommand.TYPE
        );
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
