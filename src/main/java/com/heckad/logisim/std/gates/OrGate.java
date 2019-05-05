/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.gates;

import static com.heckad.logisim.util.LocaleString.*;

import java.awt.Graphics;

import com.heckad.logisim.analyze.model.Expression;
import com.heckad.logisim.analyze.model.Expressions;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import com.heckad.logisim.tools.WireRepairData;

class OrGate extends AbstractGate {
    public static OrGate FACTORY = new OrGate();

    private OrGate() {
        super("OR Gate", getFromLocale("orGateComponent"));
        setRectangularLabel("\u2265" + "1");
        setIconNames("orGate.svg", "orGateRect.svg", "dinOrGate.svg");
        setPaintInputLines(true);
    }

    @Override
    public void paintIconShaped(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        //g.drawImage(image.createImage(image.getWidth(), image.getHeight()), image.getWidth(), image.getHeight(), null);
    }

    @Override
    protected void paintShape(InstancePainter painter, int width, int height) {
        Graphics g = painter.getGraphics();
        //g.drawImage(image.createImage(image.getWidth(), image.getHeight()), image.getWidth(), image.getHeight(), null);
        PainterShaped.paintOr(painter, width, height);
    }

    @Override
    protected void paintDinShape(InstancePainter painter, int width, int height,
            int inputs) {
        PainterDin.paintOr(painter, width, height, false);
    }

    @Override
    protected Value computeOutput(Value[] inputs, int numInputs,
            InstanceState state) {
        return GateFunctions.computeOr(inputs, numInputs);
    }

    @Override
    protected boolean shouldRepairWire(Instance instance, WireRepairData data) {
        boolean ret = !data.getPoint().equals(instance.getLocation());
        return ret;
    }

    @Override
    protected Expression computeExpression(Expression[] inputs, int numInputs) {
        Expression ret = inputs[0];
        for (int i = 1; i < numInputs; i++) {
            ret = Expressions.or(ret, inputs[i]);
        }
        return ret;
    }

    @Override
    protected Value getIdentity() { return Value.FALSE; }
}
