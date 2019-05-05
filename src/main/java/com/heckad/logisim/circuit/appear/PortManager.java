/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.circuit.appear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.heckad.draw.model.CanvasObject;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.data.Direction;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.instance.Instance;
import com.heckad.logisim.instance.StdAttr;

class PortManager {
    private CircuitAppearance appearance;
    private boolean doingUpdate;

    PortManager(CircuitAppearance appearance) {
        this.appearance = appearance;
        this.doingUpdate = false;
    }

    void updatePorts() {
        appearance.recomputePorts();
    }

    void updatePorts(Set<Instance> adds, Set<Instance> removes,
            Map<Instance, Instance> replaces, Collection<Instance> allPins) {
        if (appearance.isDefaultAppearance()) {
            appearance.recomputePorts();
        } else if (!doingUpdate) {
            // "doingUpdate" ensures infinite recursion doesn't happen
            try {
                doingUpdate = true;
                performUpdate(adds, removes, replaces, allPins);
                appearance.recomputePorts();
            } finally {
                doingUpdate = false;
            }
        }
    }

    private void performUpdate(Set<Instance> adds, Set<Instance> removes,
            Map<Instance, Instance> replaces, Collection<Instance> allPins) {
        // Find the current objects corresponding to pins
        Map<Instance, AppearancePort> oldObjects;
        oldObjects = new HashMap<Instance, AppearancePort>();
        AppearanceAnchor anchor = null;
        for (CanvasObject o : appearance.getObjectsFromBottom()) {
            if (o instanceof AppearancePort) {
                AppearancePort port = (AppearancePort) o;
                oldObjects.put(port.getPin(), port);
            } else if (o instanceof AppearanceAnchor) {
                anchor = (AppearanceAnchor) o;
            }
        }

        // ensure we have the anchor in the circuit
        if (anchor == null) {
            for (CanvasObject o : DefaultAppearance.build(allPins)) {
                if (o instanceof AppearanceAnchor) {
                    anchor = (AppearanceAnchor) o;
                }
            }
            if (anchor == null) {
                anchor = new AppearanceAnchor(Location.create(100, 100));
            }
            int dest = appearance.getObjectsFromBottom().size();
            appearance.addObjects(dest, Collections.singleton(anchor));
        }

        // Compute how the ports should change
        ArrayList<AppearancePort> portRemoves;
        portRemoves = new ArrayList<AppearancePort>(removes.size());
        ArrayList<AppearancePort> portAdds;
        portAdds = new ArrayList<AppearancePort>(adds.size());

        // handle removals
        for (Instance pin : removes) {
            AppearancePort port = oldObjects.remove(pin);
            if (port != null) {
                portRemoves.add(port);
            }
        }
        // handle replacements
        ArrayList<Instance> addsCopy = new ArrayList<Instance>(adds);
        for (Map.Entry<Instance, Instance> entry : replaces.entrySet()) {
            AppearancePort port = oldObjects.remove(entry.getKey());
            if (port != null) {
                port.setPin(entry.getValue());
                oldObjects.put(entry.getValue(), port);
            // this really shouldn't happen, but just to make sure...
            } else {
                addsCopy.add(entry.getValue());
            }
        }
        // handle additions
        DefaultAppearance.sortPinList(addsCopy, Direction.EAST);
            // They're probably not really all facing east.
            // I'm just sorting them so it works predictably.
        for (Instance pin : addsCopy) {
            if (!oldObjects.containsKey(pin)) {
                Location loc = computeDefaultLocation(appearance, pin, oldObjects);
                AppearancePort o = new AppearancePort(loc, pin);
                portAdds.add(o);
                oldObjects.put(pin, o);
            }
        }

        // Now update the appearance
        appearance.replaceAutomatically(portRemoves, portAdds);
    }

    private static Location computeDefaultLocation(CircuitAppearance appear,
            Instance pin, Map<Instance, AppearancePort> others) {
        // Determine which locations are being used in canvas, and look for
        // which instances facing the same way in layout
        Set<Location> usedLocs = new HashSet<Location>();
        List<Instance> sameWay = new ArrayList<Instance>();
        Direction facing = pin.getAttributeValue(StdAttr.FACING);
        for (Map.Entry<Instance, AppearancePort> entry : others.entrySet()) {
            Instance pin2 = entry.getKey();
            Location loc = entry.getValue().getLocation();
            usedLocs.add(loc);
            if (pin2.getAttributeValue(StdAttr.FACING) == facing) {
                sameWay.add(pin2);
            }
        }

        // if at least one faces the same way, place pin relative to that
        if (sameWay.size() > 0) {
            sameWay.add(pin);
            DefaultAppearance.sortPinList(sameWay, facing);
            boolean isFirst = false;
            // (preferably previous in map)
            Instance neighbor = null;
            for (Instance p : sameWay) {
                if (p == pin) {
                    break;
                } else {
                    neighbor = p;
                }
            }
            // pin must have been first in list
            if (neighbor == null) {
                neighbor = sameWay.get(1);
            }
            int dx;
            int dy;
            if (facing == Direction.EAST || facing == Direction.WEST) {
                dx = 0;
                dy = isFirst? -10 : 10;
            } else {
                dx = isFirst ? -10 : 10;
                dy = 0;
            }
            Location loc = others.get(neighbor).getLocation();
            do {
                loc = loc.translate(dx, dy);
            } while (usedLocs.contains(loc));
            if (loc.getX() >= 0 && loc.getY() >= 0) {
                return loc;
            }
            do {
                loc = loc.translate(-dx, -dy);
            } while (usedLocs.contains(loc));
            return loc;
        }

        // otherwise place it on the boundary of the bounding rectangle
        Bounds bds = appear.getAbsoluteBounds();
        int x;
        int y;
        int dx = 0;
        int dy = 0;
        // on west side by default
        if (facing == Direction.EAST) {
            x = bds.getX() - 7;
            y = bds.getY() + 5;
            dy = 10;
        // on east side by default
        } else if (facing == Direction.WEST) {
            x = bds.getX() + bds.getWidth() - 3;
            y = bds.getY() + 5;
            dy = 10;
        // on north side by default
        } else if (facing == Direction.SOUTH) {
            x = bds.getX() + 5;
            y = bds.getY() - 7;
            dx = 10;
        // on south side by default
        } else {
            x = bds.getX() + 5;
            y = bds.getY() + bds.getHeight() - 3;
            dx = 10;
        }
        // round coordinates up to ensure they're on grid
        x = (x + 9) / 10 * 10;
        y = (y + 9) / 10 * 10;
        Location loc = Location.create(x, y);
        while (usedLocs.contains(loc)) {
            loc = loc.translate(dx, dy);
        }
        return loc;
    }
}
