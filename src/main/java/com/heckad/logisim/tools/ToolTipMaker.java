/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools;

import com.heckad.logisim.comp.ComponentUserEvent;

public interface ToolTipMaker {
    public String getToolTip(ComponentUserEvent event);
}
