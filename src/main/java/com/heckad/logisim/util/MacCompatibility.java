/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.util;

import java.io.File;
import java.io.IOException;

import javax.swing.JMenuBar;

import net.roydesign.mac.MRJAdapter;

public class MacCompatibility {
    private MacCompatibility() { }

    public static final double mrjVersion;

    static {
        double versionValue;
        try {
            versionValue = MRJAdapter.mrjVersion;
        } catch (Exception t) {
            versionValue = 0.0;
        }
        mrjVersion = versionValue;
    }

    public static boolean isAboutAutomaticallyPresent() {
        try {
            return MRJAdapter.isAboutAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    public static boolean isPreferencesAutomaticallyPresent() {
        try {
            return MRJAdapter.isPreferencesAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    public static boolean isQuitAutomaticallyPresent() {
        try {
            return MRJAdapter.isQuitAutomaticallyPresent();
        } catch (Exception t) {
            return false;
        }
    }

    public static boolean isSwingUsingScreenMenuBar() {
        try {
            return MRJAdapter.isSwingUsingScreenMenuBar();
        } catch (Exception t) {
            return false;
        }
    }

    public static void setFramelessJMenuBar(JMenuBar menubar) {
        try {
            MRJAdapter.setFramelessJMenuBar(menubar);
        } catch (Exception t) { }
    }

    public static void setFileCreatorAndType(File dest, String app, String type)
            throws IOException {
        IOException ioExcept = null;
        try {
            try {
                MRJAdapter.setFileCreatorAndType(dest, app, type);
            } catch (IOException e) {
                ioExcept = e;
            }
        } catch (Exception t) { }
        if (ioExcept != null) {
            throw ioExcept;
        }

    }

}
