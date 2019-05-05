/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.heckad.draw.toolbar.Toolbar;
import com.heckad.logisim.gui.generic.ProjectExplorer;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.tools.Tool;

@SuppressWarnings("serial")
class Toolbox extends JPanel {
    private ProjectExplorer toolbox;

    Toolbox(Project proj, MenuListener menu) {
        super(new BorderLayout());

        ToolboxToolbarModel toolbarModel = new ToolboxToolbarModel(menu);
        Toolbar toolbar = new Toolbar(toolbarModel);
        add(toolbar, BorderLayout.NORTH);

        toolbox = new ProjectExplorer(proj);
        toolbox.setListener(new ToolboxManip(proj, toolbox));
        add(new JScrollPane(toolbox), BorderLayout.CENTER);
    }

    void setHaloedTool(Tool value) {
        toolbox.setHaloedTool(value);
    }
}
