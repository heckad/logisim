/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit;

import com.heckad.logisim.comp.Component;
import com.heckad.logisim.data.Attribute;

public interface CircuitMutator {
    public void clear(Circuit circuit);
    public void add(Circuit circuit, Component comp);
    public void remove(Circuit circuit, Component comp);
    public void replace(Circuit circuit, Component oldComponent, Component newComponent);
    public void replace(Circuit circuit, ReplacementMap replacements);
    public void set(Circuit circuit, Component comp, Attribute<?> attr, Object value);
    public void setForCircuit(Circuit circuit, Attribute<?> attr, Object value);
}
