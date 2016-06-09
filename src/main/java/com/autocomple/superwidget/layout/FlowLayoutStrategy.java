package com.autocomple.superwidget.layout;

import com.google.gwt.dom.client.Element;

public class FlowLayoutStrategy extends MosaicBasedLayoutStrategy {

    public FlowLayoutStrategy(Element parentElement) {
        this(DEFAULT_INTERNAL_HEIGHT_IN_UNITS, DEFAULT_INTERNAL_WIDTH_IN_UNITS, parentElement);
    }

    public FlowLayoutStrategy(int heightInUnits,
                              int widthInUnits,
                              Element parentElement) {
        this(heightInUnits, widthInUnits, new UnitRuler(parentElement, widthInUnits, heightInUnits));
    }

    public FlowLayoutStrategy(int heightInUnits,
                              int widthInUnits,
                              UnitRuler unitRuler) {
        this(heightInUnits, widthInUnits, new DefaultTileUnitMatrixFactory(unitRuler), unitRuler);
    }

    public FlowLayoutStrategy(int heightInUnits,
                              int widthInUnits,
                              TileUnitMatrixFactory tileUnitMatrixFactory,
                              UnitRuler unitRuler) {
        super(heightInUnits, widthInUnits, tileUnitMatrixFactory, unitRuler);
    }

    @Override
    protected Position placeMatrix(Mosaic.UnitMatrix matrix) {

        if (matrix.isEmpty()) {
            return new LayoutStrategy.Position(0,0);
        }

        if (matrix.getWidth() > getMosaicMatrix().getWidth()) {
            return null;
        }

        int column, row;
        for (row = 0; ; row++) {
            column = findSuitableColumn(matrix, row);
            if (column >= 0) break;
        }

        if (row + matrix.getHeight() > getMosaicMatrix().getHeight()) {
            onHeightOverflow();
        }

        getMosaic().placeMatrix(matrix, row, column);

        return new LayoutStrategy.Position(row, column);
    }

    protected void onHeightOverflow() {}


    private int findSuitableColumn(Mosaic.UnitMatrix tileMatrix, int row) {
        int maxColumn = getMosaicMatrix().getWidth() - tileMatrix.getWidth();

        for(int column = 0; column <= maxColumn; column++) {
            if (canPlace(tileMatrix, row, column)) {
                return column;
            }
        }

        return -1;
    }

    private boolean canPlace(Mosaic.UnitMatrix tileMatrix, int row, int column) {
        Mosaic.UnitMatrix mosaicMatrix = getMosaicMatrix();

        for (int r = row; r < row + tileMatrix.getHeight() && r < mosaicMatrix.getHeight(); r++) {
            for (int c = column; c < column + tileMatrix.getWidth() && c < mosaicMatrix.getWidth(); c++) {
                if (tileMatrix.isOccupied(r - row, c - column) && mosaicMatrix.isOccupied(r, c)) {
                    return false;
                }
            }
        }

        return true;
    }


    protected Mosaic.UnitMatrix getMosaicMatrix() {
        return getMosaic().getMosaicMatrix();
    }
}
