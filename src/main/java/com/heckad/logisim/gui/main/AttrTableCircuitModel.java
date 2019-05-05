/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.circuit.CircuitMutation;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.gui.generic.AttrTableSetException;
import com.heckad.logisim.gui.generic.AttributeSetTableModel;
import com.heckad.logisim.proj.Project;
import static com.heckad.logisim.util.LocaleString.*;

public class AttrTableCircuitModel extends AttributeSetTableModel {
    private Project proj;
    private Circuit circ;

    public AttrTableCircuitModel(Project proj, Circuit circ) {
        super(circ.getStaticAttributes());
        this.proj = proj;
        this.circ = circ;
    }

    @Override
    public String getTitle() {
        return getFromLocale("circuitAttrTitle", circ.getName());
    }

    @Override
    public void setValueRequested(Attribute<Object> attr, Object value)
            throws AttrTableSetException {
        if (!proj.getLogisimFile().contains(circ)) {
            String msg = getFromLocale("cannotModifyCircuitError");
            throw new AttrTableSetException(msg);
        } else {
            CircuitMutation xn = new CircuitMutation(circ);
            xn.setForCircuit(attr, value);
            proj.doAction(xn.toAction(getFromLocale("changeCircuitAttrAction")));
        }
    }
}

