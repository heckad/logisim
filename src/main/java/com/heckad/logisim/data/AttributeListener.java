/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.data;

public interface AttributeListener {
    public void attributeListChanged(AttributeEvent e);
    public void attributeValueChanged(AttributeEvent e);
}
