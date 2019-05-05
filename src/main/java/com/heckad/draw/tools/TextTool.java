/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.tools;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

import com.heckad.draw.actions.ModelAddAction;
import com.heckad.draw.actions.ModelEditTextAction;
import com.heckad.draw.actions.ModelRemoveAction;
import com.heckad.draw.canvas.Canvas;
import com.heckad.draw.model.CanvasObject;
import com.heckad.draw.shapes.DrawAttr;
import com.heckad.draw.shapes.Text;
import com.heckad.draw.util.EditableLabelField;
import com.heckad.logisim.data.Attribute;
import com.heckad.logisim.data.AttributeEvent;
import com.heckad.logisim.data.AttributeListener;
import com.heckad.logisim.data.Location;
import com.heckad.logisim.util.Icons;

@SuppressWarnings("serial")
public class TextTool extends AbstractTool {
    private class FieldListener extends AbstractAction implements AttributeListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            commitText(curCanvas);
        }

        @Override
        public void attributeListChanged(AttributeEvent e) {
            Text cur = curText;
            if (cur != null) {
                double zoom = curCanvas.getZoomFactor();
                cur.getLabel().configureTextField(field, zoom);
                curCanvas.repaint();
            }
        }

        @Override
        public void attributeValueChanged(AttributeEvent e) {
            attributeListChanged(e);
        }
    }

    private class CancelListener extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelText(curCanvas);
        }
    }

    private DrawingAttributeSet attrs;
    private EditableLabelField field;
    private FieldListener fieldListener;

    private Text curText;
    private Canvas curCanvas;
    private boolean isTextNew;

    public TextTool(DrawingAttributeSet attrs) {
        this.attrs = attrs;
        curText = null;
        isTextNew = false;
        field = new EditableLabelField();

        fieldListener = new FieldListener();
        InputMap fieldInput = field.getInputMap();
        fieldInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "commit");
        fieldInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "cancel");
        ActionMap fieldAction = field.getActionMap();
        fieldAction.put("commit", fieldListener);
        fieldAction.put("cancel", new CancelListener());
    }

    @Override
    public Icon getIcon() {
        return Icons.getIcon("text.svg");
    }

    @Override
    public Cursor getCursor(Canvas canvas) {
        return Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
    }

    @Override
    public List<Attribute<?>> getAttributes() {
        return DrawAttr.ATTRS_TEXT_TOOL;
    }

    @Override
    public void toolSelected(Canvas canvas) {
        cancelText(canvas);
    }

    @Override
    public void toolDeselected(Canvas canvas) {
        commitText(canvas);
    }

    @Override
    public void mousePressed(Canvas canvas, MouseEvent e) {
        if (curText != null) {
            commitText(canvas);
        }

        Text clicked = null;
        boolean found = false;
        int mx = e.getX();
        int my = e.getY();
        Location mloc = Location.create(mx, my);
        for (CanvasObject o : canvas.getModel().getObjectsFromTop()) {
            if (o instanceof Text && o.contains(mloc, true)) {
                clicked = (Text) o;
                found = true;
                break;
            }
        }
        if (!found) {
            clicked = attrs.applyTo(new Text(mx, my, ""));
        }

        curText = clicked;
        curCanvas = canvas;
        isTextNew = !found;
        clicked.getLabel().configureTextField(field, canvas.getZoomFactor());
        field.setText(clicked.getText());
        canvas.add(field);

        Point fieldLoc = field.getLocation();
        double zoom = canvas.getZoomFactor();
        fieldLoc.x = (int) Math.round(mx * zoom - fieldLoc.x);
        fieldLoc.y = (int) Math.round(my * zoom - fieldLoc.y);
        int caret = field.viewToModel(fieldLoc);
        if (caret >= 0) {
            field.setCaretPosition(caret);
        }
        field.requestFocus();

        canvas.getSelection().setSelected(clicked, true);
        canvas.getSelection().setHidden(Collections.singleton(clicked), true);
        clicked.addAttributeListener(fieldListener);
        canvas.repaint();
    }

    @Override
    public void zoomFactorChanged(Canvas canvas) {
        Text t = curText;
        if (t != null) {
            t.getLabel().configureTextField(field, canvas.getZoomFactor());
        }
    }

    @Override
    public void draw(Canvas canvas, Graphics g) {
        // actually, there's nothing to do here - it's handled by the field
        ;
    }

    private void cancelText(Canvas canvas) {
        Text cur = curText;
        if (cur != null) {
            curText = null;
            cur.removeAttributeListener(fieldListener);
            canvas.remove(field);
            canvas.getSelection().clearSelected();
            canvas.repaint();
        }
    }

    private void commitText(Canvas canvas) {
        Text cur = curText;
        boolean isNew = isTextNew;
        String newText = field.getText();
        if (cur == null) {
            return;
        }
        cancelText(canvas);

        if (isNew) {
            if (!newText.equals("")) {
                cur.setText(newText);
                canvas.doAction(new ModelAddAction(canvas.getModel(), cur));
            }
        } else {
            String oldText = cur.getText();
            if (newText.equals("")) {
                canvas.doAction(new ModelRemoveAction(canvas.getModel(), cur));
            } else if (!oldText.equals(newText)) {
                canvas.doAction(new ModelEditTextAction(canvas.getModel(), cur,
                        newText));
            }
        }
    }
}
