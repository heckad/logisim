/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.canvas;

import com.heckad.draw.undo.Action;

public interface ActionDispatcher {
    public void doAction(Action action);
}
