/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.comp;

import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.gui.main.Canvas;


public class ComponentUserEvent {
    private Canvas canvas;
    private int x = 0;
    private int y = 0;

    ComponentUserEvent(Canvas canvas) {
        this.canvas = canvas;
    }

    public ComponentUserEvent(Canvas canvas, int x, int y) {
        this.canvas = canvas;
        this.x = x;
        this.y = y;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public CircuitState getCircuitState() {
        return canvas.getCircuitState();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
