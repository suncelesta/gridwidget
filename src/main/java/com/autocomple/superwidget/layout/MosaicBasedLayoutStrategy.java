package com.autocomple.superwidget.layout;

import com.autocomple.superwidget.tile.Tile;
import com.autocomple.superwidget.util.Container;
import com.google.gwt.dom.client.Element;

public abstract class MosaicBasedLayoutStrategy implements LayoutStrategy {
    protected static final int DEFAULT_INTERNAL_WIDTH_IN_UNITS = 100;
    protected static final int DEFAULT_INTERNAL_HEIGHT_IN_UNITS = 200;

    private Mosaic mosaic;

    private TileUnitMatrixFactory tileUnitMatrixFactory;
    private Ruler ruler;

    protected MosaicBasedLayoutStrategy(int mosaicHeightInUnits,
                                        int mosaicWidthInUnits,
                                        Element parentElement) {

        this.ruler = new MosaicUnitRuler(parentElement, mosaicWidthInUnits, mosaicHeightInUnits);
        this.tileUnitMatrixFactory = new DefaultTileUnitMatrixFactory(ruler);
        this.mosaic = new Mosaic(mosaicHeightInUnits, mosaicWidthInUnits);
    }

    @Override
    public Position place(Tile tile, Container tileContainer) {
        Area tileArea = getArea(tileContainer);

        Mosaic.UnitMatrix tileMatrix = tileArea != null ?
                tileArea.getMatrix() :
                tileUnitMatrixFactory.createUnitMatrix(tile, tileContainer);

        Position topLeft = placeMatrix(tileMatrix);

        if (tileArea != null) {
            tileArea.setPosition(topLeft);
        } else {
            tileContainer.setLayoutStrategyData(new Area(topLeft, tileMatrix));
        }

        return topLeft;
    }

    protected abstract Position placeMatrix(Mosaic.UnitMatrix matrix);

    @Override
    public void remove(Container tileContainer) {
        Area tileArea = getArea(tileContainer);

        if (tileArea != null) {

            Mosaic.UnitMatrix matrix = tileArea.getMatrix();
            Position position = tileArea.getPosition();

            if (position != null) {
                mosaic.removeMatrix(matrix, position);

                tileContainer.setLayoutStrategyData(null);
            }

        }
    }

    @Override
    public void clear() {
        mosaic.clear();
    }

    protected Mosaic getMosaic() {
        return mosaic;
    }

    private static Area getArea(Container container) {
        Object layoutStrategyData = container.getLayoutStrategyData();

        return layoutStrategyData != null ? (Area)layoutStrategyData : null;
    }

    @Override
    public Ruler getRuler() {
        return ruler;
    }


    public void setTileUnitMatrixFactory(TileUnitMatrixFactory tileUnitMatrixFactory) {
        this.tileUnitMatrixFactory = tileUnitMatrixFactory;
    }

    private static class Area {
        private Position position;
        private Mosaic.UnitMatrix matrix;

        private Area(Position position, Mosaic.UnitMatrix matrix) {
            this.position = position;
            this.matrix = matrix;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public Mosaic.UnitMatrix getMatrix() {
            return matrix;
        }
    }

    public interface TileUnitMatrixFactory {
        Mosaic.UnitMatrix createUnitMatrix(Tile tile, Container tileContainer);
    }

    protected static class DefaultTileUnitMatrixFactory implements TileUnitMatrixFactory {
        private Ruler ruler;

        public DefaultTileUnitMatrixFactory(Ruler ruler) {
            this.ruler = ruler;
        }

        @Override
        public Mosaic.UnitMatrix createUnitMatrix(Tile tile, Container tileContainer) {
            return newMatrix(tileContainer.getElement());
        }

        private Mosaic.UnitMatrix newMatrix(Element element) {
            return newMatrix(
                    ruler.measureHeight(element),
                    ruler.measureWidth(element));
        }

        private Mosaic.UnitMatrix newMatrix(int height,
                                            int width) {

            Mosaic.UnitMatrix result = new Mosaic.UnitMatrix(height, width);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    result.setOccupied(i, j, true);
                }
            }

            return result;
        }
    }
}
