package com.autocomple;

import com.autocomple.testrags.RagsTest;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class RagsApp implements EntryPoint {

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry point.
     */
    public void onModuleLoad() {
        RootPanel.get().add(new RagsTest());
    }
}
