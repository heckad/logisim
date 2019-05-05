/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.menu;

import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.circuit.Simulator;

public interface SimulateListener {
    public void stateChangeRequested(Simulator sim, CircuitState state);
}
