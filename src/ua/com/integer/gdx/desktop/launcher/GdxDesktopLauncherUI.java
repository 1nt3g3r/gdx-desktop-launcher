/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package ua.com.integer.gdx.desktop.launcher;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.badlogic.gdx.Gdx;

import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

/**
 * Simple UI for GdxDesktopLauncher. You can setup resolution here, apply some plugins, and launch game
 * 
 * @author 1nt3g3r
 */
public class GdxDesktopLauncherUI extends JDialog {
	private static final long serialVersionUID = -8044534089121377469L;
	private static java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenCenterX = screenSize.width / 2;
    private static int screenCenterY = screenSize.height / 2;

    private GdxDesktopLauncher launcher;
    private boolean launched;

    private JComboBox<String> resolutionCombobox, scaleCombobox;
    private JCheckBox portraitMode;

    public GdxDesktopLauncherUI(GdxDesktopLauncher launcher) {
        this.launcher = launcher;

        setTitle(launcher.getConfig().title + " Launcher");
        setSize(400, 200);
        setResizable(false);
        situateOnCenter(this);
        setVisible(true);
        addWindowListener(new ExitOnCloseListener());

        setLayout(null);
        addResolutionPanel();
        addLaunchButton();
        addPlugins();
    }

    public static void situateOnCenter(JDialog dialog) {
        int frameWidth = dialog.getSize().width;
        int frameHeight = dialog.getSize().height;

        int frameX = screenCenterX - frameWidth / 2;
        int frameY = screenCenterY - frameHeight / 2;

        dialog.setLocation(frameX, frameY);
    }

    private void addResolutionPanel() {
        JLabel resolutionLabel = new JLabel("Resolution:");
        resolutionLabel.setBounds(10, 25, 100, 25);
        add(resolutionLabel);

        resolutionCombobox = new JComboBox<String>(new String[] {"2048x1536", "1280x800", "1024x768", "1024x600", "1136x640", "960x540", "800x480", "640x480" });
        resolutionCombobox.setSelectedIndex(6);
        resolutionCombobox.setBounds(105, 25, 100, 25);
        add(resolutionCombobox);

        JLabel scaleLabel = new JLabel("Scale:");
        scaleLabel.setBounds(215, 25, 50, 25);
        add(scaleLabel);

        scaleCombobox = new JComboBox<String>(new String[] {"2", "1.5", "1", "0.9", "0.8", "0.7", "0.6", "0.5"});
        scaleCombobox.setSelectedIndex(2);
        scaleCombobox.setBounds(270, 25, 100, 25);
        add(scaleCombobox);

        portraitMode = new JCheckBox("Portrait orientation");
        portraitMode.setBounds(5, 55, 300, 25);
        add(portraitMode);

        loadSettings();
    }

    private void loadSettings() {
        Settings sets = Settings.getInstance().setSettingsClass(getClass());
        
        resolutionCombobox.setSelectedItem(sets.getString("resolution", "800x480"));
        scaleCombobox.setSelectedItem(sets.getString("scale", "1.0"));
        portraitMode.setSelected(sets.getBoolean("portrait-orientation", false));
    }

    private void addLaunchButton() {
        JButton launchButton = new JButton("Launch");
        launchButton.addActionListener(new LaunchListener());
        launchButton.setBounds(290, 120, 100, 25);
        add(launchButton);
    }

    private void addPlugins() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        if (launcher.getPlugins().size > 0) {
            JMenu menu = new JMenu("Plugins");
            menuBar.add(menu);

            for(int i = 0; i < launcher.getPlugins().size; i++) {
                if (launcher.getPlugins().get(i).getCommands() != null && launcher.getPlugins().get(i).getCommands().length > 0) {
                    menu.add(createPluginMenu(launcher.getPlugins().get(i)));
                }
            }
        }
    }

    private JMenu createPluginMenu(GdxDesktopLauncherPlugin plugin) {
        JMenu result = new JMenu(plugin.getName());
        for(String command : plugin.getCommands()) {
            JMenuItem button = new JMenuItem(command);
            button.addActionListener(new PluginCommandListener(plugin, command));
            result.add(button);
        }
        return result;
    }

    class ExitOnCloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            if (Gdx.app != null) {
                Gdx.app.exit();
            } else {
                notifyPluginsAboutClose();

                saveSettings();

                System.exit(0);
            }
        }

        private void saveSettings() {
            Settings sets = Settings.getInstance().setSettingsClass(getClass());
            sets.putString("resolution", resolutionCombobox.getSelectedItem().toString());
            sets.putString("scale", scaleCombobox.getSelectedItem().toString());
            sets.putBoolean("portrait-orientation", portraitMode.isSelected());
        }
    }

    private void notifyPluginsAboutClose() {
        for(int i = 0; i < launcher.getPlugins().size; i++) {
            launcher.getPlugins().get(i).onExit();
        }
    }

    class LaunchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!launched) {
                launched = true;

                String[] resolutionParts = resolutionCombobox.getSelectedItem().toString().split("x");
                int width = Integer.parseInt(resolutionParts[0]);
                int height = Integer.parseInt(resolutionParts[1]);

                float scale = Integer.parseInt(scaleCombobox.getSelectedItem().toString());

                if (portraitMode.isSelected()) {
                    launcher.resolution(height, width, scale);
                } else {
                    launcher.resolution(width, height, scale);
                }
                launcher.launch();
            }
        }
    }

    class PluginCommandListener implements ActionListener {
        private GdxDesktopLauncherPlugin plugin;
        private String command;

        public PluginCommandListener(GdxDesktopLauncherPlugin plugin, String command) {
            this.plugin = plugin;
            this.command = command;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            plugin.executeCommand(command);
        }
    }
}
