/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.logisim.gui.prefs;

import com.heckad.logisim.data.Direction;
import com.heckad.logisim.prefs.AppPreferences;
import com.heckad.logisim.util.TableLayout;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import static com.heckad.logisim.util.LocaleString.getFromLocale;

@SuppressWarnings("serial")
class WindowOptions extends OptionsPanel {
    private PrefBoolean[]  checks;
    private PrefOptionList toolbarPlacement;

    public WindowOptions(PreferencesFrame window) {
        super(window);

        checks = new PrefBoolean[]{
                new PrefBoolean(AppPreferences.SHOW_TICK_RATE,
                                getFromLocale("windowTickRate")),
        };

        toolbarPlacement = new PrefOptionList(
                AppPreferences.TOOLBAR_PLACEMENT,
                getFromLocale("windowToolbarLocation"),
                new PrefOption[]{
                        new PrefOption(Direction.NORTH.toString(),
                                       Direction.NORTH.getDisplayGetter()),
                        new PrefOption(Direction.SOUTH.toString(),
                                       Direction.SOUTH.getDisplayGetter()),
                        new PrefOption(Direction.EAST.toString(),
                                       Direction.EAST.getDisplayGetter()),
                        new PrefOption(Direction.WEST.toString(),
                                       Direction.WEST.getDisplayGetter()),
                        new PrefOption(AppPreferences.TOOLBAR_DOWN_MIDDLE,
                                       getFromLocale("windowToolbarDownMiddle")),
                        new PrefOption(AppPreferences.TOOLBAR_HIDDEN,
                                       getFromLocale("windowToolbarHidden"))});

        JPanel panel = new JPanel(new TableLayout(2));
        panel.add(toolbarPlacement.getJLabel());
        panel.add(toolbarPlacement.getJComboBox());

        setLayout(new TableLayout(1));

        for (PrefBoolean check : checks) {
            add(check);
        }

        add(panel);

        //new PrefOption(MotifLookAndFeel.class.getName(), getFromLocale("motifLookAndFeel")),
        PrefOptionList lookAndFeel = new PrefOptionList(
                AppPreferences.LOOK_AND_FEEL,
                getFromLocale("lookAndFeel"),
                new PrefOption[]{
                        new PrefOption(UIManager.getSystemLookAndFeelClassName(), getFromLocale("systemLookAndFeel")),
                        new PrefOption(NimbusLookAndFeel.class.getName(), getFromLocale("nimbusLookAndFeel")),
                        // new PrefOption(MotifLookAndFeel.class.getName(), getFromLocale("motifLookAndFeel")),
                        new PrefOption(MetalLookAndFeel.class.getName(), getFromLocale("metalLookAndFeel")),
                });
        panel.add(lookAndFeel.getJLabel());
        panel.add(lookAndFeel.getJComboBox());
    }

    @Override
    public String getTitle() {
        return getFromLocale("windowTitle");
    }

    @Override
    public String getHelpText() {
        return getFromLocale("windowHelp");
    }

    @Override
    public void localeChanged() {
        for (PrefBoolean check : checks) {
            check.localeChanged();
        }
        toolbarPlacement.localeChanged();
    }
}
