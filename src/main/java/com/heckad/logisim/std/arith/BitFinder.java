/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.arith;

import java.awt.Graphics;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeOption;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.data.Attributes;
import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.data.Value;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.InstanceFactory;
import com.heckad.logisim.instance.InstancePainter;
import com.heckad.logisim.instance.InstanceState;
import com.heckad.logisim.instance.Port;
import com.heckad.logisim.instance.StdAttr;
import com.heckad.logisim.tools.key.BitWidthConfigurator;
import com.heckad.logisim.util.GraphicsUtil;
import static com.heckad.logisim.util.LocaleString.*;

public class BitFinder extends InstanceFactory {
    static final AttributeOption LOW_ONE
        = new AttributeOption("low1", getFromLocale("bitFinderLowOption", "1"));
    static final AttributeOption HIGH_ONE
    = new AttributeOption("high1", getFromLocale("bitFinderHighOption", "1"));
    static final AttributeOption LOW_ZERO
        = new AttributeOption("low0", getFromLocale("bitFinderLowOption", "0"));
    static final AttributeOption HIGH_ZERO
        = new AttributeOption("high0", getFromLocale("bitFinderHighOption", "0"));
    static final Attribute<AttributeOption> TYPE
        = Attributes.forOption("type", getFromLocale("bitFinderTypeAttr"),
                new AttributeOption[] { LOW_ONE, HIGH_ONE, LOW_ZERO, HIGH_ZERO });

    public BitFinder() {
        super("BitFinder", getFromLocale("bitFinderComponent"));
        setAttributes(new Attribute[] {
                StdAttr.WIDTH, TYPE
            }, new Object[] {
                BitWidth.create(8), LOW_ONE
            });
        setKeyConfigurator(new BitWidthConfigurator(StdAttr.WIDTH));
        setIconName("bitfindr.svg");
    }

    @Override
    public Bounds getOffsetBounds(AttributeSet attrs) {
        return Bounds.create(-40, -20, 40, 40);
    }

    @Override
    protected void configureNewInstance(Instance instance) {
        configurePorts(instance);
        instance.addAttributeListener();
    }

    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == StdAttr.WIDTH) {
            configurePorts(instance);
        } else if (attr == TYPE) {
            instance.fireInvalidated();
        }
    }

    private void configurePorts(Instance instance) {
        BitWidth inWidth = instance.getAttributeValue(StdAttr.WIDTH);
        int outWidth = computeOutputBits(inWidth.getWidth() - 1);

        Port[] ps = new Port[3];
        ps[0] = new Port(-20,  20, Port.OUTPUT, BitWidth.ONE);
        ps[1] = new Port(  0,   0, Port.OUTPUT, BitWidth.create(outWidth));
        ps[2] = new Port(-40,   0, Port.INPUT,  inWidth);

        Object type = instance.getAttributeValue(TYPE);
        if (type == HIGH_ZERO) {
            ps[0].setToolTip(getFromLocale("bitFinderPresentTip", "0"));
            ps[1].setToolTip(getFromLocale("bitFinderIndexHighTip", "0"));
        } else if (type == LOW_ZERO) {
            ps[0].setToolTip(getFromLocale("bitFinderPresentTip", "0"));
            ps[1].setToolTip(getFromLocale("bitFinderIndexLowTip", "0"));
        } else if (type == HIGH_ONE) {
            ps[0].setToolTip(getFromLocale("bitFinderPresentTip", "1"));
            ps[1].setToolTip(getFromLocale("bitFinderIndexHighTip", "1"));
        } else {
            ps[0].setToolTip(getFromLocale("bitFinderPresentTip", "1"));
            ps[1].setToolTip(getFromLocale("bitFinderIndexLowTip", "1"));
        }
        ps[2].setToolTip(getFromLocale("bitFinderInputTip"));
        instance.setPorts(ps);
    }

    private int computeOutputBits(int maxBits) {
        int outWidth = 1;
        while ((1 << outWidth) <= maxBits) outWidth++;
        return outWidth;
    }

    @Override
    public void propagate(InstanceState state) {
        int width = state.getAttributeValue(StdAttr.WIDTH).getWidth();
        int outWidth = computeOutputBits(width - 1);
        Object type = state.getAttributeValue(TYPE);

        Value[] bits = state.getPort(2).getAll();
        Value want;
        int i;
        if (type == HIGH_ZERO) {
            want = Value.FALSE;
            for (i = bits.length - 1; i >= 0 && bits[i] == Value.TRUE; i--) { }
        } else if (type == LOW_ZERO) {
            want = Value.FALSE;
            for (i = 0; i < bits.length && bits[i] == Value.TRUE; i++) { }
        } else if (type == HIGH_ONE) {
            want = Value.TRUE;
            for (i = bits.length - 1; i >= 0 && bits[i] == Value.FALSE; i--) { }
        } else {
            want = Value.TRUE;
            for (i = 0; i < bits.length && bits[i] == Value.FALSE; i++) { }
        }

        Value present;
        Value index;
        if (i < 0 || i >= bits.length) {
            present = Value.FALSE;
            index = Value.createKnown(BitWidth.create(outWidth), 0);
        } else if (bits[i] == want) {
            present = Value.TRUE;
            index = Value.createKnown(BitWidth.create(outWidth), i);
        } else {
            present = Value.ERROR;
            index = Value.createError(BitWidth.create(outWidth));
        }

        int delay = outWidth * Adder.PER_DELAY;
        state.setPort(0, present, delay);
        state.setPort(1, index, delay);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        painter.drawBounds();
        painter.drawPorts();

        String top = getFromLocale("bitFinderFindLabel");
        String mid;
        String bot;
        Object type = painter.getAttributeValue(TYPE);
        if (type == HIGH_ZERO) {
            mid = getFromLocale("bitFinderHighLabel");
            bot = "0";
        } else if (type == LOW_ZERO) {
            mid = getFromLocale("bitFinderLowLabel");
            bot = "0";
        } else if (type == HIGH_ONE) {
            mid = getFromLocale("bitFinderHighLabel");
            bot = "1";
        } else {
            mid = getFromLocale("bitFinderLowLabel");
            bot = "1";
        }

        Bounds bds = painter.getBounds();
        int x = bds.getX() + bds.getWidth() / 2;
        int y0 = bds.getY();
        GraphicsUtil.drawCenteredText(g, top, x, y0 + 8);
        GraphicsUtil.drawCenteredText(g, mid, x, y0 + 20);
        GraphicsUtil.drawCenteredText(g, bot, x, y0 + 32);
    }
}
