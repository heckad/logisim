/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.toolbar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

public interface ToolbarItem {
    public boolean isSelectable();
    public void paintIcon(Component destination, Graphics g);
    public String getToolTip();
    public Dimension getDimension(Object orientation);
}
