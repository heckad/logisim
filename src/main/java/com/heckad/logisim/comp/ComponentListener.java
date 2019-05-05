/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.comp;

public interface ComponentListener {
    public void endChanged(ComponentEvent e);
    public void componentInvalidated(ComponentEvent e);
}
