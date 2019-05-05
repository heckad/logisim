/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools;

public interface CaretListener {
    public void editingCanceled(CaretEvent e);
    public void editingStopped(CaretEvent e);
}
