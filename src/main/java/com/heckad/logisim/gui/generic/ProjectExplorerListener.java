/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.generic;

import javax.swing.JPopupMenu;

import com.heckad.logisim.tools.AddTool;

public interface ProjectExplorerListener {
    public void selectionChanged(ProjectExplorerEvent event);
    public void doubleClicked(ProjectExplorerEvent event);
    public void moveRequested(ProjectExplorerEvent event, AddTool dragged, AddTool target);
    public void deleteRequested(ProjectExplorerEvent event);
    public JPopupMenu menuRequested(ProjectExplorerEvent event);
}