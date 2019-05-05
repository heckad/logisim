/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools;

import javax.swing.JPopupMenu;

import com.heckad.logisim.proj.Project;

public interface MenuExtender {
    public void configureMenu(JPopupMenu menu, Project proj);
}
