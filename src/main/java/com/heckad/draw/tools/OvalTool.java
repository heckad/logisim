/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.tools;

import java.awt.Graphics;
import java.util.List;

import javax.swing.Icon;

import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.shapes.DrawAttr;
import com.heckad.draw.shapes.Oval;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.util.Icons;

public class OvalTool extends RectangularTool {
    private DrawingAttributeSet attrs;

    public OvalTool(DrawingAttributeSet attrs) {
        this.attrs = attrs;
    }

    @Override
    public Icon getIcon() {
        return Icons.getIcon("drawoval.svg");
    }

    @Override
    public List<Attribute<?>> getAttributes() {
        return DrawAttr.getFillAttributes(attrs.getValue(DrawAttr.PAINT_TYPE));
    }

    @Override
    public CanvasObject createShape(int x, int y, int w, int h) {
        return attrs.applyTo(new Oval(x, y, w, h));
    }

    @Override
    public void drawShape(Graphics g, int x, int y, int w, int h) {
        g.drawOval(x, y, w, h);
    }

    @Override
    public void fillShape(Graphics g, int x, int y, int w, int h) {
        g.fillOval(x, y, w, h);
    }
}
