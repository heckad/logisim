/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.appear;

import com.heckad.draw.canvas.Canvas;
import com.heckad.draw.gui.AttrTableDrawManager;
import com.heckad.draw.toolbar.ToolbarModel;
import com.heckad.draw.tools.DrawingAttributeSet;
import com.heckad.draw.tools.SelectTool;
import com.heckad.logisim.circuit.CircuitState;
import com.heckad.logisim.data.AttributeSet;
import com.heckad.logisim.gui.generic.AttrTable;
import com.heckad.logisim.gui.generic.BasicZoomModel;
import com.heckad.logisim.gui.generic.CanvasPane;
import com.heckad.logisim.gui.generic.ZoomModel;
import com.heckad.logisim.gui.main.EditHandler;
import com.heckad.logisim.prefs.AppPreferences;
import com.heckad.logisim.proj.Project;

public class AppearanceView {
    private static final double[] ZOOM_OPTIONS = { 100, 150, 200, 300, 400, 600, 800 };

    private DrawingAttributeSet attrs;
    private AppearanceCanvas canvas;
    private CanvasPane canvasPane;
    private AppearanceToolbarModel toolbarModel;
    private AttrTableDrawManager attrTableManager;
    private ZoomModel zoomModel;
    private AppearanceEditHandler editHandler;

    public AppearanceView() {
        attrs = new DrawingAttributeSet();
        SelectTool selectTool = new SelectTool();
        canvas = new AppearanceCanvas(selectTool);
        toolbarModel = new AppearanceToolbarModel(selectTool, canvas, attrs);
        zoomModel = new BasicZoomModel(AppPreferences.APPEARANCE_SHOW_GRID,
                AppPreferences.APPEARANCE_ZOOM, ZOOM_OPTIONS);
        canvas.getGridPainter().setZoomModel(zoomModel);
        attrTableManager = null;
        canvasPane = new CanvasPane(canvas);
        canvasPane.setZoomModel(zoomModel);
        editHandler = new AppearanceEditHandler(canvas);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public CanvasPane getCanvasPane() {
        return canvasPane;
    }

    public ToolbarModel getToolbarModel() {
        return toolbarModel;
    }

    public ZoomModel getZoomModel() {
        return zoomModel;
    }

    public EditHandler getEditHandler() {
        return editHandler;
    }

    public AttributeSet getAttributeSet() {
        return attrs;
    }

    public AttrTableDrawManager getAttrTableDrawManager(AttrTable table) {
        AttrTableDrawManager ret = attrTableManager;
        if (ret == null) {
            ret = new AttrTableDrawManager(canvas, table, attrs);
            attrTableManager = ret;
        }
        return ret;
    }

    public void setCircuit(Project proj, CircuitState circuitState) {
        canvas.setCircuit(proj, circuitState);
    }
}
