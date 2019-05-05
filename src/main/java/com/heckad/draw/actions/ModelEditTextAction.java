/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.actions;

import java.util.Collection;
import java.util.Collections;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.shapes.Text;
import static com.heckad.logisim.util.LocaleString.*;

public class ModelEditTextAction extends ModelAction {
    private Text text;
    private String oldValue;
    private String newValue;

    public ModelEditTextAction(CanvasModel model, Text text, String newValue) {
        super(model);
        this.text = text;
        this.oldValue = text.getText();
        this.newValue = newValue;
    }

    @Override
    public Collection<CanvasObject> getObjects() {
        return Collections.singleton((CanvasObject) text);
    }

    @Override
    public String getName() {
        return getFromLocale("actionEditText");
    }

    @Override
    void doSub(CanvasModel model) {
        model.setText(text, newValue);
    }

    @Override
    void undoSub(CanvasModel model) {
        model.setText(text, oldValue);
    }
}
