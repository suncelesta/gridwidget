package com.autocomple.testlist;

import com.autocomple.superwidget.DataProvider;
import com.autocomple.superwidget.Placeholder;
import com.google.gwt.user.client.Timer;

import java.util.List;

public class TestSuperListDataProvider<Value>
        extends DataProvider<List<Value>, SuperList<Value>> {

    @Override
    public void updateData(List<Value> values) {
        super.updateData(values);
    }

    @Override
    protected void updateData(SuperList<Value> list, List<Value> values) {
        Placeholder<List<Value>> listPlaceholder = list.getListPlaceholder();

        listPlaceholder.reservePlace();

        Timer setValuesTimer = new Timer() {
            @Override
            public void run() {
                listPlaceholder.resolveData(values);
            }
        };

        setValuesTimer.schedule(3000);
    }
}
