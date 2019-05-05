/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.undo;

import com.heckad.draw.canvas.ActionDispatcher;

public class UndoLogDispatcher implements ActionDispatcher {
    private UndoLog log;

    public UndoLogDispatcher(UndoLog log) {
        this.log = log;
    }

    @Override
    public void doAction(Action action) {
        log.doAction(action);
    }
}
