/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools.key;

import com.heckad.logisim.data.Attribute;

public class IntegerConfigurator extends NumericConfigurator<Integer> {
    public IntegerConfigurator(Attribute<Integer> attr, int min, int max, int modifiersEx) {
        super(attr, min, max, modifiersEx);
    }

    public IntegerConfigurator(Attribute<Integer> attr, int min, int max,
            int modifiersEx, int radix) {
        super(attr, min, max, modifiersEx, radix);
    }

    @Override
    protected Integer createValue(int val) {
        return Integer.valueOf(val);
    }
}
