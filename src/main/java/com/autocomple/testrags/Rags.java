package com.autocomple.testrags;

import com.autocomple.mosaic.Dimension;
import com.autocomple.mosaic.MosaicPanel;
import com.autocomple.mosaic.command.PrependCommand;
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
        addRagpiece(randomDimension(), randomDimension(), randomColor());
    }

    private void addRagpiece(Dimension height,
                             Dimension width,
                             String color) {
        Ragpiece ragpiece = new Ragpiece(eventBus);

        ragpiece.setPlacerHeight(height);
        ragpiece.setPlacerWidth(width);

        ragpiece.addClickHandler((event) -> changePieceColor(ragpiece));

        PrependCommand.sendTo(ragsContainer, ragpiece);
        UpdateCommand.sendTo(ragpiece, color);

        GWT.log("Added piece with height=" + height + ", width=" + width + ", color=" + color);
    }

    private void changePieceColor(Ragpiece piece) {
        UpdateCommand.sendTo(piece, randomColor());
    }

    private Dimension randomDimension() {
        double value = random.nextDouble() * 30 + 25;

        boolean dynamic = random.nextBoolean();
        Style.Unit unit = dynamic ? Style.Unit.PCT : Style.Unit.PX;

        return new Dimension(value, unit);
    }


    private String randomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        return CssColor.make(r, g, b).value();
    }
}
