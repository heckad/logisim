/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.file.LoadedLibrary;
import com.heckad.logisim.file.Loader;
import com.heckad.logisim.file.LogisimFile;
import com.heckad.logisim.gui.main.Frame;
import com.heckad.logisim.gui.main.StatisticsDialog;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;
import static com.heckad.logisim.util.LocaleString.*;

@SuppressWarnings("serial")
public class Popups {
    private static class ProjectPopup extends JPopupMenu
            implements ActionListener {
        Project proj;
        JMenuItem add = new JMenuItem(getFromLocale("projectAddCircuitItem"));
        JMenu load = new JMenu(getFromLocale("projectLoadLibraryItem"));
        JMenuItem loadBuiltin = new JMenuItem(getFromLocale("projectLoadBuiltinItem"));
        JMenuItem loadLogisim = new JMenuItem(getFromLocale("projectLoadLogisimItem"));
        JMenuItem loadJar = new JMenuItem(getFromLocale("projectLoadJarItem"));

        ProjectPopup(Project proj) {
            super(getFromLocale("projMenu"));
            this.proj = proj;

            load.add(loadBuiltin); loadBuiltin.addActionListener(this);
            load.add(loadLogisim); loadLogisim.addActionListener(this);
            load.add(loadJar); loadJar.addActionListener(this);

            add(add); add.addActionListener(this);
            add(load);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == add) {
                ProjectCircuitActions.doAddCircuit(proj);
            } else if (src == loadBuiltin) {
                ProjectLibraryActions.doLoadBuiltinLibrary(proj);
            } else if (src == loadLogisim) {
                ProjectLibraryActions.doLoadLogisimLibrary(proj);
            } else if (src == loadJar) {
                ProjectLibraryActions.doLoadJarLibrary(proj);
            }
        }
    }

    private static class LibraryPopup extends JPopupMenu
            implements ActionListener {
        Project proj;
        Library lib;
        JMenuItem unload = new JMenuItem(getFromLocale("projectUnloadLibraryItem"));
        JMenuItem reload = new JMenuItem(getFromLocale("projectReloadLibraryItem"));

        LibraryPopup(Project proj, Library lib, boolean is_top) {
            super(getFromLocale("libMenu"));
            this.proj = proj;
            this.lib = lib;

            add(unload); unload.addActionListener(this);
            add(reload); reload.addActionListener(this);
            unload.setEnabled(is_top);
            reload.setEnabled(is_top && lib instanceof LoadedLibrary);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == unload) {
                ProjectLibraryActions.doUnloadLibrary(proj, lib);
            } else if (src == reload) {
                Loader loader = proj.getLogisimFile().getLoader();
                loader.reload((LoadedLibrary) lib);
            }
        }
    }

    private static class CircuitPopup extends JPopupMenu
            implements ActionListener {
        Project proj;
        Circuit circuit;
        JMenuItem analyze = new JMenuItem(getFromLocale("projectAnalyzeCircuitItem"));
        JMenuItem stats = new JMenuItem(getFromLocale("projectGetCircuitStatisticsItem"));
        JMenuItem main = new JMenuItem(getFromLocale("projectSetAsMainItem"));
        JMenuItem remove = new JMenuItem(getFromLocale("projectRemoveCircuitItem"));
        JMenuItem editLayout = new JMenuItem(getFromLocale("projectEditCircuitLayoutItem"));
        JMenuItem editAppearance = new JMenuItem(getFromLocale("projectEditCircuitAppearanceItem"));

        CircuitPopup(Project proj, Tool tool, Circuit circuit) {
            super(getFromLocale("circuitMenu"));
            this.proj = proj;
            this.circuit = circuit;

            add(editLayout); editLayout.addActionListener(this);
            add(editAppearance); editAppearance.addActionListener(this);
            add(analyze); analyze.addActionListener(this);
            add(stats); stats.addActionListener(this);
            addSeparator();
            add(main); main.addActionListener(this);
            add(remove); remove.addActionListener(this);

            boolean canChange = proj.getLogisimFile().contains(circuit);
            LogisimFile file = proj.getLogisimFile();
            if (circuit == proj.getCurrentCircuit()) {
                if (proj.getFrame().getEditorView().equals(Frame.EDIT_APPEARANCE)) {
                    editAppearance.setEnabled(false);
                } else {
                    editLayout.setEnabled(false);
                }
            }
            main.setEnabled(canChange && file.getMainCircuit() != circuit);
            remove.setEnabled(canChange && file.getCircuitCount() > 1
                    && proj.getDependencies().canRemove(circuit));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == editLayout) {
                proj.setCurrentCircuit(circuit);
                proj.getFrame().setEditorView(Frame.EDIT_LAYOUT);
            } else if (source == editAppearance) {
                proj.setCurrentCircuit(circuit);
                proj.getFrame().setEditorView(Frame.EDIT_APPEARANCE);
            } else if (source == analyze) {
                ProjectCircuitActions.doAnalyze(proj, circuit);
            } else if (source == stats) {
                JFrame frame = (JFrame) SwingUtilities.getRoot(this);
                StatisticsDialog.show(frame, proj.getLogisimFile(), circuit);
            } else if (source == main) {
                ProjectCircuitActions.doSetAsMainCircuit(proj, circuit);
            } else if (source == remove) {
                ProjectCircuitActions.doRemoveCircuit(proj, circuit);
            }
        }
    }

    public static JPopupMenu forCircuit(Project proj, AddTool tool, Circuit circ) {
        return new CircuitPopup(proj, tool, circ);
    }

    public static JPopupMenu forTool(Project proj, Tool tool) {
        return null;
    }

    public static JPopupMenu forProject(Project proj) {
        return new ProjectPopup(proj);
    }

    public static JPopupMenu forLibrary(Project proj, Library lib, boolean isTop) {
        return new LibraryPopup(proj, lib, isTop);
    }

}
