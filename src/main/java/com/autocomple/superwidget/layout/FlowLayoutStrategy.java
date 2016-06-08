package com.autocomple.superwidget.layout;

public class FlowLayoutStrategy extends MosaicBasedLayoutStrategy {
    public FlowLayoutStrategy(int heightInUnits, int widthInUnits, UnitRuler unitRuler) {
        super(heightInUnits, widthInUnits, unitRuler);
    }

    @Override
    protected Position placeMatrix(Mosaic.UnitMatrix matrix) {

        if (matrix.isEmpty()) {
            return new LayoutStrategy.Position(0,0);
        }

        if (matrix.getWidth() > getMosaicMatrix().getWidth()) {
            //todo: make configurable; same for height!
            return null;
        }

        int column, row;
        for (row = 0; ; row++) {
            column = findSuitableColumn(matrix, row);
            if (column >= 0) break;
        }

        getMosaic().placeMatrix(matrix, row, column);

        return new LayoutStrategy.Position(row, column);
    }


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
