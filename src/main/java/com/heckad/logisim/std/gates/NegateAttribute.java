/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.gates;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.Attributes;
import com.heckad.logisim.data.Direction;
import static com.heckad.logisim.util.LocaleString.*;

class NegateAttribute extends Attribute<Boolean> {
    private static Attribute<Boolean> BOOLEAN_ATTR = Attributes.forBoolean("negateDummy");

    int index;
    private Direction side;

    public NegateAttribute(int index, Direction side) {
        super("negate" + index, null);
        this.index = index;
        this.side = side;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NegateAttribute) {
            NegateAttribute o = (NegateAttribute) other;
            return this.index == o.index && this.side == o.side;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return index * 31 + (side == null ? 0 : side.hashCode());
    }

    @Override
    public String getDisplayName() {
        String ret = getFromLocale("gateNegateAttr", "" + (index + 1));
        if (side != null) {
            ret += " (" + side.toVerticalDisplayString() + ")";
        }
        return ret;
    }

    @Override
    public String toDisplayString(Boolean value) {
        return BOOLEAN_ATTR.toDisplayString(value);
    }

    @Override
    public Boolean parse(String value) {
        return BOOLEAN_ATTR.parse(value);
    }

    @Override
    public java.awt.Component getCellEditor(Boolean value) {
        return BOOLEAN_ATTR.getCellEditor(null, value);
    }


}
