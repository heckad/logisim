/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.generic;

public class AttrTableModelEvent {
    private AttrTableModel model;
    private int index;

    public AttrTableModelEvent(AttrTableModel model) {
        this(model, -1);
    }

    public AttrTableModelEvent(AttrTableModel model, int index) {
        this.model = model;
        this.index = index;
    }

    public Object getSource() {
        return model;
    }

    public AttrTableModel getModel() {
        return model;
    }

    public int getRowIndex() {
        return index;
    }
}
