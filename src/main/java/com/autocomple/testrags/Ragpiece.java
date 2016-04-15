package com.autocomple.testrags;

import com.autocomple.mosaic.SingletonTile;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class Ragpiece extends SingletonTile<String> implements HasClickHandlers, HasContextMenuHandlers {
    /**
     * Constructs a {@code Ragpiece} with the given with and height.
     *
     * @param eventBus the event bus used to provide command events
     */
    public Ragpiece(EventBus eventBus) {
        super(eventBus, new RagpieceCell());
    }

    /**
     * Adds a {@link ClickEvent} handler.
     *
     * @param handler the click handler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    /**
     * Adds a {@link ContextMenuEvent} handler.
     *
     * @param handler the context menu handler
     * @return {@link HandlerRegistration} used to remove this handler
     */
    @Override
    public HandlerRegistration addContextMenuHandler(ContextMenuHandler handler) {
        return addDomHandler(handler, ContextMenuEvent.getType());
    }

    public static class RagpieceCell extends AbstractCell<String> {
        private static final Template TEMPLATE = GWT.create(Template.class);

        @Override
        public void render(Context context, String color, SafeHtmlBuilder sb) {
            SafeStyles ragpieceStyle = new SafeStylesBuilder()
                    .trustedBackgroundColor(color)
                    .height(100, Style.Unit.PCT)
                    .width(100, Style.Unit.PCT)
                    .borderStyle(Style.BorderStyle.SOLID)
                    .trustedBorderColor("#900")
                    .appendTrustedString("box-sizing: border-box;\n" +
                            "    -moz-box-sizing: border-box;\n" +
                            "    -webkit-box-sizing: border-box;")
                    .toSafeStyles();


            sb.append(TEMPLATE.ragpiece(ragpieceStyle));
        }

        interface Template extends SafeHtmlTemplates {
            @Template("<div style=\"{0}\"></div>")
            SafeHtml ragpiece(SafeStyles style);
        }
    }
}
