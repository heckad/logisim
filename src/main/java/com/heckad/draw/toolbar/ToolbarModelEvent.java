/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.toolbar;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ToolbarModelEvent extends EventObject {
    public ToolbarModelEvent(ToolbarModel model) {
        super(model);
    }
}
