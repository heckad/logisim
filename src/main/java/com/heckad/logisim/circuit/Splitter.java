/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit;

import javax.swing.JPopupMenu;

import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.circuit.CircuitWires;
import com.heckad.logisim.comp.ComponentEvent;
import com.heckad.logisim.comp.ComponentFactory;
import com.heckad.logisim.comp.ComponentDrawContext;
import com.heckad.logisim.comp.ComponentUserEvent;
import com.heckad.logisim.comp.EndData;
import com.heckad.logisim.comp.ManagedComponent;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Direction;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.instance.StdAttr;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.tools.MenuExtender;
import com.heckad.logisim.tools.ToolTipMaker;
import com.heckad.logisim.tools.WireRepair;
import com.heckad.logisim.tools.WireRepairData;
import static com.heckad.logisim.util.LocaleString.*;

public class Splitter extends ManagedComponent
        implements WireRepair, ToolTipMaker, MenuExtender, AttributeListener {
    // basic data
    // how each bit maps to thread within end
    byte[] bit_thread;

    // derived data
    CircuitWires.SplitterData wire_data;

    public Splitter(Location loc, AttributeSet attrs) {
        super(loc, attrs, 3);
        configureComponent();
        attrs.addAttributeListener(this);
    }

    //
    // abstract ManagedComponent methods
    //
    @Override
    public ComponentFactory getFactory() {
        return SplitterFactory.instance;
    }

    @Override
    public void propagate(CircuitState state) {
        // handled by CircuitWires, nothing to do
        ;
    }

    @Override
    public boolean contains(Location loc) {
        if (super.contains(loc)) {
            Location myLoc = getLocation();
            Direction facing = getAttributeSet().getValue(StdAttr.FACING);
            if (facing == Direction.EAST || facing == Direction.WEST) {
                return Math.abs(loc.getX() - myLoc.getX()) > 5
                    || loc.manhattanDistanceTo(myLoc) <= 5;
            } else {
                return Math.abs(loc.getY() - myLoc.getY()) > 5
                    || loc.manhattanDistanceTo(myLoc) <= 5;
            }
        } else {
            return false;
        }
    }

    private synchronized void configureComponent() {
        SplitterAttributes attrs = (SplitterAttributes) getAttributeSet();
        SplitterParameters parms = attrs.getParameters();
        int fanout = attrs.fanout;
        byte[] bit_end = attrs.bit_end;

        // compute width of each end
        bit_thread = new byte[bit_end.length];
        byte[] end_width = new byte[fanout + 1];
        end_width[0] = (byte) bit_end.length;
        for (int i = 0; i < bit_end.length; i++) {
            byte thr = bit_end[i];
            if (thr > 0) {
                bit_thread[i] = end_width[thr];
                end_width[thr]++;
            } else {
                bit_thread[i] = -1;
            }
        }

        // compute end positions
        Location origin = getLocation();
        int x = origin.getX() + parms.getEnd0X();
        int y = origin.getY() + parms.getEnd0Y();
        int dx = parms.getEndToEndDeltaX();
        int dy = parms.getEndToEndDeltaY();

        EndData[] ends = new EndData[fanout + 1];
        ends[0] = new EndData(origin, BitWidth.create(bit_end.length), EndData.INPUT_OUTPUT);
        for (int i = 0; i < fanout; i++) {
            ends[i + 1] = new EndData(Location.create(x, y),
                    BitWidth.create(end_width[i + 1]), EndData.INPUT_OUTPUT);
            x += dx;
            y += dy;
        }
        wire_data = new CircuitWires.SplitterData(fanout);
        setEnds(ends);
        recomputeBounds();
        fireComponentInvalidated(new ComponentEvent(this));
    }

    //
    // user interface methods
    //
    @Override
    public void draw(ComponentDrawContext context) {
        SplitterAttributes attrs = (SplitterAttributes) getAttributeSet();
        if (attrs.appear == SplitterAttributes.APPEAR_LEGACY) {
            SplitterPainter.drawLegacy(context, attrs, getLocation());
        } else {
            Location loc = getLocation();
            SplitterPainter.drawLines(context, attrs, loc);
            SplitterPainter.drawLabels(context, attrs, loc);
            context.drawPins(this);
        }
    }

    @Override
    public Object getFeature(Object key) {
        if (key == WireRepair.class) {
            return this;
        }

        if (key == ToolTipMaker.class) {
            return this;
        }

        if (key == MenuExtender.class) {
            return this;
        }

        else {
            return super.getFeature(key);
        }

    }

    @Override
    public boolean shouldRepairWire(WireRepairData data) {
        return true;
    }

    @Override
    public String getToolTip(ComponentUserEvent e) {
        int end = -1;
        for (int i = getEnds().size() - 1; i >= 0; i--) {
            if (getEndLocation(i).manhattanDistanceTo(e.getX(), e.getY()) < 10) {
                end = i;
                break;
            }
        }

        if (end == 0) {
            return getFromLocale("splitterCombinedTip");
        } else if (end > 0){
            int bits = 0;
            StringBuilder buf = new StringBuilder();
            SplitterAttributes attrs = (SplitterAttributes) getAttributeSet();
            byte[] bit_end = attrs.bit_end;
            boolean inString = false;
            int beginString = 0;
            for (int i = 0; i < bit_end.length; i++) {
                if (bit_end[i] == end) {
                    bits++;
                    if (!inString) {
                        inString = true;
                        beginString = i;
                    }
                } else {
                    if (inString) {
                        appendBuf(buf, beginString, i - 1);
                        inString = false;
                    }
                }
            }
            if (inString) {
                appendBuf(buf, beginString, bit_end.length - 1);
            }

            String base;
            switch (bits) {
            case 0:  base = "splitterSplit0Tip"; break;
            case 1:  base = "splitterSplit1Tip"; break;
            default: base = "splitterSplitManyTip"; break;
            }
            return getFromLocale(base, buf.toString());
        } else {
            return null;
        }
    }
    private static void appendBuf(StringBuilder buf, int start, int end) {
        if (buf.length() > 0) {
            buf.append(",");
        }

        if (start == end) {
            buf.append(start);
        } else {
            buf.append(start + "-" + end);
        }
    }

    @Override
    public void configureMenu(JPopupMenu menu, Project proj) {
        menu.addSeparator();
        menu.add(new SplitterDistributeItem(proj, this, 1));
        menu.add(new SplitterDistributeItem(proj, this, -1));
    }

    //
    // AttributeListener methods
    //
    @Override
    public void attributeListChanged(AttributeEvent e) { }

    @Override
    public void attributeValueChanged(AttributeEvent e) {
        configureComponent();
    }
}