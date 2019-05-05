/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools.key;

public interface KeyConfigurator {
    public KeyConfigurator clone();
    public KeyConfigurationResult keyEventReceived(KeyConfigurationEvent event);
}
