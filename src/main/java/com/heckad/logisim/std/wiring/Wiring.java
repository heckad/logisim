/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.wiring;

import java.util.ArrayList;
import java.util.List;

import com.heckad.logisim.circuit.SplitterFactory;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeOption;
import com.heckad.logisim.data.Attributes;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.tools.FactoryDescription;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;
import static com.heckad.logisim.util.LocaleString.*;

public class Wiring extends Library {

    static final AttributeOption GATE_TOP_LEFT
        = new AttributeOption("tl", getFromLocale("wiringGateTopLeftOption"));
    static final AttributeOption GATE_BOTTOM_RIGHT
        = new AttributeOption("br", getFromLocale("wiringGateBottomRightOption"));
    static final Attribute<AttributeOption> ATTR_GATE = Attributes.forOption("gate",
            getFromLocale("wiringGateAttr"),
            new AttributeOption[] { GATE_TOP_LEFT, GATE_BOTTOM_RIGHT });

    private static Tool[] ADD_TOOLS = {
        new AddTool(SplitterFactory.instance),
        new AddTool(Pin.FACTORY),
        new AddTool(Probe.FACTORY),
        new AddTool(Tunnel.FACTORY),
        new AddTool(PullResistor.FACTORY),
        new AddTool(Clock.FACTORY),
        new AddTool(Constant.FACTORY),
    };

    private static FactoryDescription[] DESCRIPTIONS = {
        new FactoryDescription("Power", getFromLocale("powerComponent"),
                "power.svg", "Power"),
        new FactoryDescription("Ground", getFromLocale("groundComponent"),
                "ground.svg", "Ground"),
        new FactoryDescription("Transistor", getFromLocale("transistorComponent"),
                "trans0.svg", "Transistor"),
        new FactoryDescription("Transmission Gate", getFromLocale("transmissionGateComponent"),
                "transmis.svg", "TransmissionGate"),
        new FactoryDescription("Bit Extender", getFromLocale("extenderComponent"),
                "extender.svg", "BitExtender"),
    };

    private List<Tool> tools = null;

    public Wiring() { }

    @Override
    public String getName() { return "Wiring"; }

    @Override
    public String getDisplayName() { return getFromLocale("wiringLibrary"); }

    @Override
    public List<Tool> getTools() {
        if (tools == null) {
            List<Tool> ret = new ArrayList<Tool>(ADD_TOOLS.length + DESCRIPTIONS.length);
            for (Tool a : ADD_TOOLS) {
                ret.add(a);
            }
            ret.addAll(FactoryDescription.getTools(Wiring.class, DESCRIPTIONS));
            tools = ret;
        }
        return tools;
    }
}
