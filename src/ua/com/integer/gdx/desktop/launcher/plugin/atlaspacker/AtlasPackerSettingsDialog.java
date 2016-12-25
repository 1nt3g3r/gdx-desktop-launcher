package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JDialog;

import ua.com.integer.gdx.desktop.launcher.Settings;
import ua.com.integer.gdx.desktop.launcher.Util;

public class AtlasPackerSettingsDialog extends JDialog {
    private JCheckBox checkChangedAtlases;

    public AtlasPackerSettingsDialog(AtlasPackerPlugin plugin) {
        setSize(350, 100);
        Util.situateOnCenter(this);
        setTitle("Atlas Packer Plugin Settings");
        setModal(true);

        setLayout(new BorderLayout());

        checkChangedAtlases = new JCheckBox("Check for changed atlases on startup");
        checkChangedAtlases.setSelected(sets().getBoolean("autocheck", true));
        checkChangedAtlases.addActionListener(new CheckChangedAtlasesListener());
        add(checkChangedAtlases);
    }

    class CheckChangedAtlasesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           sets().putBoolean("autocheck", checkChangedAtlases.isSelected());
        }
    }

    private Settings sets() {
        return Settings.getInstance().setSettingsClass(AtlasPackerPlugin.class);
    }
}
