/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.plexers;

import java.awt.Graphics;
import java.util.List;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeOption;
import com.heckad.logisim.data.Attributes;
import com.heckad.logisim.data.BitWidth;
import com.heckad.logisim.data.Bounds;
import com.heckad.logisim.data.Direction;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.tools.FactoryDescription;
import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.Tool;
import com.heckad.logisim.util.GraphicsUtil;
import static com.heckad.logisim.util.LocaleString.*;

public class Plexers extends Library {
    public static final Attribute<BitWidth> ATTR_SELECT
        = Attributes.forBitWidth("select", getFromLocale("plexerSelectBitsAttr"), 1, 5);
    public static final Object DEFAULT_SELECT = BitWidth.create(1);

    public static final Attribute<Boolean> ATTR_TRISTATE
        = Attributes.forBoolean("tristate", getFromLocale("plexerThreeStateAttr"));
    public static final Object DEFAULT_TRISTATE = Boolean.FALSE;

    public static final AttributeOption DISABLED_FLOATING
        = new AttributeOption("Z", getFromLocale("plexerDisabledFloating"));
    public static final AttributeOption DISABLED_ZERO
        = new AttributeOption("0", getFromLocale("plexerDisabledZero"));
    public static final Attribute<AttributeOption> ATTR_DISABLED
        = Attributes.forOption("disabled", getFromLocale("plexerDisabledAttr"),
                new AttributeOption[] { DISABLED_FLOATING, DISABLED_ZERO });

    public static final Attribute<Boolean> ATTR_ENABLE
        = Attributes.forBoolean("enable", getFromLocale("plexerEnableAttr"));

    static final AttributeOption SELECT_BOTTOM_LEFT
        = new AttributeOption("bl", getFromLocale("plexerSelectBottomLeftOption"));
    static final AttributeOption SELECT_TOP_RIGHT
        = new AttributeOption("tr", getFromLocale("plexerSelectTopRightOption"));
    static final Attribute<AttributeOption> ATTR_SELECT_LOC = Attributes.forOption("selloc",
            getFromLocale("plexerSelectLocAttr"),
            new AttributeOption[] { SELECT_BOTTOM_LEFT, SELECT_TOP_RIGHT });

    protected static final int DELAY = 3;

    private static FactoryDescription[] DESCRIPTIONS = {
        new FactoryDescription("Multiplexer", getFromLocale("multiplexerComponent"),
                "multiplexer.svg", "Multiplexer"),
        new FactoryDescription("Demultiplexer", getFromLocale("demultiplexerComponent"),
                "demultiplexer.svg", "Demultiplexer"),
        new FactoryDescription("Decoder", getFromLocale("decoderComponent"),
                "decoder.svg", "Decoder"),
        new FactoryDescription("Priority Encoder", getFromLocale("priorityEncoderComponent"),
                "priencod.svg", "PriorityEncoder"),
        new FactoryDescription("BitSelector", getFromLocale("bitSelectorComponent"),
                "bitSelector.svg", "BitSelector"),
    };

    private List<Tool> tools = null;

    public Plexers() { }

    @Override
    public String getName() { return "Plexers"; }

    @Override
    public String getDisplayName() { return getFromLocale("plexerLibrary"); }

    @Override
    public List<Tool> getTools() {
        if (tools == null) {
            tools = FactoryDescription.getTools(Plexers.class, DESCRIPTIONS);
        }
        return tools;
    }

    static void drawTrapezoid(Graphics g, Bounds bds, Direction facing,
            int facingLean) {
        int wid = bds.getWidth();
        int ht = bds.getHeight();
        int x0 = bds.getX(); int x1 = x0 + wid;
        int y0 = bds.getY(); int y1 = y0 + ht;
        int[] xp = { x0, x1, x1, x0 };
        int[] yp = { y0, y0, y1, y1 };
        if (facing == Direction.WEST) {
            yp[0] += facingLean; yp[3] -= facingLean;
        } else if (facing == Direction.NORTH) {
            xp[0] += facingLean; xp[1] -= facingLean;
        } else if (facing == Direction.SOUTH) {
            xp[2] -= facingLean; xp[3] += facingLean;
        } else {
            yp[1] += facingLean; yp[2] -= facingLean;
        }
        GraphicsUtil.switchToWidth(g, 2);
        g.drawPolygon(xp, yp, 4);
    }

    static boolean contains(Location loc, Bounds bds, Direction facing) {
        if (bds.contains(loc, 1)) {
            int x = loc.getX();
            int y = loc.getY();
            int x0 = bds.getX();
            int x1 = x0 + bds.getWidth();
            int y0 = bds.getY();
            int y1 = y0 + bds.getHeight();
            if (facing == Direction.NORTH || facing == Direction.SOUTH) {
                if (x < x0 + 5 || x > x1 - 5) {
                    if (facing == Direction.SOUTH) {
                        return y < y0 + 5;
                    } else {
                        return y > y1 - 5;
                    }
                } else {
                    return true;
                }
            } else {
                if (y < y0 + 5 || y > y1 - 5) {
                    if (facing == Direction.EAST) {
                        return x < x0 + 5;
                    } else {
                        return x > x1 - 5;
                    }
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
