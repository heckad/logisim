/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.data;

import com.heckad.logisim.LogisimVersion;

public interface AttributeDefaultProvider {
    public boolean isAllDefaultValues(AttributeSet attrs, LogisimVersion ver);
    public Object getDefaultAttributeValue(Attribute<?> attr, LogisimVersion ver);
}
