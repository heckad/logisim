/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.gates;

import com.heckad.logisim.analyze.model.Expression;
import com.heckad.logisim.analyze.model.Expressions;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import static com.heckad.logisim.util.LocaleString.*;

class EvenParityGate extends AbstractGate {
    public static EvenParityGate FACTORY = new EvenParityGate();

    private EvenParityGate() {
        super("Even Parity", getFromLocale("evenParityComponent"));
        setRectangularLabel("2k");
        setIconNames("parityEvenGate.svg");
    }

    @Override
    public void paintIconShaped(InstancePainter painter) {
        paintIconRectangular(painter);
    }

    @Override
    protected void paintShape(InstancePainter painter, int width, int height) {
        paintRectangular(painter, width, height);
    }

    @Override
    protected void paintDinShape(InstancePainter painter, int width, int height,
            int inputs) {
        paintRectangular(painter, width, height);
    }

    @Override
    protected Value computeOutput(Value[] inputs, int numInputs, InstanceState state) {
        return GateFunctions.computeOddParity(inputs, numInputs).not();
    }

    @Override
    protected Expression computeExpression(Expression[] inputs, int numInputs) {
        Expression ret = inputs[0];
        for (int i = 1; i < numInputs; i++) {
            ret = Expressions.xor(ret, inputs[i]);
        }
        return Expressions.not(ret);
    }

    @Override
    protected Value getIdentity() { return Value.FALSE; }
}
