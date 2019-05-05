/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit.appear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.heckad.logisim.circuit.ReplacementMap;
import com.heckad.logisim.comp.Component;
import com.heckad.logisim.comp.ComponentEvent;
import com.heckad.logisim.comp.ComponentListener;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.StdAttr;
import com.heckad.logisim.std.wiring.Pin;

public class CircuitPins {
    private class MyComponentListener
            implements ComponentListener, AttributeListener {
        @Override
        public void endChanged(ComponentEvent e) {
            appearanceManager.updatePorts();
        }
        @Override
        public void componentInvalidated(ComponentEvent e) { }

        @Override
        public void attributeListChanged(AttributeEvent e) { }
        @Override
        public void attributeValueChanged(AttributeEvent e) {
            Attribute<?> attr = e.getAttribute();
            if (attr == StdAttr.FACING || attr == StdAttr.LABEL
                    || attr == Pin.ATTR_TYPE) {
                appearanceManager.updatePorts();
            }
        }
    }

    private PortManager appearanceManager;
    private MyComponentListener myComponentListener;
    private Set<Instance> pins;

    CircuitPins(PortManager appearanceManager) {
        this.appearanceManager = appearanceManager;
        myComponentListener = new MyComponentListener();
        pins = new HashSet<Instance>();
    }

    public void transactionCompleted(ReplacementMap repl) {
        // determine the changes
        Set<Instance> adds = new HashSet<Instance>();
        Set<Instance> removes = new HashSet<Instance>();
        Map<Instance, Instance> replaces = new HashMap<Instance, Instance>();
        for (Component comp : repl.getAdditions()) {
            if (comp.getFactory() instanceof Pin) {
                Instance in = Instance.getInstanceFor(comp);
                boolean added = pins.add(in);
                if (added) {
                    comp.addComponentListener(myComponentListener);
                    in.getAttributeSet().addAttributeListener(myComponentListener);
                    adds.add(in);
                }
            }
        }
        for (Component comp : repl.getRemovals()) {
            if (comp.getFactory() instanceof Pin) {
                Instance in = Instance.getInstanceFor(comp);
                boolean removed = pins.remove(in);
                if (removed) {
                    comp.removeComponentListener(myComponentListener);
                    in.getAttributeSet().removeAttributeListener(myComponentListener);
                    Collection<Component> rs = repl.getComponentsReplacing(comp);
                    if (rs.isEmpty()) {
                        removes.add(in);
                    } else {
                        Component r = rs.iterator().next();
                        Instance rin = Instance.getInstanceFor(r);
                        adds.remove(rin);
                        replaces.put(in, rin);
                    }
                }
            }
        }

        appearanceManager.updatePorts(adds, removes, replaces, getPins());
    }

    public Collection<Instance> getPins() {
        return new ArrayList<Instance>(pins);
    }
}
