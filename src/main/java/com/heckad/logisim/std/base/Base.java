/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.std.base;

import java.util.Arrays;
import java.util.List;

import com.heckad.logisim.tools.Library;
import com.heckad.logisim.tools.MenuTool;
import com.heckad.logisim.tools.PokeTool;
import com.heckad.logisim.tools.SelectTool;
import com.heckad.logisim.tools.TextTool;
import com.heckad.logisim.tools.AddTool;
import com.heckad.logisim.tools.EditTool;
import com.heckad.logisim.tools.Tool;
import com.heckad.logisim.tools.WiringTool;
import static com.heckad.logisim.util.LocaleString.*;

public class Base extends Library {
    private List<Tool> tools = null;

    public Base() {
        SelectTool select = new SelectTool();
        WiringTool wiring = new WiringTool();

        tools = Arrays.asList(new Tool[] {
            new PokeTool(),
            new EditTool(select, wiring),
            select,
            wiring,
            new TextTool(),
            new MenuTool(),
            new AddTool(Text.FACTORY),
        });
    }

    @Override
    public String getName() { return "Base"; }

    @Override
    public String getDisplayName() { return getFromLocale("baseLibrary"); }

    @Override
    public List<Tool> getTools() {
        return tools;
    }
}
