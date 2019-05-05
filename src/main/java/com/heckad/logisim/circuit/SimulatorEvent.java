/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit;

public class SimulatorEvent {
    private Simulator source;

    public SimulatorEvent(Simulator source) {
        this.source = source;
    }

    public Simulator getSource() {
        return source;
    }
}
