/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import java.util.Vector;

import javax.swing.JList;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.file.LogisimFile;
import com.heckad.logisim.proj.Project;

@SuppressWarnings("serial")
class CircuitJList extends JList<Circuit> {
    public CircuitJList(Project proj, boolean includeEmpty) {
        LogisimFile file = proj.getLogisimFile();
        Circuit current = proj.getCurrentCircuit();
        Vector<Circuit> options = new Vector<Circuit>();
        boolean currentFound = false;
        for (Circuit circ : file.getCircuits()) {
            if (!includeEmpty || circ.getBounds() != Bounds.EMPTY_BOUNDS) {
                if (circ == current) currentFound = true;
                options.add(circ);
            }
        }

        setListData(options);
        if (currentFound) setSelectedValue(current, true);
        setVisibleRowCount(Math.min(6, options.size()));
    }
}
