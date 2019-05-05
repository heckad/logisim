/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.start;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;

import com.heckad.logisim.LogisimMain;

public class About {
	static final int IMAGE_WIDTH = 200;
	static final int IMAGE_HEIGHT = 200;
	static final Dimension DIMENSION = new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT);
	protected static JSVGCanvas svgCanvas =new JSVGCanvas();
	
	public static JComponent createComponents() {
		final JPanel panel = new JPanel();
		panel.add(svgCanvas);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		panel.setBackground(Color.WHITE);
		svgCanvas.setURI(About.class.getResource("/logisim/drawing.svg").toString());
		svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {});
		svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {});
		svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {});
		return panel;
	}

	public static void showAboutDialog(JFrame owner) {
		JOptionPane.showMessageDialog(owner, createComponents(),
									  "Logisim " + LogisimMain.VERSION_NAME, JOptionPane.PLAIN_MESSAGE);
	}
}