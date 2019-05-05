/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.file;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.heckad.draw.model.AbstractCanvasObject;
import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.circuit.CircuitMutator;
import com.heckad.logisim.circuit.CircuitTransaction;
import com.heckad.logisim.circuit.Wire;
import com.heckad.logisim.comp.Component;
import com.heckad.logisim.comp.ComponentFactory;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;
import static com.heckad.logisim.util.LocaleString.*;

public class XmlCircuitReader extends CircuitTransaction {
    private XmlReader.ReadContext reader;
    private List<XmlReader.CircuitData> circuitsData;

    public XmlCircuitReader(XmlReader.ReadContext reader,
            List<XmlReader.CircuitData> circDatas) {
        this.reader = reader;
        this.circuitsData = circDatas;
    }

    @Override
    protected Map<Circuit, Integer> getAccessedCircuits() {
        HashMap<Circuit, Integer> access = new HashMap<Circuit,Integer>();
        for (XmlReader.CircuitData data : circuitsData) {
            access.put(data.circuit, READ_WRITE);
        }
        return access;
    }

    @Override
    protected void run(CircuitMutator mutator) {
        for (XmlReader.CircuitData circuitData : circuitsData) {
            buildCircuit(circuitData, mutator);
        }
    }

    private void buildCircuit(XmlReader.CircuitData circData, CircuitMutator mutator) {
        Element elt = circData.circuitElement;
        Circuit dest = circData.circuit;
        Map<Element, Component> knownComponents = circData.knownComponents;
        if (knownComponents == null) {
            knownComponents = Collections.emptyMap();
        }

        try {
            reader.initAttributeSet(circData.circuitElement, dest.getStaticAttributes(), null);
        } catch (XmlReaderException e) {
            reader.addErrors(e, circData.circuit.getName() + ".static");
        }

        for (Element sub_elt : XmlIterator.forChildElements(elt)) {
            String sub_elt_name = sub_elt.getTagName();
            if (sub_elt_name.equals("comp")) {
                try {
                    Component comp = knownComponents.get(sub_elt);
                    if (comp == null) {
                        comp = getComponent(sub_elt, reader);
                    }
                    mutator.add(dest, comp);
                } catch (XmlReaderException e) {
                    reader.addErrors(e, circData.circuit.getName() + "." + toComponentString(sub_elt));
                }
            } else if (sub_elt_name.equals("wire")) {
                try {
                    addWire(dest, mutator, sub_elt);
                } catch (XmlReaderException e) {
                    reader.addErrors(e, circData.circuit.getName() + "." + toWireString(sub_elt));
                }
            }
        }

        List<AbstractCanvasObject> appearance = circData.appearance;
        if (appearance != null && !appearance.isEmpty()) {
            dest.getAppearance().setObjectsForce(appearance);
            dest.getAppearance().setDefaultAppearance(false);
        }
    }

    private String toComponentString(Element elt) {
        String name = elt.getAttribute("name");
        String loc = elt.getAttribute("loc");
        return name + "(" + loc + ")";
    }

    private String toWireString(Element elt) {
        String from = elt.getAttribute("from");
        String to = elt.getAttribute("to");
        return "w" + from + "-" + to;
    }

    void addWire(Circuit dest, CircuitMutator mutator, Element elt)
            throws XmlReaderException {
        Location pt0;
        try {
            String str = elt.getAttribute("from");
            if (str == null || str.equals("")) {
                throw new XmlReaderException(getFromLocale("wireStartMissingError"));
            }
            pt0 = Location.parse(str);
        } catch (NumberFormatException e) {
            throw new XmlReaderException(getFromLocale("wireStartInvalidError"));
        }

        Location pt1;
        try {
            String str = elt.getAttribute("to");
            if (str == null || str.equals("")) {
                throw new XmlReaderException(getFromLocale("wireEndMissingError"));
            }
            pt1 = Location.parse(str);
        } catch (NumberFormatException e) {
            throw new XmlReaderException(getFromLocale("wireEndInvalidError"));
        }

        mutator.add(dest, Wire.create(pt0, pt1));
    }

    static Component getComponent(Element elt, XmlReader.ReadContext reader)
            throws XmlReaderException {
        // Determine the factory that creates this element
        String name = elt.getAttribute("name");
        if (name == null || name.equals("")) {
            throw new XmlReaderException(getFromLocale("compNameMissingError"));
        }

        String libName = elt.getAttribute("lib");
        Library lib = reader.findLibrary(libName);
        if (lib == null) {
            throw new XmlReaderException(getFromLocale("compUnknownError", "no-lib"));
        }

        Tool tool = lib.getTool(name);
        if (tool == null || !(tool instanceof AddTool)) {
            if (libName == null || libName.equals("")) {
                throw new XmlReaderException(getFromLocale("compUnknownError", name));
            } else {
                throw new XmlReaderException(getFromLocale("compAbsentError", name, libName));
            }
        }
        ComponentFactory source = ((AddTool) tool).getFactory();

        // Determine attributes
        String loc_str = elt.getAttribute("loc");
        AttributeSet attrs = source.createAttributeSet();
        reader.initAttributeSet(elt, attrs, source);

        // Create component if location known
        if (loc_str == null || loc_str.equals("")) {
            throw new XmlReaderException(getFromLocale("compLocMissingError", source.getName()));
        } else {
            try {
                Location loc = Location.parse(loc_str);
                return source.createComponent(loc, attrs);
            } catch (NumberFormatException e) {
                throw new XmlReaderException(getFromLocale("compLocInvalidError",
                    source.getName(), loc_str));
            }
        }
    }
}
