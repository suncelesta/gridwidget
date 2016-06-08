package com.autocomple.superwidget.layout;

import com.autocomple.superwidget.util.Container;

public interface LayoutStrategy {

    Position place(Container tileContainer);

    void remove(Container tileContainer);

    void clear();

    class Position {
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
}