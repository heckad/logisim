/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.data;

import java.util.List;

public interface AttributeSet {
    public Object clone();
    public void addAttributeListener(AttributeListener l);
    public void removeAttributeListener(AttributeListener l);

    public List<Attribute<?>> getAttributes();
    public boolean containsAttribute(Attribute<?> attr);
    public Attribute<?> getAttribute(String name);

    public boolean isReadOnly(Attribute<?> attr);
    // optional
    public void setReadOnly(Attribute<?> attr, boolean value);

    public boolean isToSave(Attribute<?> attr);

    public <V> V getValue(Attribute<V> attr);
    public <V> void setValue(Attribute<V> attr, V value);
}
