/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.menu;

import javax.swing.JMenu;

@SuppressWarnings("serial")
abstract class Menu extends JMenu {
    abstract void computeEnabled();
}
