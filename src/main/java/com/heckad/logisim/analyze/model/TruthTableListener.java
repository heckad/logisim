/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.analyze.model;

public interface TruthTableListener {
    public void cellsChanged(TruthTableEvent event);
    public void structureChanged(TruthTableEvent event);
}
