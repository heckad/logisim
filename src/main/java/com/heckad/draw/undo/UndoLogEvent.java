/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.undo;

import java.util.EventObject;

@SuppressWarnings("serial")
public class UndoLogEvent extends EventObject {
    public static final int ACTION_DONE = 0;
    public static final int ACTION_UNDONE = 1;

    private int action;
    private Action actionObject;

    public UndoLogEvent(UndoLog source, int action, Action actionObject) {
        super(source);
        this.action = action;
        this.actionObject = actionObject;
    }

    public UndoLog getUndoLog() {
        return (UndoLog) getSource();
    }

    public int getAction() {
        return action;
    }

    public Action getActionObject() {
        return actionObject;
    }
}
