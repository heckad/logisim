/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import javax.swing.JPopupMenu;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.circuit.SubcircuitFactory;
import com.heckad.logisim.comp.ComponentFactory;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.file.LibraryEvent;
import com.heckad.logisim.file.LibraryEventSource;
import com.heckad.logisim.file.LibraryListener;
import com.heckad.logisim.file.LogisimFile;
import com.heckad.logisim.file.LogisimFileActions;
import com.heckad.logisim.gui.generic.AttrTableModel;
import com.heckad.logisim.gui.generic.ProjectExplorer;
import com.heckad.logisim.gui.generic.ProjectExplorerEvent;
import com.heckad.logisim.gui.generic.ProjectExplorerLibraryNode;
import com.heckad.logisim.gui.generic.ProjectExplorerListener;
import com.heckad.logisim.gui.generic.ProjectExplorerToolNode;
import com.heckad.logisim.gui.menu.ProjectCircuitActions;
import com.heckad.logisim.gui.menu.ProjectLibraryActions;
import com.heckad.logisim.gui.menu.Popups;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.proj.ProjectEvent;
import com.heckad.logisim.proj.ProjectListener;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;

class ToolboxManip implements ProjectExplorerListener {
    private class MyListener
            implements ProjectListener, LibraryListener, AttributeListener {
        private LogisimFile curFile = null;

        @Override
        public void projectChanged(ProjectEvent event) {
            int action = event.getAction();
            if (action == ProjectEvent.ACTION_SET_FILE) {
                setFile((LogisimFile) event.getOldData(),
                        (LogisimFile) event.getData());
                explorer.repaint();
            }
        }

        private void setFile(LogisimFile oldFile, LogisimFile newFile) {
            if (oldFile != null) {
                removeLibrary(oldFile);
                for (Library lib : oldFile.getLibraries()) {
                    removeLibrary(lib);
                }
            }
            curFile = newFile;
            if (newFile != null) {
                addLibrary(newFile);
                for (Library lib : newFile.getLibraries()) {
                    addLibrary(lib);
                }
            }
        }

        @Override
        public void libraryChanged(LibraryEvent event) {
            int action = event.getAction();
            if (action == LibraryEvent.ADD_LIBRARY) {
                if (event.getSource() == curFile) {
                    addLibrary((Library) event.getData());
                }
            } else if (action == LibraryEvent.REMOVE_LIBRARY) {
                if (event.getSource() == curFile) {
                    removeLibrary((Library) event.getData());
                }
            } else if (action == LibraryEvent.ADD_TOOL) {
                Tool tool = (Tool) event.getData();
                AttributeSet attrs = tool.getAttributeSet();
                if (attrs != null) {
                    attrs.addAttributeListener(this);
                }

            } else if (action == LibraryEvent.REMOVE_TOOL) {
                Tool tool = (Tool) event.getData();
                AttributeSet attrs = tool.getAttributeSet();
                if (attrs != null) {
                    attrs.removeAttributeListener(this);
                }

            }
            explorer.repaint();
        }

        private void addLibrary(Library lib) {
            if (lib instanceof LibraryEventSource) {
                ((LibraryEventSource) lib).addLibraryListener(this);
            }
            for (Tool tool : lib.getTools()) {
                AttributeSet attrs = tool.getAttributeSet();
                if (attrs != null) {
                    attrs.addAttributeListener(this);
                }

            }
        }

        private void removeLibrary(Library lib) {
            if (lib instanceof LibraryEventSource) {
                ((LibraryEventSource) lib).removeLibraryListener(this);
            }
            for (Tool tool : lib.getTools()) {
                AttributeSet attrs = tool.getAttributeSet();
                if (attrs != null) {
                    attrs.removeAttributeListener(this);
                }

            }
        }


        @Override
        public void attributeListChanged(AttributeEvent e) { }

        @Override
        public void attributeValueChanged(AttributeEvent e) {
            explorer.repaint();
        }

    }

    private Project proj;
    private ProjectExplorer explorer;
    private MyListener myListener = new MyListener();
    private Tool lastSelected = null;

    ToolboxManip(Project proj, ProjectExplorer explorer) {
        this.proj = proj;
        this.explorer = explorer;
        proj.addProjectListener(myListener);
        myListener.setFile(null, proj.getLogisimFile());
    }

    @Override
    public void selectionChanged(ProjectExplorerEvent event) {
        Object selected = event.getTarget();
        if (selected instanceof ProjectExplorerToolNode) {
            Tool tool = ((ProjectExplorerToolNode) selected).getValue();
            if (tool instanceof AddTool) {
                AddTool addTool = (AddTool) tool;
                ComponentFactory source = addTool.getFactory();
                if (source instanceof SubcircuitFactory) {
                    SubcircuitFactory circFact = (SubcircuitFactory) source;
                    Circuit circ = circFact.getSubcircuit();
                    if (proj.getCurrentCircuit() == circ) {
                        AttrTableModel m = new AttrTableCircuitModel(proj, circ);
                        proj.getFrame().setAttrTableModel(m);
                        return;
                    }
                }
            }

            lastSelected = proj.getTool();
            proj.setTool(tool);
            proj.getFrame().viewAttributes(tool);
        }
    }

    @Override
    public void doubleClicked(ProjectExplorerEvent event) {
        Object clicked = event.getTarget();
        if (clicked instanceof ProjectExplorerToolNode) {
            Tool baseTool = ((ProjectExplorerToolNode) clicked).getValue();
            if (baseTool instanceof AddTool) {
                AddTool tool = (AddTool) baseTool;
                ComponentFactory source = tool.getFactory();
                if (source instanceof SubcircuitFactory) {
                    SubcircuitFactory circFact = (SubcircuitFactory) source;
                    proj.setCurrentCircuit(circFact.getSubcircuit());
                    proj.getFrame().setEditorView(Frame.EDIT_LAYOUT);
                    if (lastSelected != null) {
                        proj.setTool(lastSelected);
                    }

                }
            }
        }
    }

    @Override
    public void moveRequested(ProjectExplorerEvent event, AddTool dragged, AddTool target) {
        LogisimFile file = proj.getLogisimFile();
        int draggedIndex = file.getTools().indexOf(dragged);
        int targetIndex = file.getTools().indexOf(target);
        if (targetIndex > draggedIndex) {
            targetIndex++;
        }

        proj.doAction(LogisimFileActions.moveCircuit(dragged, targetIndex));
    }

    @Override
    public void deleteRequested(ProjectExplorerEvent event) {
        Object request = event.getTarget();
        if (request instanceof ProjectExplorerLibraryNode) {
            Library lib = ((ProjectExplorerLibraryNode) request).getValue();
            ProjectLibraryActions.doUnloadLibrary(proj, lib);
        } else if (request instanceof ProjectExplorerToolNode) {
            Tool tool = ((ProjectExplorerToolNode) request).getValue();
            if (tool instanceof AddTool) {
                ComponentFactory factory = ((AddTool) tool).getFactory();
                if (factory instanceof SubcircuitFactory) {
                    SubcircuitFactory circFact = (SubcircuitFactory) factory;
                    ProjectCircuitActions.doRemoveCircuit(proj, circFact.getSubcircuit());
                }
            }
        }
    }

    @Override
    public JPopupMenu menuRequested(ProjectExplorerEvent event) {
        Object clicked = event.getTarget();
        if (clicked instanceof ProjectExplorerToolNode) {
            Tool baseTool = ((ProjectExplorerToolNode) clicked).getValue();
            if (baseTool instanceof AddTool) {
                AddTool tool = (AddTool) baseTool;
                ComponentFactory source = tool.getFactory();
                if (source instanceof SubcircuitFactory) {
                    Circuit circ = ((SubcircuitFactory) source).getSubcircuit();
                    return Popups.forCircuit(proj, tool, circ);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else if (clicked instanceof ProjectExplorerLibraryNode) {
            Library lib = ((ProjectExplorerLibraryNode) clicked).getValue();
            if (lib == proj.getLogisimFile()) {
                return Popups.forProject(proj);
            } else {
                boolean is_top = event.getTreePath().getPathCount() <= 2;
                return Popups.forLibrary(proj, lib, is_top);
            }
        } else {
            return null;
        }
    }

}
