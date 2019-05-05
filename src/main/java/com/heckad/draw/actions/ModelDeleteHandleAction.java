/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.Collections;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.model.Handle;
import static com.heckad.logisim.util.LocaleString.*;

public class ModelDeleteHandleAction extends ModelAction {
    private Handle handle;
    private Handle previous;

    public ModelDeleteHandleAction(CanvasModel model, Handle handle) {
        super(model);
        this.handle = handle;
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        return Collections.singleton(handle.getObject());
    }

    @Override
    public String getName() {
        return getFromLocale("actionDeleteHandle");
    }

    @Override
    void doSub(CanvasModel model) {
        previous = model.deleteHandle(handle);
    }

    @Override
    void undoSub(CanvasModel model) {
        model.insertHandle(handle, previous);
    }
}
