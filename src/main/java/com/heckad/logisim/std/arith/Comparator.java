/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.arith;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeOption;
import com.heckad.logisim.data.Attributes;
import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.data.Direction;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.InstanceFactory;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import com.heckad.logisim.instance.Port;
import com.heckad.logisim.instance.StdAttr;
import com.heckad.logisim.tools.key.BitWidthConfigurator;
import static com.heckad.logisim.util.LocaleString.*;

public class Comparator extends InstanceFactory {
    private static final AttributeOption SIGNED_OPTION
        = new AttributeOption("twosComplement", "twosComplement", getFromLocale("twosComplementOption"));
    private static final AttributeOption UNSIGNED_OPTION
        = new AttributeOption("unsigned", "unsigned", getFromLocale("unsignedOption"));
    private static final Attribute<AttributeOption> MODE_ATTRIBUTE
        = Attributes.forOption("mode", getFromLocale("comparatorType"),
                new AttributeOption[] { SIGNED_OPTION, UNSIGNED_OPTION });

    private static final int IN0   = 0;
    private static final int IN1   = 1;
    private static final int GT    = 2;
    private static final int EQ    = 3;
    private static final int LT    = 4;

    public Comparator() {
        super("Comparator", getFromLocale("comparatorComponent"));
        setAttributes(new Attribute[] { StdAttr.WIDTH, MODE_ATTRIBUTE },
                new Object[] { BitWidth.create(8), SIGNED_OPTION });
        setKeyConfigurator(new BitWidthConfigurator(StdAttr.WIDTH));
        setOffsetBounds(Bounds.create(-40, -20, 40, 40));
        setIconName("comparator.svg");

        Port[] ps = new Port[5];
        ps[IN0] = new Port(-40, -10, Port.INPUT,  StdAttr.WIDTH);
        ps[IN1] = new Port(-40,  10, Port.INPUT,  StdAttr.WIDTH);
        ps[GT]  = new Port(  0, -10, Port.OUTPUT, 1);
        ps[EQ]  = new Port(  0,   0, Port.OUTPUT, 1);
        ps[LT]  = new Port(  0,  10, Port.OUTPUT, 1);
        ps[IN0].setToolTip(getFromLocale("comparatorInputATip"));
        ps[IN1].setToolTip(getFromLocale("comparatorInputBTip"));
        ps[GT].setToolTip(getFromLocale("comparatorGreaterTip"));
        ps[EQ].setToolTip(getFromLocale("comparatorEqualTip"));
        ps[LT].setToolTip(getFromLocale("comparatorLessTip"));
        setPorts(ps);
    }

    @Override
    public void propagate(InstanceState state) {
        // get attributes
        BitWidth dataWidth = state.getAttributeValue(StdAttr.WIDTH);

        // compute outputs
        Value gt = Value.FALSE;
        Value eq = Value.TRUE;
        Value lt = Value.FALSE;

        Value a = state.getPort(IN0);
        Value b = state.getPort(IN1);
        Value[] ax = a.getAll();
        Value[] bx = b.getAll();
        int maxlen = Math.max(ax.length, bx.length);
        for (int pos = maxlen - 1; pos >= 0; pos--) {
            Value ab = pos < ax.length ? ax[pos] : Value.ERROR;
            Value bb = pos < bx.length ? bx[pos] : Value.ERROR;
            if (pos == ax.length - 1 && ab != bb) {
                Object mode = state.getAttributeValue(MODE_ATTRIBUTE);
                if (mode != UNSIGNED_OPTION) {
                    Value t = ab;
                    ab = bb;
                    bb = t;
                }
            }

            if (ab == Value.ERROR || bb == Value.ERROR) {
                gt = Value.ERROR;
                eq = Value.ERROR;
                lt = Value.ERROR;
                break;
            } else if (ab == Value.UNKNOWN || bb == Value.UNKNOWN) {
                gt = Value.UNKNOWN;
                eq = Value.UNKNOWN;
                lt = Value.UNKNOWN;
                break;
            } else if (ab != bb) {
                eq = Value.FALSE;
                if (ab == Value.TRUE) {
                    gt = Value.TRUE;
                }

                else {
                                    lt = Value.TRUE;
                }

                break;
            }
        }

        // propagate them
        int delay = (dataWidth.getWidth() + 2) * Adder.PER_DELAY;
        state.setPort(GT, gt, delay);
        state.setPort(EQ, eq, delay);
        state.setPort(LT, lt, delay);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawPort(IN0);
        painter.drawPort(IN1);
        painter.drawPort(GT, ">", Direction.WEST);
        painter.drawPort(EQ, "=", Direction.WEST);
        painter.drawPort(LT, "<", Direction.WEST);
    }


    //
    // methods for instances
    //
    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
    }

    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        instance.fireInvalidated();
    }
}
