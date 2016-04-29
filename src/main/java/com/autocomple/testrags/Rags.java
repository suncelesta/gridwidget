package com.autocomple.testrags;

import com.autocomple.mosaic.MosaicPanel;
import com.autocomple.mosaic.command.PrependCommand;
import com.autocomple.mosaic.command.RemoveCommand;
import com.autocomple.mosaic.command.UpdateCommand;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import java.util.Random;

public class Rags extends Composite {

    interface RagsUiBinder extends UiBinder<HTMLPanel, Rags> {}

    private static RagsUiBinder uiBinder = GWT.create(RagsUiBinder.class);

    private EventBus eventBus;
    private Random random = new Random();

    @UiField(provided = true)
    MosaicPanel ragsContainer;

    /**
     * Constructs an {@code Rags} with the given with and height.
     *
     * @param eventBus the event bus used to provide command events
     */
    public Rags(EventBus eventBus) {
        this.eventBus = eventBus;
        this.ragsContainer = new MosaicPanel(eventBus);

        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("addPieceButton")
    void addPiece(ClickEvent event) {
        addRagpiece();
    }

    private void addRagpiece() {
        addRagpiece(randomDimensionValue(),
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
        Ragpiece ragpiece = new Ragpiece(eventBus);

        //ragpiece.setHeight(heightValue, heightUnit);
        ragpiece.setWidth(widthValue, widthUnit);

        ragpiece.addClickHandler((event) -> changePieceColor(ragpiece));
        ragpiece.addContextMenuHandler((event -> {
            event.preventDefault();
            remove(ragpiece);
        }));

        PrependCommand.sendTo(ragsContainer, ragpiece);
        UpdateCommand.sendTo(ragpiece, color);

        GWT.log("Added piece with height=" + heightValue + heightUnit.getType() +
                ", width=" + widthValue + widthUnit.getType() + ", color=" + color);
    }

    private void changePieceColor(Ragpiece piece) {
        UpdateCommand.sendTo(piece, randomColor());
    }

    private void remove(Ragpiece ragpiece) {
        RemoveCommand.sendTo(ragsContainer, ragpiece);
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
