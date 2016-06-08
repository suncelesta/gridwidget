package com.autocomple.superwidget.layout;

import com.autocomple.superwidget.util.Container;
import com.google.gwt.dom.client.Element;

public abstract class MosaicBasedLayoutStrategy implements LayoutStrategy {
    private Mosaic mosaic;
    private UnitRuler unitRuler;

    protected MosaicBasedLayoutStrategy(int heightInUnits,
                                     int widthInUnits,
                                     UnitRuler unitRuler) {
        this.unitRuler = unitRuler;
        this.mosaic = new Mosaic(heightInUnits, widthInUnits);
    }

    @Override
    public Position place(Container tileContainer) {
        Placeholder tilePlaceholder = getPlaceholder(tileContainer);
        Mosaic.UnitMatrix tileMatrix = tilePlaceholder != null ?
                tilePlaceholder.getMatrix() :
                newMatrix(tileContainer.getElement());

        Position topLeft = placeMatrix(tileMatrix);

        if (tilePlaceholder != null) {
            tilePlaceholder.setPosition(topLeft);
        } else {
            tileContainer.setLayoutStrategyData(new Placeholder(topLeft, tileMatrix));
        }

        return topLeft;
    }

    protected abstract Position placeMatrix(Mosaic.UnitMatrix matrix);

    @Override
    public void remove(Container tileContainer) {
        Placeholder placeholder = getPlaceholder(tileContainer);

        if (placeholder != null) {

            Mosaic.UnitMatrix matrix = placeholder.getMatrix();
            Position position = placeholder.getPosition();

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

    //todo: provider?
    private Mosaic.UnitMatrix newMatrix(Element element) {
        return newMatrix(
                unitRuler.measureHeight(element),
                unitRuler.measureWidth(element));
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

    private static Placeholder getPlaceholder(Container container) {
        Object layoutStrategyData = container.getLayoutStrategyData();

        return layoutStrategyData != null ? (Placeholder)layoutStrategyData : null;
    }

    private static class Placeholder {
        private Position position;
        private Mosaic.UnitMatrix matrix;

        private Placeholder(Position position, Mosaic.UnitMatrix matrix) {
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
}