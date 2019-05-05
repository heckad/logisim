/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.comp.Component;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.gui.generic.AttrTableSetException;
import com.heckad.logisim.gui.generic.AttributeSetTableModel;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.tools.SetAttributeAction;
import static com.heckad.logisim.util.LocaleString.*;

class AttrTableComponentModel extends AttributeSetTableModel {
    Project proj;
    Circuit circ;
    Component comp;

    AttrTableComponentModel(Project proj, Circuit circ, Component comp) {
        super(comp.getAttributeSet());
        this.proj = proj;
        this.circ = circ;
        this.comp = comp;
    }

    public Circuit getCircuit() {
        return circ;
    }

    public Component getComponent() {
        return comp;
    }

    @Override
    public String getTitle() {
        return comp.getFactory().getDisplayName();
    }

    @Override
    public void setValueRequested(Attribute<Object> attr, Object value)
            throws AttrTableSetException {
        if (!proj.getLogisimFile().contains(circ)) {
            String msg = getFromLocale("cannotModifyCircuitError");
            throw new AttrTableSetException(msg);
        } else {
            SetAttributeAction act = new SetAttributeAction(circ,
                    getFromLocale("changeAttributeAction"));
            act.set(comp, attr, value);
            proj.doAction(act);
        }
    }
}


