/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.memory;

import com.heckad.logisim.data.Value;
import static com.heckad.logisim.util.LocaleString.*;

public class DFlipFlop extends AbstractFlipFlop {
    public DFlipFlop() {
        super("D Flip-Flop", "dFlipFlop.svg",
                getFromLocale("dFlipFlopComponent"), 1, true);
    }

    @Override
    protected String getInputName(int index) {
        return "D";
    }

    @Override
    protected Value computeValue(Value[] inputs, Value curValue) {
        return inputs[0];
    }
}
