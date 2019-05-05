/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.comp;

public class TextFieldEvent {
    private TextField field;
    private String oldval;
    private String newval;

    public TextFieldEvent(TextField field, String old, String val) {
        this.field = field;
        this.oldval = old;
        this.newval = val;
    }

    public TextField getTextField() {
        return field;
    }

    public String getOldText() {
        return oldval;
    }

    public String getText() {
        return newval;
    }
}
