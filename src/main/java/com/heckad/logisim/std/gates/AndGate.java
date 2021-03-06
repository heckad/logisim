/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.gates;

import static com.heckad.logisim.util.LocaleString.*;

import java.awt.Graphics;

import com.heckad.logisim.analyze.model.Expression;
import com.heckad.logisim.analyze.model.Expressions;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import com.heckad.logisim.util.GraphicsUtil;

class AndGate extends AbstractGate {
    public static AndGate FACTORY = new AndGate();

    private AndGate() {
        super("AND Gate", getFromLocale("andGateComponent"));
        setRectangularLabel("&");
        setIconNames("/logisim/icons/andGate.svg", "/logisim/icons/andGateRect.svg", "/logisim/icons/dinAndGate.svg");
    }

    @Override
    protected void paintIconShaped(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        int[] xp = new int[] { 10, 2, 2, 10 };
        int[] yp = new int[] { 2, 2, 18, 18 };
        g.drawPolyline(xp, yp, 4);
        GraphicsUtil.drawCenteredArc(g, 10, 10, 8, -90, 180);
    }

    @Override
    protected void paintShape(InstancePainter painter, int width, int height) {
        PainterShaped.paintAnd(painter, width, height);
    }

    @Override
    protected void paintDinShape(InstancePainter painter, int width, int height, int inputs) {
        PainterDin.paintAnd(painter, width, height, false);
    }

    @Override
    protected Value computeOutput(Value[] inputs, int numInputs,
            InstanceState state) {
        return GateFunctions.computeAnd(inputs, numInputs);
    }

    @Override
    protected Expression computeExpression(Expression[] inputs, int numInputs) {
        Expression ret = inputs[0];
        for (int i = 1; i < numInputs; i++) {
            ret = Expressions.and(ret, inputs[i]);
        }
        return ret;
    }

    @Override
    protected Value getIdentity() { return Value.TRUE; }
}
