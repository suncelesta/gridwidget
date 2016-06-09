package com.autocomple;

import com.autocomple.superwidget.SuperWidgetPanel;
import com.autocomple.testlist.SuperList;
import com.autocomple.testlist.TestSuperListDataProvider;
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
        List<String> values = createValues();

        SuperList<String> list = createAndAddList();

        setValues(values, list);
    }

    private void setValues(List<String> values, SuperList<String> list) {
        TestSuperListDataProvider<String> dataProvider = new TestSuperListDataProvider<>();
        dataProvider.addDisplay(list);
        dataProvider.updateData(values);
        //list.setValues(values);
    }

    private SuperList<String> createAndAddList() {
        SuperList<String> list = new SuperList<>(new TextCell());

        SuperWidgetPanel tilePanel = new SuperWidgetPanel();
        tilePanel.setHeight("50%");
        tilePanel.setWidth("50%");

        tilePanel.setWidget(list);

        RootPanel.get().add(tilePanel);
        return list;
    }

    private List<String> createValues() {
        List<String> values = new ArrayList<>();

        values.add("one");
        values.add("two");
        values.add("three");
        return values;
    }
}
