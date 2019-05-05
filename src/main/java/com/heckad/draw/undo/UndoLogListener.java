/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.undo;

import java.util.EventListener;

public interface UndoLogListener extends EventListener {
    public void undoLogChanged(UndoLogEvent e);
}
