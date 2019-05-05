/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.instance;

import com.heckad.logisim.comp.ComponentState;

public interface InstanceData extends ComponentState {
    @Override
    public Object clone();
}
