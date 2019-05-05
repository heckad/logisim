/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.heckad.draw.toolbar.AbstractToolbarModel;
import com.heckad.draw.toolbar.ToolbarItem;
import com.heckad.draw.toolbar.ToolbarSeparator;
import com.heckad.logisim.comp.ComponentDrawContext;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.file.LogisimFile;
import com.heckad.logisim.file.ToolbarData;
import com.heckad.logisim.prefs.AppPreferences;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.proj.ProjectEvent;
import com.heckad.logisim.proj.ProjectListener;
import com.heckad.logisim.tools.Tool;
import com.heckad.logisim.util.InputEventUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class LayoutToolbarModel extends AbstractToolbarModel {
    private class ToolItem implements ToolbarItem {
        private Tool tool;

        ToolItem(Tool tool) {
            this.tool = tool;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }

        @Override
        public void paintIcon(Component destination, Graphics g) {
            // draw halo
            if (tool == haloedTool && AppPreferences.ATTRIBUTE_HALO.getBoolean()) {
                g.setColor(Canvas.HALO_COLOR);
                g.fillRect(1, 1, 22, 22);
            }

            // draw tool icon
            g.setColor(Color.BLACK);
            Graphics g_copy = g.create();
            ComponentDrawContext c = new ComponentDrawContext(destination,
                    null, null, g, g_copy);
            tool.paintIcon(c, 2, 2);
            g_copy.dispose();
        }

        @Override
        public String getToolTip() {
            String ret = tool.getDescription();
            int index = 1;
            for (ToolbarItem item : items) {
                if (item == this) {
                    break;
                }

                if (item instanceof ToolItem) {
                    ++index;
                }

            }
            if (index <= 10) {
                if (index == 10) {
                    index = 0;
                }

                int mask = frame.getToolkit().getMenuShortcutKeyMask();
                ret += " (" + InputEventUtil.toKeyDisplayString(mask)
                    + "-" + index + ")";
            }
            return ret;
        }

        @Override
        public Dimension getDimension(Object orientation) {
            return new Dimension(24, 24);
        }
    }

    private class MyListener implements ProjectListener, AttributeListener,
                ToolbarData.ToolbarListener, PropertyChangeListener {
        //
        // ProjectListener methods
        //
        @Override
        public void projectChanged(ProjectEvent e) {
            int act = e.getAction();
            if (act == ProjectEvent.ACTION_SET_TOOL) {
                fireToolbarAppearanceChanged();
            } else if (act == ProjectEvent.ACTION_SET_FILE) {
                LogisimFile old = (LogisimFile) e.getOldData();
                if (old != null) {
                    ToolbarData data = old.getOptions().getToolbarData();
                    data.removeToolbarListener(this);
                    data.removeToolAttributeListener(this);
                }
                LogisimFile file = (LogisimFile) e.getData();
                if (file != null) {
                    ToolbarData data = file.getOptions().getToolbarData();
                    data.addToolbarListener(this);
                    data.addToolAttributeListener(this);
                }
                buildContents();
            }
        }

        //
        // ToolbarListener methods
        //
        @Override
        public void toolbarChanged() {
            buildContents();
        }

        //
        // AttributeListener methods
        //
        @Override
        public void attributeListChanged(AttributeEvent e) { }
        @Override
        public void attributeValueChanged(AttributeEvent e) {
            fireToolbarAppearanceChanged();
        }

        //
        // PropertyChangeListener method
        //
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (AppPreferences.GATE_SHAPE.isSource(event)) {
                fireToolbarAppearanceChanged();
            }
        }
    }

    private Frame frame;
    private Project proj;
    private MyListener myListener;
    private List<ToolbarItem> items;
    private Tool haloedTool;

    public LayoutToolbarModel(Frame frame, Project proj) {
        this.frame = frame;
        this.proj = proj;
        myListener = new MyListener();
        items = Collections.emptyList();
        haloedTool = null;
        buildContents();

        // set up listeners
        ToolbarData data = proj.getOptions().getToolbarData();
        data.addToolbarListener(myListener);
        data.addToolAttributeListener(myListener);
        AppPreferences.GATE_SHAPE.addPropertyChangeListener(myListener);
        proj.addProjectListener(myListener);
    }

    @Override
    public List<ToolbarItem> getItems() {
        return items;
    }

    @Override
    public boolean isSelected(ToolbarItem item) {
        if (item instanceof ToolItem) {
            Tool tool = ((ToolItem) item).tool;
            return tool == proj.getTool();
        } else {
            return false;
        }
    }

    @Override
    public void itemSelected(ToolbarItem item) {
        if (item instanceof ToolItem) {
            Tool tool = ((ToolItem) item).tool;
            proj.setTool(tool);
        }
    }

    public void setHaloedTool(Tool t) {
        if (haloedTool != t) {
            haloedTool = t;
            fireToolbarAppearanceChanged();
        }
    }

    private void buildContents() {
        List<ToolbarItem> oldItems = items;
        List<ToolbarItem> newItems = new ArrayList<ToolbarItem>();
        int pos = -1;
        ToolbarData data = proj.getLogisimFile().getOptions().getToolbarData();
        for (Tool tool : data.getContents()) {
            ++pos;
            if (tool == null) {
                newItems.add(new ToolbarSeparator(4));
            } else {
                ToolbarItem i = findItem(oldItems, tool);
                if (i == null) {
                    newItems.add(new ToolItem(tool));
                } else {
                    newItems.add(i);
                }
            }
        }
        items = Collections.unmodifiableList(newItems);
        fireToolbarContentsChanged();
    }

    private static ToolbarItem findItem(List<ToolbarItem> items, Tool tool) {
        for (ToolbarItem item : items) {
            if (item instanceof ToolItem) {
                if (tool == ((ToolItem) item).tool) {
                    return item;
                }
            }
        }
        return null;
    }
}
