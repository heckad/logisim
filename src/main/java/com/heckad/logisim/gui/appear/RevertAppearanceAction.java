/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.appear;

import java.util.ArrayList;

import com.heckad.draw.model.CanvasObject;
import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.circuit.appear.CircuitAppearance;
import com.heckad.logisim.proj.Action;
import com.heckad.logisim.proj.Project;
import static com.heckad.logisim.util.LocaleString.*;

public class RevertAppearanceAction extends Action {
    private Circuit circuit;
    private ArrayList<CanvasObject> old;
    private boolean wasDefault;

    public RevertAppearanceAction(Circuit circuit) {
        this.circuit = circuit;
    }

    @Override
    public String getName() {
        return getFromLocale("revertAppearanceAction");
    }

    @Override
    public void doIt(Project proj) {
        CircuitAppearance appear = circuit.getAppearance();
        wasDefault = appear.isDefaultAppearance();
        old = new ArrayList<CanvasObject>(appear.getObjectsFromBottom());
        appear.setDefaultAppearance(true);
    }

    @Override
    public void undo(Project proj) {
        CircuitAppearance appear = circuit.getAppearance();
        appear.setObjectsForce(old);
        appear.setDefaultAppearance(wasDefault);
    }
}
