package com.autocomple.superwidget;

import com.autocomple.superwidget.tile.Tile;

public abstract class Placeholder<Data> {
    private Tile placeholderTile;

    public Placeholder(Tile placeholderTile) {
        this.placeholderTile = placeholderTile;
    }

    public abstract void reservePlace();

    public abstract void resolveData(Data data);

    public Tile getPlaceholderTile() {
        return placeholderTile;
    }
}
