package com.autocomple.mosaic;

import com.autocomple.mosaic.command.AddCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

/***
 * A panel that allows to embed {@link MosaicTile} into
 * elements that don't provide resize events.
 */
public class MosaicPanel extends ResizeLayoutPanel {
    private MosaicTile rootTile;

    /**
     * @param commandEventBus the event bus used to provide command events
     */
    public MosaicPanel(EventBus commandEventBus) {
        this.rootTile = new MosaicTile(commandEventBus);

        setWidget(rootTile);

        getElement().getStyle().setPosition(Style.Position.ABSOLUTE);

        delegateAddCommands();
    }

    //todo: also remove
    private void delegateAddCommands() {
        addHandler(rootTile::fireEvent, AddCommand.TYPE);
    }
}
