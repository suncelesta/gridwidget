package com.autocomple.superwidget;

import java.util.HashSet;
import java.util.Set;

public abstract class DataProvider<Data, Display extends SuperWidget> {
    private Set<Display> displays = new HashSet<>();

    public void addDisplay(Display display) {
        displays.add(display);
    }

    public void removeDisplay(Display display) {
        displays.remove(display);
    }

    protected void updateData(Data data) {
        for (Display display : displays) {
            updateData(display, data);
        }
    }

    protected abstract void updateData(Display display, Data data);
}
