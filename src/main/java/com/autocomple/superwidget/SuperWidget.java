package com.autocomple.superwidget;

import com.autocomple.superwidget.command.*;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public abstract class SuperWidget implements IsWidget, RequiresResize {
    private Tile rootTile;
    private EventBus commandEventBus;

    protected SuperWidget(Tile rootTile) {
        this(rootTile, new SimpleEventBus());
    }

    protected SuperWidget(Tile rootTile, EventBus commandEventBus) {
        this.rootTile = rootTile;
        this.commandEventBus = commandEventBus;

        //initWidget(rootTile);
    }

    @Override
    public void onResize() {
        rootTile.onResize();
    }

    @Override
    public Widget asWidget() {
        return getRootTile();
    }

    protected void appendTile(Tile tile) {
        AppendCommand.sendTo(rootTile, tile);
    }

    protected void prependTile(Tile tile) {
        PrependCommand.sendTo(rootTile, tile);
    }

    protected void addTile(Tile tile, int index) {
        AddToIndexCommand.sendTo(rootTile, index, tile);
    }

    protected void removeTile(Tile tile) {
        RemoveTileCommand.sendTo(rootTile, tile);
    }

    protected void removeTile(int index) {
        RemoveFromIndexCommand.sendTo(rootTile, index);
    }

    protected <T> void updateTile(Tile tile, T value) {
        UpdateCommand.sendTo(tile, value);
    }

    protected <T> void update(T value) {
        updateTile(rootTile, value);
    }

    protected void clear() {
        ClearCommand.sendTo(rootTile);
    }

    protected Tile getRootTile() {
        return rootTile;
    }

    protected EventBus getCommandEventBus() {
        return commandEventBus;
    }
}
