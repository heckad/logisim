/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.analyze.gui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
abstract class AnalyzerTab extends JPanel {
    abstract void updateTab();
    abstract void localeChanged();
}
