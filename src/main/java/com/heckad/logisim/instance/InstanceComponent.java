/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.instance;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.list.UnmodifiableList;

import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.comp.Component;
import com.heckad.logisim.comp.ComponentDrawContext;
import com.heckad.logisim.comp.ComponentEvent;
import com.heckad.logisim.comp.ComponentFactory;
import com.heckad.logisim.comp.ComponentListener;
import com.heckad.logisim.comp.ComponentUserEvent;
import com.heckad.logisim.comp.EndData;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.tools.TextEditable;
import com.heckad.logisim.tools.ToolTipMaker;
import com.heckad.logisim.util.EventSourceWeakSupport;

class InstanceComponent implements Component, AttributeListener, ToolTipMaker {
    private EventSourceWeakSupport<ComponentListener> listeners;
    private InstanceFactory factory;
    private Instance instance;
    private Location loc;
    private Bounds bounds;
    private List<Port> portList;
    private EndData[] endArray;
    private List<EndData> endList;
    private boolean hasToolTips;
    private HashSet<Attribute<BitWidth>> widthAttrs;
    private AttributeSet attrs;
    private boolean attrListenRequested;
    private InstanceTextField textField;

    InstanceComponent(InstanceFactory factory, Location loc,
            AttributeSet attrs) {
        this.listeners = null;
        this.factory = factory;
        this.instance = new Instance(this);
        this.loc = loc;
        this.bounds = factory.getOffsetBounds(attrs).translate(loc.getX(), loc.getY());
        this.portList = factory.getPorts();
        this.endArray = null;
        this.hasToolTips = false;
        this.attrs = attrs;
        this.attrListenRequested = false;
        this.textField = null;

        computeEnds();
    }

    private void computeEnds() {
        List<Port> ports = portList;
        EndData[] esOld = endArray;
        int esOldLength = esOld == null ? 0 : esOld.length;
        EndData[] es = esOld;
        if (es == null || es.length != ports.size()) {
            es = new EndData[ports.size()];
            if (esOldLength > 0) {
                int toCopy = Math.min(esOldLength, es.length);
                System.arraycopy(esOld, 0, es, 0, toCopy);
            }
        }
        HashSet<Attribute<BitWidth>> wattrs = null;
        boolean toolTipFound = false;
        ArrayList<EndData> endsChangedOld = null;
        ArrayList<EndData> endsChangedNew = null;
        Iterator<Port> pit = ports.iterator();
        for (int i = 0; pit.hasNext() || i < esOldLength; i++) {
            Port p = pit.hasNext() ? pit.next() : null;
            EndData oldEnd = i < esOldLength ? esOld[i] : null;
            EndData newEnd = p == null ? null : p.toEnd(loc, attrs);
            if (oldEnd == null || !oldEnd.equals(newEnd)) {
                if (newEnd != null) {
                    es[i] = newEnd;
                }

                if (endsChangedOld == null) {
                    endsChangedOld = new ArrayList<EndData>();
                    endsChangedNew = new ArrayList<EndData>();
                }
                endsChangedOld.add(oldEnd);
                endsChangedNew.add(newEnd);
            }

            if (p != null) {
                Attribute<BitWidth> attr = p.getWidthAttribute();
                if (attr != null) {
                    if (wattrs == null) {
                        wattrs = new HashSet<Attribute<BitWidth>>();
                    }
                    wattrs.add(attr);
                }

                if (p.getToolTip() != null) {
                    toolTipFound = true;
                }

            }
        }
        if (!attrListenRequested) {
            HashSet<Attribute<BitWidth>> oldWattrs = widthAttrs;
            if (wattrs == null && oldWattrs != null) {
                getAttributeSet().removeAttributeListener(this);
            } else if (wattrs != null && oldWattrs == null) {
                getAttributeSet().addAttributeListener(this);
            }
        }
        if (es != esOld) {
            endArray = es;
            endList = UnmodifiableList.decorate(Arrays.asList(es));
        }
        widthAttrs = wattrs;
        hasToolTips = toolTipFound;
        if (endsChangedOld != null) {
            fireEndsChanged(endsChangedOld, endsChangedNew);
        }
    }

    //
    // listening methods
    //
    @Override
    public void addComponentListener(ComponentListener l) {
        EventSourceWeakSupport<ComponentListener> ls = listeners;
        if (ls == null) {
            ls = new EventSourceWeakSupport<ComponentListener>();
            ls.add(l);
            listeners = ls;
        } else {
            ls.add(l);
        }
    }

    @Override
    public void removeComponentListener(ComponentListener l) {
        if (listeners != null) {
            listeners.remove(l);
            if (listeners.isEmpty()) {
                listeners = null;
            }

        }
    }

    private void fireEndsChanged(ArrayList<EndData> oldEnds,
            ArrayList<EndData> newEnds) {
        EventSourceWeakSupport<ComponentListener> ls = listeners;
        if (ls != null) {
            ComponentEvent e = null;
            for (ComponentListener l : ls) {
                if (e == null) {
                    e = new ComponentEvent(this, oldEnds, newEnds);
                }

                l.endChanged(e);
            }
        }
    }

    void fireInvalidated() {
        EventSourceWeakSupport<ComponentListener> ls = listeners;
        if (ls != null) {
            ComponentEvent e = null;
            for (ComponentListener l : ls) {
                if (e == null) {
                    e = new ComponentEvent(this);
                }

                l.componentInvalidated(e);
            }
        }
    }

    //
    // basic information methods
    //
    @Override
    public ComponentFactory getFactory() {
        return factory;
    }

    @Override
    public AttributeSet getAttributeSet() {
        return attrs;
    }

    @Override
    public Object getFeature(Object key) {
        Object ret = factory.getInstanceFeature(instance, key);
        if (ret != null) {
            return ret;
        } else if (key == ToolTipMaker.class) {
            Object defaultTip = factory.getDefaultToolTip();
            if (hasToolTips || defaultTip != null) {
                return this;
            }

        } else if (key == TextEditable.class) {
            InstanceTextField field = textField;
            if (field != null) {
                return field;
            }

        }
        return null;
    }

    //
    // location/extent methods
    //
    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public Bounds getBounds() {
        return bounds;
    }

    @Override
    public Bounds getBounds(Graphics g) {
        Bounds ret = bounds;
        InstanceTextField field = textField;
        if (field != null) {
            ret = ret.add(field.getBounds(g));
        }

        return ret;
    }

    @Override
    public boolean contains(Location pt) {
        Location translated = pt.translate(-loc.getX(), -loc.getY());
        InstanceFactory factory = instance.getFactory();
        return factory.contains(translated, instance.getAttributeSet());
    }

    @Override
    public boolean contains(Location pt, Graphics g) {
        InstanceTextField field = textField;
        if (field != null && field.getBounds(g).contains(pt)) {
            return true;
        }

        else {
            return contains(pt);
        }

    }

    //
    // propagation methods
    //
    @Override
    public List<EndData> getEnds() {
        return endList;
    }

    @Override
    public EndData getEnd(int index) {
        return endArray[index];
    }

    @Override
    public boolean endsAt(Location pt) {
        EndData[] ends = endArray;
        for (int i = 0; i < ends.length; i++) {
            if (ends[i].getLocation().equals(pt)) {
                return true;
            }

        }
        return false;
    }

    @Override
    public void propagate(CircuitState state) {
        factory.propagate(state.getInstanceState(this));
    }

    //
    // drawing methods
    //
    @Override
    public void draw(ComponentDrawContext context) {
        InstancePainter painter = context.getInstancePainter();
        painter.setInstance(this);
        factory.paintInstance(painter);
    }

    @Override
    public void expose(ComponentDrawContext context) {
        Bounds b = bounds;
        context.getDestination().repaint(b.getX(), b.getY(), b.getWidth(), b.getHeight());
    }

    @Override
    public String getToolTip(ComponentUserEvent e) {
        int x = e.getX();
        int y = e.getY();
        int i = -1;
        for (EndData end : endArray) {
            i++;
            if (end.getLocation().manhattanDistanceTo(x, y) < 10) {
                Port p = portList.get(i);
                return p.getToolTip();
            }
        }
        String defaultTip = factory.getDefaultToolTip();
        return defaultTip == null ? null : defaultTip.toString();
    }

    //
    // AttributeListener methods
    //
    @Override
    public void attributeListChanged(AttributeEvent e) { }

    @Override
    public void attributeValueChanged(AttributeEvent e) {
        Attribute<?> attr = e.getAttribute();
        if (widthAttrs != null && widthAttrs.contains(attr)) {
            computeEnds();
        }

        if (attrListenRequested) {
            factory.instanceAttributeChanged(instance, e.getAttribute());
        }
    }

    //
    // methods for InstancePainter
    //
    void drawLabel(ComponentDrawContext context) {
        InstanceTextField field = textField;
        if (field != null) {
            field.draw(this, context);
        }

    }

    //
    // methods for Instance
    //
    Instance getInstance() {
        return instance;
    }

    List<Port> getPorts() {
        return portList;
    }

    void setPorts(Port[] ports) {
        portList = UnmodifiableList.decorate(Arrays.asList(ports.clone()));
        computeEnds();
    }

    void recomputeBounds() {
        Location p = loc;
        bounds = factory.getOffsetBounds(attrs).translate(p.getX(), p.getY());
    }

    void addAttributeListener(Instance instance) {
        if (!attrListenRequested) {
            attrListenRequested = true;
            if (widthAttrs == null) {
                getAttributeSet().addAttributeListener(this);
            }

        }
    }

    void setTextField(Attribute<String> labelAttr, Attribute<Font> fontAttr,
            int x, int y, int halign, int valign) {
        InstanceTextField field = textField;
        if (field == null) {
            field = new InstanceTextField(this);
            field.update(labelAttr, fontAttr, x, y, halign, valign);
            textField = field;
        } else {
            field.update(labelAttr, fontAttr, x, y, halign, valign);
        }
    }

}
