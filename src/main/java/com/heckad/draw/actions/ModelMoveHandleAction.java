/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.Collections;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.model.Handle;
import com.heckad.draw.model.HandleGesture;
import static com.heckad.logisim.util.LocaleString.*;

public class ModelMoveHandleAction extends ModelAction {
    private HandleGesture gesture;
    private Handle newHandle;

    public ModelMoveHandleAction(CanvasModel model, HandleGesture gesture) {
        super(model);
        this.gesture = gesture;
    }

    public Handle getNewHandle() {
        return newHandle;
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        return Collections.singleton(gesture.getHandle().getObject());
    }

    @Override
    public String getName() {
        return getFromLocale("actionMoveHandle");
    }

    @Override
    void doSub(CanvasModel model) {
        newHandle = model.moveHandle(gesture);
    }

    @Override
    void undoSub(CanvasModel model) {
        Handle oldHandle = gesture.getHandle();
        int dx = oldHandle.getX() - newHandle.getX();
        int dy = oldHandle.getY() - newHandle.getY();
        HandleGesture reverse = new HandleGesture(newHandle, dx, dy, 0);
        model.moveHandle(reverse);
    }
}
