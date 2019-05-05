/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.analyze.gui;

import javax.swing.JFrame;

import com.heckad.logisim.util.LocaleListener;
import com.heckad.logisim.util.LocaleManager;
import com.heckad.logisim.util.WindowMenuItemManager;
import static com.heckad.logisim.util.LocaleString.getFromLocale;

public class AnalyzerManager extends WindowMenuItemManager
        implements LocaleListener {
    public static void initialize() {
        analysisManager = new AnalyzerManager();
    }

    public static Analyzer getAnalyzer() {
        if (analysisWindow == null) {
            analysisWindow = new Analyzer();
            analysisWindow.pack();
            if (analysisManager != null) {
                analysisManager.frameOpened(analysisWindow);
            }

        }
        return analysisWindow;
    }

    private static Analyzer analysisWindow = null;
    private static AnalyzerManager analysisManager = null;

    private AnalyzerManager() {
        super(getFromLocale("analyzerWindowTitle"), true);
        LocaleManager.addLocaleListener(this);
    }

    @Override
    public JFrame getJFrame(boolean create) {
        if (create) {
            return getAnalyzer();
        } else {
            return analysisWindow;
        }
    }

    @Override
    public void localeChanged() {
        setText(getFromLocale("analyzerWindowTitle"));
    }
}
