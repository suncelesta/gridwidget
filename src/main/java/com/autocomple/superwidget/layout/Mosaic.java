package com.autocomple.superwidget.layout;

import java.util.Arrays;

class Mosaic {
    private UnitMatrix mosaicMatrix;

    public Mosaic(int heightInUnits, int widthInUnits) {
        this.mosaicMatrix = new UnitMatrix(heightInUnits, widthInUnits);
    }

    public void placeMatrix(UnitMatrix matrix, int row, int column) {
        for (int r = row; r < row + matrix.getHeight() && r < mosaicMatrix.getHeight(); r++) {
            for (int c = column; c < column + matrix.getWidth() && c < mosaicMatrix.getWidth(); c++) {
                mosaicMatrix.setOccupied(r, c, matrix.isOccupied(r - row, c - column));
            }
        }
    }

    public void removeMatrix(UnitMatrix matrix, LayoutStrategy.Position position) {
        int maxTileRow = position.getTop() + matrix.getHeight();
        int realMaxRow = Math.min(maxTileRow, mosaicMatrix.getHeight());

        int maxTileCol = position.getLeft() + matrix.getWidth();
        int realMaxCol = Math.min(maxTileCol, mosaicMatrix.getWidth());

        for (int i = position.getTop(); i <  realMaxRow; i++) {
            for (int j = position.getLeft(); j <  realMaxCol; j++) {
                if (matrix.isOccupied(i - position.getTop(), j - position.getLeft())) {
                    mosaicMatrix.setOccupied(i, j, false);
                }
            }
        }
    }

    public void clear() {
        mosaicMatrix.clear();
    }

    public UnitMatrix getMosaicMatrix() {
        return mosaicMatrix;
    }

    static class UnitMatrix {
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
