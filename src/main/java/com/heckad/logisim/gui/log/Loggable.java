/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.log;

import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.data.Value;

public interface Loggable {
    public Object[] getLogOptions(CircuitState state);
    public String getLogName(Object option);
    public Value getLogValue(CircuitState state, Object option);
}
