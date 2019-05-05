/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import com.heckad.draw.toolbar.Toolbar;
import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.circuit.Simulator;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.proj.ProjectEvent;
import com.heckad.logisim.proj.ProjectListener;

@SuppressWarnings("serial")
class SimulationExplorer extends JPanel
        implements ProjectListener, MouseListener {
    private Project project;
    private SimulationTreeModel model;
    private JTree tree;

    SimulationExplorer(Project proj, MenuListener menu) {
        super(new BorderLayout());
        this.project = proj;

        SimulationToolbarModel toolbarModel = new SimulationToolbarModel(proj, menu);
        Toolbar toolbar = new Toolbar(toolbarModel);
        add(toolbar, BorderLayout.NORTH);

        model = new SimulationTreeModel(proj.getSimulator().getCircuitState());
        model.setCurrentView(project.getCircuitState());
        tree = new JTree(model);
        tree.setCellRenderer(new SimulationTreeRenderer());
        tree.addMouseListener(this);
        tree.setToggleClickCount(3);
        add(new JScrollPane(tree), BorderLayout.CENTER);
        proj.addProjectListener(this);
    }

    //
    // ProjectListener methods
    //
    @Override
    public void projectChanged(ProjectEvent event) {
        int action = event.getAction();
        if (action == ProjectEvent.ACTION_SET_STATE) {
            Simulator sim = project.getSimulator();
            CircuitState root = sim.getCircuitState();
            if (model.getRootState() != root) {
                model = new SimulationTreeModel(root);
                tree.setModel(model);
            }
            model.setCurrentView(project.getCircuitState());
            TreePath path = model.mapToPath(project.getCircuitState());
            if (path != null) {
                tree.scrollPathToVisible(path);
            }
        }
    }

    //
    // MouseListener methods
    //
    //
    // MouseListener methods
    //
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) {
        requestFocus();
        checkForPopup(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        checkForPopup(e);
    }
    private void checkForPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            // do nothing
            ;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            TreePath path = tree.getPathForLocation(e.getX(), e.getY());
            if (path != null) {
                Object last = path.getLastPathComponent();
                if (last instanceof SimulationTreeCircuitNode) {
                    SimulationTreeCircuitNode node;
                    node = (SimulationTreeCircuitNode) last;
                    project.setCircuitState(node.getCircuitState());
                }
            }
        }
    }
}
