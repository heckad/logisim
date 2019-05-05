/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.Collections;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.model.Handle;
import static com.heckad.logisim.util.LocaleString.*;

public class ModelInsertHandleAction extends ModelAction {
    private Handle desired;

    public ModelInsertHandleAction(CanvasModel model, Handle desired) {
        super(model);
        this.desired = desired;
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        return Collections.singleton(desired.getObject());
    }

    @Override
    public String getName() {
        return getFromLocale("actionInsertHandle");
    }

    @Override
    void doSub(CanvasModel model) {
        model.insertHandle(desired, null);
    }

    @Override
    void undoSub(CanvasModel model) {
        model.deleteHandle(desired);
    }
}
