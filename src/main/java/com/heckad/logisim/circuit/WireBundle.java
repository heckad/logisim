/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit;

import java.util.concurrent.CopyOnWriteArraySet;

import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.data.Value;

class WireBundle {
    private BitWidth width = BitWidth.UNKNOWN;
    private Value pullValue = Value.UNKNOWN;
    private WireBundle parent;
    private Location widthDeterminant = null;
    WireThread[] threads = null;
    // points bundle hits
    CopyOnWriteArraySet<Location> points = new CopyOnWriteArraySet<Location>();
    private WidthIncompatibilityData incompatibilityData = null;

    WireBundle() {
        parent = this;
    }

    boolean isValid() {
        return incompatibilityData == null;
    }

    void setWidth(BitWidth width, Location det) {
        if (width == BitWidth.UNKNOWN) {
            return;
        }

        if (incompatibilityData != null) {
            incompatibilityData.add(det, width);
            return;
        }
        if (this.width != BitWidth.UNKNOWN) {
            if (width.equals(this.width)) {
                // the widths match, and the bundle is already set; nothing to do
                return;
            // the widths are broken: Create incompatibilityData holding this info
            } else {
                incompatibilityData = new WidthIncompatibilityData();
                incompatibilityData.add(widthDeterminant, this.width);
                incompatibilityData.add(det, width);
                return;
            }
        }
        this.width = width;
        this.widthDeterminant = det;
        this.threads = new WireThread[width.getWidth()];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WireThread();
        }
    }

    BitWidth getWidth() {
        if (incompatibilityData != null) {
            return BitWidth.UNKNOWN;
        } else {
            return width;
        }
    }

    Location getWidthDeterminant() {
        if (incompatibilityData != null) {
            return null;
        } else {
            return widthDeterminant;
        }
    }

    WidthIncompatibilityData getWidthIncompatibilityData() {
        return incompatibilityData;
    }

    void isolate() {
        parent = this;
    }

    void unite(WireBundle other) {
        WireBundle group = this.find();
        WireBundle group2 = other.find();
        if (group != group2) {
            group.parent = group2;
        }

    }

    WireBundle find() {
        WireBundle ret = this;
        if (ret.parent != ret) {
            do ret = ret.parent; while (ret.parent != ret);
            this.parent = ret;
        }
        return ret;
    }

    void addPullValue(Value val) {
        pullValue = pullValue.combine(val);
    }

    Value getPullValue() {
        return pullValue;
    }
}
