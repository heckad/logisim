/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.canvas;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.heckad.draw.model.CanvasModelEvent;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.model.Handle;
import com.heckad.draw.model.HandleGesture;
import com.heckad.logisim.data.Location;

public class Selection {
    private static final String MOVING_HANDLE = "movingHandle";
    private static final String TRANSLATING = "translating";
    private static final String HIDDEN = "hidden";

    private List<SelectionListener> listeners;
    private Set<CanvasObject> selected;
    private Set<CanvasObject> selectedView;
    private Map<CanvasObject, String> suppressed;
    private Set<CanvasObject> suppressedView;
    private Handle selectedHandle;
    private HandleGesture curHandleGesture;
    private int moveDx;
    private int moveDy;

    protected Selection() {
        listeners = new ArrayList<SelectionListener>();
        selected = new HashSet<CanvasObject>();
        suppressed = new HashMap<CanvasObject, String>();
        selectedView = Collections.unmodifiableSet(selected);
        suppressedView = Collections.unmodifiableSet(suppressed.keySet());
    }

    public void addSelectionListener(SelectionListener l) {
        listeners.add(l);
    }

    public void removeSelectionListener(SelectionListener l) {
        listeners.remove(l);
    }

    private void fireChanged(int action, Collection<CanvasObject> affected) {
        SelectionEvent e = null;
        for (SelectionListener listener : listeners) {
            if (e == null) {
                e = new SelectionEvent(this, action, affected);
            }

            listener.selectionChanged(e);
        }
    }

    public boolean isEmpty() {
        return selected.isEmpty();
    }

    public boolean isSelected(CanvasObject shape) {
        return selected.contains(shape);
    }

    public Set<CanvasObject> getSelected() {
        return selectedView;
    }

    public void clearSelected() {
        if (!selected.isEmpty()) {
            List<CanvasObject> oldSelected;
            oldSelected = new ArrayList<CanvasObject>(selected);
            selected.clear();
            suppressed.clear();
            setHandleSelected(null);
            fireChanged(SelectionEvent.ACTION_REMOVED, oldSelected);
        }
    }

    public void setSelected(CanvasObject shape, boolean value) {
        setSelected(Collections.singleton(shape), value);
    }

    public void setSelected(Collection<CanvasObject> shapes, boolean value) {
        if (value) {
            ArrayList<CanvasObject> added;
            added = new ArrayList<CanvasObject>(shapes.size());
            for (CanvasObject shape : shapes) {
                if (selected.add(shape)) {
                    added.add(shape);
                }
            }
            if (!added.isEmpty()) {
                fireChanged(SelectionEvent.ACTION_ADDED, added);
            }
        } else {
            List<CanvasObject> removed;
            removed = new ArrayList<CanvasObject>(shapes.size());
            for (CanvasObject shape : shapes) {
                if (selected.remove(shape)) {
                    suppressed.remove(shape);
                    Handle h = selectedHandle;
                    if (h != null && h.getObject() == shape) {
                        setHandleSelected(null);
                    }

                    removed.add(shape);
                }
            }
            if (!removed.isEmpty()) {
                fireChanged(SelectionEvent.ACTION_REMOVED, removed);
            }
        }
    }

    public void toggleSelected(Collection<CanvasObject> shapes) {
        List<CanvasObject> added;
        added = new ArrayList<CanvasObject>(shapes.size());
        List<CanvasObject> removed;
        removed = new ArrayList<CanvasObject>(shapes.size());
        for (CanvasObject shape : shapes) {
            if (selected.contains(shape)) {
                selected.remove(shape);
                suppressed.remove(shape);
                Handle h = selectedHandle;
                if (h != null && h.getObject() == shape) {
                    setHandleSelected(null);
                }

                removed.add(shape);
            } else {
                selected.add(shape);
                added.add(shape);
            }
        }
        if (!removed.isEmpty()) {
            fireChanged(SelectionEvent.ACTION_REMOVED, removed);
        }
        if (!added.isEmpty()) {
            fireChanged(SelectionEvent.ACTION_ADDED, added);
        }
    }

    public Set<CanvasObject> getDrawsSuppressed() {
        return suppressedView;
    }

    public void clearDrawsSuppressed() {
        suppressed.clear();
        curHandleGesture = null;
    }

    public Handle getSelectedHandle() {
        return selectedHandle;
    }

    public void setHandleSelected(Handle handle) {
        Handle cur = selectedHandle;
        boolean same = cur == null ? handle == null : cur.equals(handle);
        if (!same) {
            selectedHandle = handle;
            curHandleGesture = null;
            Collection<CanvasObject> objs;
            if (handle == null) {
                objs = Collections.emptySet();
            } else {
                objs = Collections.singleton(handle.getObject());
            }
            fireChanged(SelectionEvent.ACTION_HANDLE, objs);
        }
    }

    public void setHandleGesture(HandleGesture gesture) {
        HandleGesture g = curHandleGesture;
        if (g != null) {
            suppressed.remove(g.getHandle().getObject());
        }


        Handle h = gesture.getHandle();
        suppressed.put(h.getObject(), MOVING_HANDLE);
        curHandleGesture = gesture;
    }

    public void setMovingShapes(Collection<? extends CanvasObject> shapes, int dx, int dy) {
        for (CanvasObject o : shapes) {
            suppressed.put(o, TRANSLATING);
        }
        moveDx = dx;
        moveDy = dy;
    }

    public void setHidden(Collection<? extends CanvasObject> shapes, boolean value) {
        if (value) {
            for (CanvasObject o : shapes) {
                suppressed.put(o, HIDDEN);
            }
        } else {
            suppressed.keySet().removeAll(shapes);
        }
    }

    public Location getMovingDelta() {
        return Location.create(moveDx, moveDy);
    }

    public void setMovingDelta(int dx, int dy) {
        moveDx = dx;
        moveDy = dy;
    }

    public void drawSuppressed(Graphics g, CanvasObject shape) {
        String state = suppressed.get(shape);
        if (state.equals(MOVING_HANDLE)) {
            shape.paint(g, curHandleGesture);
        } else if (state.equals(TRANSLATING)) {
            g.translate(moveDx, moveDy);
            shape.paint(g, null);
        }
    }

    void modelChanged(CanvasModelEvent event) {
        int action = event.getAction();
        switch (action) {
        case CanvasModelEvent.ACTION_REMOVED:
            Collection<? extends CanvasObject> affected = event.getAffected();
            if (affected != null) {
                selected.removeAll(affected);
                suppressed.keySet().removeAll(affected);
                Handle h = selectedHandle;
                if (h != null && affected.contains(h.getObject())) {
                    setHandleSelected(null);
                }
            }
            break;
        case CanvasModelEvent.ACTION_HANDLE_DELETED:
            if (event.getHandle().equals(selectedHandle)) {
                setHandleSelected(null);
            }
            break;
        case CanvasModelEvent.ACTION_HANDLE_MOVED:
            HandleGesture gesture = event.getHandleGesture();
            if (gesture.getHandle().equals(selectedHandle)) {
                setHandleSelected(gesture.getResultingHandle());

            }
			break;
		default:
			break;
        }
    }
}
