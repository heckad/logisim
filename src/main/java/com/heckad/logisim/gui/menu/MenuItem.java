/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

interface MenuItem {
    boolean hasListeners();

    public void addActionListener(ActionListener l);
    public void removeActionListener(ActionListener l);
    public boolean isEnabled();
    public void setEnabled(boolean value);
    public void actionPerformed(ActionEvent event);
}
