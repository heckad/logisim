/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.generic;

import java.awt.Frame;

import javax.swing.JDialog;

@SuppressWarnings("serial")
public class LDialog extends JDialog {
    public LDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    private void init() {
        LFrame.attachIcon(this);
    }
}
