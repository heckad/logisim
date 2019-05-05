/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.model;

import java.awt.Graphics;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.heckad.draw.canvas.Selection;
import com.heckad.draw.shapes.Text;
import com.heckad.logisim.data.Bounds;

public interface CanvasModel {
    // listener methods
    public void addCanvasModelListener(CanvasModelListener l);
    public void removeCanvasModelListener(CanvasModelListener l);

    // methods that don't change any data in the model
    public void paint(Graphics g, Selection selection);
    public List<CanvasObject> getObjectsFromTop();
    public List<CanvasObject> getObjectsFromBottom();
    public Collection<CanvasObject> getObjectsIn(Bounds bds);
    public Collection<CanvasObject> getObjectsOverlapping(CanvasObject shape);

    // methods that alter the model
    public void addObjects(int index, Collection<? extends CanvasObject> shapes);
    public void addObjects(Map<? extends CanvasObject, Integer> shapes);
    public void removeObjects(Collection<? extends CanvasObject> shapes);
    public void translateObjects(Collection<? extends CanvasObject> shapes, int dx, int dy);
    public void reorderObjects(List<ReorderRequest> requests);
    public Handle moveHandle(HandleGesture gesture);
    public void insertHandle(Handle desired, Handle previous);
    public Handle deleteHandle(Handle handle);
    public void setAttributeValues(Map<AttributeMapKey,Object> values);
    public void setText(Text text, String value);
}
