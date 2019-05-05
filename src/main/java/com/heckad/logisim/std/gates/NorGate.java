/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.gates;

import java.awt.Graphics;

import com.heckad.logisim.analyze.model.Expression;
import com.heckad.logisim.analyze.model.Expressions;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import com.heckad.logisim.tools.WireRepairData;
import com.heckad.logisim.util.GraphicsUtil;
import static com.heckad.logisim.util.LocaleString.*;

class NorGate extends AbstractGate {
    public static NorGate FACTORY = new NorGate();

    private NorGate() {
        super("NOR Gate", getFromLocale("norGateComponent"));
        setNegateOutput(true);
        setRectangularLabel(OrGate.FACTORY.getRectangularLabel(null));
        setIconNames("norGate.svg", "norGateRect.svg", "dinNorGate.svg");
        setPaintInputLines(true);
    }

    @Override
    public void paintIconShaped(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        GraphicsUtil.drawCenteredArc(g,   0, -5, 22, -90,  53);
        GraphicsUtil.drawCenteredArc(g,   0, 23, 22,  90, -53);
        GraphicsUtil.drawCenteredArc(g, -12,  9, 16, -30, 60);
        g.drawOval(16, 8, 4, 4);
    }

    @Override
    protected void paintShape(InstancePainter painter, int width, int height) {
        PainterShaped.paintOr(painter, width, height);
    }

    @Override
    protected void paintDinShape(InstancePainter painter, int width, int height,
            int inputs) {
        PainterDin.paintOr(painter, width, height, true);
    }

    @Override
    protected Value computeOutput(Value[] inputs, int numInputs,
            InstanceState state) {
        return GateFunctions.computeOr(inputs, numInputs).not();
    }

    @Override
    protected boolean shouldRepairWire(Instance instance, WireRepairData data) {
        return !data.getPoint().equals(instance.getLocation());
    }

    @Override
    protected Expression computeExpression(Expression[] inputs, int numInputs) {
        Expression ret = inputs[0];
        for (int i = 1; i < numInputs; i++) {
            ret = Expressions.or(ret, inputs[i]);
        }
        return Expressions.not(ret);
    }

    @Override
    protected Value getIdentity() { return Value.FALSE; }
}
