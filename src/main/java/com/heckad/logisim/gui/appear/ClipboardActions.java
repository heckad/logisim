/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.appear;

import java.util.ArrayList;
import java.util.Map;

import com.heckad.draw.model.CanvasModel;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.util.ZOrder;
import com.heckad.logisim.circuit.appear.AppearanceAnchor;
import com.heckad.logisim.data.Direction;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.proj.Action;
import com.heckad.logisim.proj.Project;
import static com.heckad.logisim.util.LocaleString.*;

public class ClipboardActions extends Action {

    public static Action cut(AppearanceCanvas canvas) {
        return new ClipboardActions(true, canvas);
    }

    public static Action copy(AppearanceCanvas canvas) {
        return new ClipboardActions(false, canvas);
    }

    private boolean remove;
    private AppearanceCanvas canvas;
    private CanvasModel canvasModel;
    private ClipboardContents oldClipboard;
    private Map<CanvasObject, Integer> affected;
    private ClipboardContents newClipboard;

    private ClipboardActions(boolean remove, AppearanceCanvas canvas) {
        this.remove = remove;
        this.canvas = canvas;
        this.canvasModel = canvas.getModel();

        ArrayList<CanvasObject> contents = new ArrayList<CanvasObject>();
        Direction anchorFacing = null;
        Location anchorLocation = null;
        ArrayList<CanvasObject> aff = new ArrayList<CanvasObject>();
        for (CanvasObject o : canvas.getSelection().getSelected()) {
            if (o.canRemove()) {
                aff.add(o);
                contents.add(o.clone());
            } else if (o instanceof AppearanceAnchor) {
                AppearanceAnchor anch = (AppearanceAnchor) o;
                anchorFacing = anch.getFacing();
                anchorLocation = anch.getLocation();
            }
        }
        contents.trimToSize();
        affected = ZOrder.getZIndex(aff, canvasModel);
        newClipboard = new ClipboardContents(contents, anchorLocation, anchorFacing);
    }

    @Override
    public String getName() {
        if (remove) {
            return getFromLocale("cutSelectionAction");
        } else {
            return getFromLocale("copySelectionAction");
        }
    }

    @Override
    public void doIt(Project proj) {
        oldClipboard = Clipboard.get();
        Clipboard.set(newClipboard);
        if (remove) {
            canvasModel.removeObjects(affected.keySet());
        }
    }

    @Override
    public void undo(Project proj) {
        if (remove) {
            canvasModel.addObjects(affected);
            canvas.getSelection().clearSelected();
            canvas.getSelection().setSelected(affected.keySet(), true);
        }
        Clipboard.set(oldClipboard);
    }

}
