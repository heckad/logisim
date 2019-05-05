package com.heckad.logisim.util;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.heckad.logisim.gui.generic.ZoomControl;
import com.heckad.logisim.gui.menu.MenuSimulate;

@SuppressWarnings("serial")
public class CustomAction extends AbstractAction {
	private String cmd;
	private Object obj;

    public CustomAction(String cmd, Object obj) {
        this.cmd = cmd;
        this.obj = obj;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner().getClass() == com.heckad.logisim.gui.main.Canvas.class) {
	        if (cmd.equalsIgnoreCase("CTRL+")) {
	            ((ZoomControl) obj).zoomIn();
	        } else if (cmd.equalsIgnoreCase("CTRL-")) {
	        	((ZoomControl) obj).zoomOut();
	        } else if (cmd.equalsIgnoreCase("Space")) {
	        	((MenuSimulate) obj).tick();
	        }
    	}
    }
}
