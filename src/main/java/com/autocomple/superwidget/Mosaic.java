package com.autocomple.superwidget;

import java.util.Arrays;

public class Mosaic {
    private UnitMatrix mosaicMatrix;

    public Mosaic(int heightInUnits, int widthInUnits) {
        this.mosaicMatrix = new UnitMatrix(heightInUnits, widthInUnits);
    }

    public Position placeTile(UnitMatrix tileMatrix) {

        if (tileMatrix.isEmpty()) {
            return new Position(0,0);
        }

        if (tileMatrix.getWidth() > mosaicMatrix.getWidth()) {
            //todo: make configurable; same for height!
            return null;
        }

        int column, row;
        for (row = 0; ; row++) {
            column = findSuitableColumn(tileMatrix, row);
            if (column >= 0) break;
        }

        placeInMatrix(tileMatrix, row, column);

        return new Position(row, column);
    }

    public void removeTile(UnitMatrix tileMatrix, Position position) {
        int maxTileRow = position.getTop() + tileMatrix.getHeight();
        int realMaxRow = Math.min(maxTileRow, mosaicMatrix.getHeight());

        int maxTileCol = position.getLeft() + tileMatrix.getWidth();
        int realMaxCol = Math.min(maxTileCol, mosaicMatrix.getWidth());

        for (int i = position.getTop(); i <  realMaxRow; i++) {
            for (int j = position.getLeft(); j <  realMaxCol; j++) {
                if (tileMatrix.isOccupied(i - position.getTop(), j - position.getLeft())) {
                    mosaicMatrix.setOccupied(i, j, false);
                }
            }
        }
    }

    private int findSuitableColumn(UnitMatrix tileMatrix, int row) {
        int maxColumn = mosaicMatrix.getWidth() - tileMatrix.getWidth();

        for(int column = 0; column <= maxColumn; column++) {
            if (canPlace(tileMatrix, row, column)) {
                return column;
            }
        }

        return -1;
    }

    private boolean canPlace(UnitMatrix tileMatrix, int row, int column) {
        for (int r = row; r < row + tileMatrix.getHeight() && r < mosaicMatrix.getHeight(); r++) {
            for (int c = column; c < column + tileMatrix.getWidth() && c < mosaicMatrix.getWidth(); c++) {
                if (tileMatrix.isOccupied(r - row, c - column) && mosaicMatrix.isOccupied(r, c)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void placeInMatrix(UnitMatrix tileMatrix, int row, int column) {
        for (int r = row; r < row + tileMatrix.getHeight() && r < mosaicMatrix.getHeight(); r++) {
            for (int c = column; c < column + tileMatrix.getWidth() && c < mosaicMatrix.getWidth(); c++) {
                mosaicMatrix.setOccupied(r, c, tileMatrix.isOccupied(r - row, c - column));
            }
        }
    }

    public void clear() {
        mosaicMatrix.clear();
    }

    public static class Position {
        private int top;
        private int left;

        public Position(int top, int left) {
            this.top = top;
            this.left = left;
        }

        public int getTop() {
            return top;
        }

        public int getLeft() {
            return left;
        }
    }

    public static class UnitMatrix {
        private boolean[][] matrix;

        public UnitMatrix(int initialHeight, int initialWidth) {
            this.matrix = new boolean[initialHeight][initialWidth];
        }

        public int getWidth() {
            return matrix.length > 0 ? matrix[0].length : 0;
        }

        public int getHeight() {
            return matrix.length;
        }

        public boolean isOccupied(int row, int col) {
            return matrix[row][col];
        }

        public void setOccupied(int row, int col, boolean occupied) {
            matrix[row][col] = occupied;
        }

        public void addRows(int rowCount) {
            addRows(rowCount, getWidth());
        }

        public void addRows(int rowCount, int rowLength) {

            int oldLength = matrix.length;

            this.matrix = Arrays.copyOf(matrix, oldLength+rowCount);

            for (int i = oldLength; i < oldLength + rowCount; i++) {
                matrix[i] = new boolean[rowLength];
            }
        }

        public boolean isEmpty() {
            return matrix.length == 0;
        }

        public void clear() {
            for (int i = 0; i < getHeight(); i++) {
                for (int j = 0; j < getWidth(); j++) {
                    setOccupied(i, j, false);
                }
            }
        }
    }
}
