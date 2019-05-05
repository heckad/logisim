/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.proj;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.circuit.CircuitEvent;
import com.heckad.logisim.circuit.CircuitListener;
import com.heckad.logisim.circuit.SubcircuitFactory;
import com.heckad.logisim.comp.Component;
import com.heckad.logisim.comp.ComponentFactory;
import com.heckad.logisim.file.LibraryEvent;
import com.heckad.logisim.file.LibraryListener;
import com.heckad.logisim.file.LogisimFile;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.util.Dag;

public class Dependencies {
    private class MyListener
            implements LibraryListener, CircuitListener {
        @Override
        public void libraryChanged(LibraryEvent e) {
            switch (e.getAction()) {
            case LibraryEvent.ADD_TOOL:
                if (e.getData() instanceof AddTool) {
                    ComponentFactory factory = ((AddTool) e.getData()).getFactory();
                    if (factory instanceof SubcircuitFactory) {
                        SubcircuitFactory circFact = (SubcircuitFactory) factory;
                        processCircuit(circFact.getSubcircuit());
                    }
                }
                break;
            case LibraryEvent.REMOVE_TOOL:
                if (e.getData() instanceof AddTool) {
                    ComponentFactory factory = ((AddTool) e.getData()).getFactory();
                    if (factory instanceof SubcircuitFactory) {
                        SubcircuitFactory circFact = (SubcircuitFactory) factory;
                        Circuit circ = circFact.getSubcircuit();
                        depends.removeNode(circ);
                        circ.removeCircuitListener(this);
                    }
                }
                break;
            }
        }

        @Override
        public void circuitChanged(CircuitEvent e) {
            Component comp;
            switch (e.getAction()) {
            case CircuitEvent.ACTION_ADD:
                comp = (Component) e.getData();
                if (comp.getFactory() instanceof SubcircuitFactory) {
                    SubcircuitFactory factory = (SubcircuitFactory) comp.getFactory();
                    depends.addEdge(e.getCircuit(), factory.getSubcircuit());
                }
                break;
            case CircuitEvent.ACTION_REMOVE:
                comp = (Component) e.getData();
                if (comp.getFactory() instanceof SubcircuitFactory) {
                    SubcircuitFactory factory = (SubcircuitFactory) comp.getFactory();
                    boolean found = false;
                    for (Component o : e.getCircuit().getNonWires()) {
                        if (o.getFactory() == factory) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        depends.removeEdge(e.getCircuit(), factory.getSubcircuit());
                    }

                }
                break;
            case CircuitEvent.ACTION_CLEAR:
                depends.removeNode(e.getCircuit());
                break;
            }
        }
    }

    private MyListener myListener = new MyListener();
    private Dag depends = new Dag();

    Dependencies(LogisimFile file) {
        addDependencies(file);
    }

    public boolean canRemove(Circuit circ) {
        return !depends.hasPredecessors(circ);
    }

    public boolean canAdd(Circuit circ, Circuit sub) {
        return depends.canFollow(sub, circ);
    }

    private void addDependencies(LogisimFile file) {
        file.addLibraryListener(myListener);
        for (Circuit circuit : file.getCircuits()) {
            processCircuit(circuit);
        }
    }

    private void processCircuit(Circuit circ) {
        circ.addCircuitListener(myListener);
        for (Component comp : circ.getNonWires()) {
            if (comp.getFactory() instanceof SubcircuitFactory) {
                SubcircuitFactory factory = (SubcircuitFactory) comp.getFactory();
                depends.addEdge(circ, factory.getSubcircuit());
            }
        }
    }

}
