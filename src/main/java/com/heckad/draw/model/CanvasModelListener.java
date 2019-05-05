/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.model;

import java.util.EventListener;

public interface CanvasModelListener extends EventListener {
    public void modelChanged(CanvasModelEvent event);
}
