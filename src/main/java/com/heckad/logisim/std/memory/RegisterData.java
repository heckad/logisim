/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.memory;

import com.heckad.logisim.instance.InstanceData;

class RegisterData extends ClockState implements InstanceData {
    int value;

    public RegisterData() {
        value = 0;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}