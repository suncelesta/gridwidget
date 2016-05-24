package com.autocomple.testrags;

import com.autocomple.superwidget.SuperWidget;
import com.autocomple.superwidget.tile.CompositeTile;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;

import java.util.Random;

public class Rags extends SuperWidget {
    private Random random = new Random();

    protected Rags() {
        super(new CompositeTile());
    }

    public void addRagpiece() {
        addRagpiece(-1,//randomDimensionValue(),
                randomDimensionUnit(),
                randomDimensionValue(),
                randomDimensionUnit(),
                randomColor());
    }

    private void addRagpiece(double heightValue,
                             Style.Unit heightUnit,
                             double widthValue,
                             Style.Unit widthUnit,
                             String color) {

        Ragpiece ragpiece = new Ragpiece(heightValue, heightUnit, widthValue, widthUnit);
        //todo: find how it can be natural, without forgetting – back into constructor?
        ragpiece.setCommandEventBus(getCommandEventBus());

        prependTile(ragpiece);

        initPiece(color, ragpiece);

        GWT.log("Added piece with height=" + heightValue + heightUnit.getType() +
                ", width=" + widthValue + widthUnit.getType() + ", color=" + color);
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
}
