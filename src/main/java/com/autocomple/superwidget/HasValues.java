package com.autocomple.superwidget;

import java.util.List;

public interface HasValues<Value> {

    void setValues(List<Value> values);

    void addValue(Value value);

    void addValue(Value value, int index);

    void removeValue(Value value);
}
