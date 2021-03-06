/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.util.ZOrder;
import static com.heckad.logisim.util.LocaleString.*;

public class ModelRemoveAction extends ModelAction {
    private Map<CanvasObject, Integer> removed;

    public ModelRemoveAction(CanvasModel model, CanvasObject removed) {
        this(model, Collections.singleton(removed));
    }

    public ModelRemoveAction(CanvasModel model, Collection<CanvasObject> removed) {
        super(model);
        this.removed = ZOrder.getZIndex(removed, model);
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        return Collections.unmodifiableSet(removed.keySet());
    }

    @Override
    public String getName() {
        return getFromLocale("actionRemove", getShapesName(removed.keySet()));
    }

    @Override
    void doSub(CanvasModel model) {
        model.removeObjects(removed.keySet());
    }

    @Override
    void undoSub(CanvasModel model) {
        model.addObjects(removed);
    }
}
