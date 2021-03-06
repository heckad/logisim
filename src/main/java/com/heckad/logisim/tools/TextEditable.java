/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools;

import com.heckad.logisim.circuit.Circuit;
import com.heckad.logisim.comp.ComponentUserEvent;
import com.heckad.logisim.proj.Action;

public interface TextEditable {
    public Caret getTextCaret(ComponentUserEvent event);
    public Action getCommitAction(Circuit circuit, String oldText, String newText);
}
