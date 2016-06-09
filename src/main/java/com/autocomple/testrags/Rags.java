package com.autocomple.testrags;

import com.autocomple.superwidget.SuperWidget;
import com.autocomple.superwidget.tile.CompositeTile;
import com.autocomple.superwidget.tile.Tile;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import java.util.Random;

public class Rags extends SuperWidget {
    private Random random = new Random();

    public Rags() {
        super(new CompositeTile(new SimpleEventBus()));

        Tile.ContainerStyle containerStyle = getRootTile().getContainerStyle();
        containerStyle.setWidth(100, Style.Unit.PCT);
        containerStyle.setHeight(100, Style.Unit.PCT);

        Resources.INSTANCE.ragpieceStyle().ensureInjected();
    }

    public void addRagpiece() {
        addRagpiece(
                randomDimensionValue(),
                randomDimensionUnit(),
                randomColor());
    }

    private void addRagpiece(
                             double widthValue,
                             Style.Unit widthUnit,
                             String color) {

        Ragpiece ragpiece = new Ragpiece(
                widthValue, widthUnit,
                Resources.INSTANCE.ragpieceStyle().ragpiece(),
                getCommandEventBus());

        prependTile(ragpiece);

        initPiece(color, ragpiece);

        GWT.log("Added piece with width=" + widthValue + widthUnit.getType() + ", color=" + color);
    }

    private void initPiece(String color, Ragpiece ragpiece) {

        ragpiece.addClickHandler((event) -> updateTile(ragpiece, randomColor()));

        ragpiece.addContextMenuHandler((event -> {
            event.preventDefault();
            removeTile(ragpiece);
        }));

        updateTile(ragpiece, color);
    }

    private double randomDimensionValue() {
        return random.nextDouble() * 30 + 25;
    }

    private Style.Unit randomDimensionUnit() {
        boolean dynamic = random.nextBoolean();
        return dynamic ? Style.Unit.PCT : Style.Unit.PX;
    }

    private String randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return CssColor.make(r, g, b).value();
    }

    public interface Resources extends ClientBundle {

        Resources INSTANCE = GWT.create(Resources.class);

        @Source("ragpiece.css")
        RagsStyle ragpieceStyle();
    }

    public interface RagsStyle extends CssResource {
        String ragpiece();
    }
}
