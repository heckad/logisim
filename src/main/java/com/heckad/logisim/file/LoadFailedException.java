/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.file;

@SuppressWarnings("serial")
public class LoadFailedException extends Exception {
    private boolean shown;

    LoadFailedException(String desc) {
        this(desc, false);
    }

    LoadFailedException(String desc, boolean shown) {
        super(desc);
        this.shown = shown;
    }

    public boolean isShown() {
        return shown;
    }
}