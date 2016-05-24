package com.autocomple.testrags;

import com.autocomple.superwidget.SuperWidgetPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class RagsTest extends Composite {

    interface RagsUiBinder extends UiBinder<HTMLPanel, RagsTest> {}

    private static RagsUiBinder uiBinder = GWT.create(RagsUiBinder.class);

    private Rags rags;

    @UiField
    SuperWidgetPanel ragsContainer;

    public RagsTest() {

        initWidget(uiBinder.createAndBindUi(this));

        this.rags = new Rags();

        ragsContainer.setWidget(rags);
    }

    @UiHandler("addPieceButton")
    void addPiece(ClickEvent event) {
        rags.addRagpiece();
    }
}
