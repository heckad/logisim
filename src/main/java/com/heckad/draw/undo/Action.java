/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.undo;

public abstract class Action {
    public boolean isModification() { return true; }

    public abstract String getName();

	/** Redo
	 */
    public abstract void doIt();

	/** Undo
	 */
    public abstract void undo();

    public boolean shouldAppendTo(Action other) {
    	return false; 
    	}

    public Action append(Action other) {
        return new ActionUnion(this, other);
    }
}
