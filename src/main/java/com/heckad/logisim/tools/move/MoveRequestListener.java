/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.tools.move;

public interface MoveRequestListener {
    public void requestSatisfied(MoveGesture gesture, int dx, int dy);
}
