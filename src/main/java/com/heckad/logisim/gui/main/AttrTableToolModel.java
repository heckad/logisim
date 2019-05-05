/* Copyright (c) 2011, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.main;

import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.gui.generic.AttributeSetTableModel;
import com.heckad.logisim.proj.Project;
import com.heckad.logisim.tools.Tool;
import static com.heckad.logisim.util.LocaleString.*;

public class AttrTableToolModel extends AttributeSetTableModel {
    Project proj;
    Tool tool;

    public AttrTableToolModel(Project proj, Tool tool) {
        super(tool.getAttributeSet());
        this.proj = proj;
        this.tool = tool;
    }

    @Override
    public String getTitle() {
        return getFromLocale("toolAttrTitle", tool.getDisplayName());
    }

    public Tool getTool() {
        return tool;
    }

    @Override
    public void setValueRequested(Attribute<Object> attr, Object value) {
        proj.doAction(ToolAttributeAction.create(tool, attr, value));
    }
}
