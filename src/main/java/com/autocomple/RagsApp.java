package com.autocomple;

import com.autocomple.testrags.Rags;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;

public class RagsApp implements EntryPoint {

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry point.
     */
    public void onModuleLoad() {
        Rags rags = new Rags(new SimpleEventBus());

        RootPanel.get().add(rags);
    }
}
