package com.autocomple.superwidget.layout;

import com.autocomple.superwidget.tile.Tile;
import com.autocomple.superwidget.util.Container;

public class StackLayoutStrategy implements LayoutStrategy {
    private int totalHeight = 0;
    private Ruler ruler;

    public StackLayoutStrategy() {
        this(new PixelRuler());
    }

    public StackLayoutStrategy(Ruler ruler) {
        this.ruler = ruler;
    }

    @Override
    public Position place(Tile tile, Container tileContainer) {
        Position tilePosition = new Position(totalHeight, 0);

        totalHeight += getRuler().measureHeight(tileContainer.getElement());

        return tilePosition;
    }

    @Override
    public void remove(Container tileContainer) {
        totalHeight -= getRuler().measureHeight(tileContainer.getElement());
    }

    @Override
    public void clear() {
        totalHeight = 0;
    }

    @Override
    public Ruler getRuler() {
        return ruler;
    }
}
