package com.autocomple;

import com.autocomple.superwidget.SuperList;
import com.autocomple.superwidget.SuperWidgetPanel;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.ArrayList;
import java.util.List;

public class ListApp implements EntryPoint {
    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry point.
     */
    @Override
    public void onModuleLoad() {
        List<String> values = new ArrayList<>();

        values.add("one");
        values.add("two");
        values.add("three");

        SuperList<String> list = new SuperList<>(new TextCell());

        SuperWidgetPanel tilePanel = new SuperWidgetPanel();
        tilePanel.setHeight("50%");
        tilePanel.setWidth("50%");

        tilePanel.setWidget(list);

        RootPanel.get().add(tilePanel);

        list.setValues(values);
    }
}
