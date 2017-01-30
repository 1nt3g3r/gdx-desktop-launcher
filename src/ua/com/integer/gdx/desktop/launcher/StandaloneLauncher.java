package ua.com.integer.gdx.desktop.launcher;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StandaloneLauncher extends JDialog {
    private boolean needFullExit = true;
    private JComboBox<String> resolutionBox;
    private JCheckBox portrait, fullscreen;

    private GdxDesktopLauncher launcher;

    public StandaloneLauncher(GdxDesktopLauncher launcher) {
        this.launcher = launcher;

        setSize(300, 150);
        setTitle("Launcher");
        Util.situateOnCenter(this);
        addWindowListener(new ExitListener());

        setupContentPanel();

        space(5);
        addResolutionPanel();
        space(5);
        addFullScreenAndPortraitPanel();

        add(Box.createVerticalGlue());
        addLaunchButton();
        space(5);

        loadSettings();

        setVisible(true);
    }

    private void setupContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        setContentPane(contentPanel);
    }

    private void addResolutionPanel() {
        JPanel resolutionPanel = new JPanel();
        resolutionPanel.setBorder(BorderFactory.createEtchedBorder());
        resolutionPanel.setLayout(new BoxLayout(resolutionPanel, BoxLayout.LINE_AXIS));

        resolutionPanel.add(new JLabel("Resolution: "));

        resolutionBox = new JComboBox<>(new String[] {"2048x1536", "1280x800", "1024x768", "1024x600", "1136x640", "960x540", "800x480", "640x480" });
        resolutionPanel.add(Util.size(resolutionBox, 100, 30));

        resolutionPanel.add(Box.createHorizontalGlue());

        add(resolutionPanel);
    }

    private void addFullScreenAndPortraitPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));

        portrait = new JCheckBox("Portrait mode");
        panel.add(portrait);

        panel.add(Box.createRigidArea(new Dimension(5, 5)));

        fullscreen = new JCheckBox("Fullscreen");
        panel.add(fullscreen);

        panel.add(Box.createHorizontalGlue());

        add(panel);
    }

    private void addLaunchButton() {
        JPanel launchPanel = new JPanel();
        launchPanel.setLayout(new BoxLayout(launchPanel, BoxLayout.LINE_AXIS));

        JButton launchButton = new JButton("Launch");
        launchButton.addActionListener(new LaunchListener());
        launchPanel.add(Box.createHorizontalGlue());
        launchPanel.add(launchButton);
        launchPanel.add(Box.createRigidArea(new Dimension(5, 5)));

        add(launchPanel);
    }

    private void space(int amount) {
        add(Box.createRigidArea(new Dimension(amount, amount)));
    }

    class LaunchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            needFullExit = false;

            String[] resolutionParts = resolutionBox.getSelectedItem().toString().split("x");
            int width = Integer.parseInt(resolutionParts[0]);
            int height = Integer.parseInt(resolutionParts[1]);

            boolean portraitMode = portrait.isSelected();
            boolean fullscreenMode = fullscreen.isSelected();

            if (portraitMode) {
                launcher.resolution(height, width, 1f);
            } else {
                launcher.resolution(width, height, 1f);
            }
            launcher.fullScreen(fullscreenMode).launch();

            dispose();
        }
    }

    private void loadSettings() {
        Settings sets = sets();

        resolutionBox.setSelectedItem(sets.getString("resolution", "800x480"));
        fullscreen.setSelected(sets.getBoolean("fullscreen", false));
        portrait.setSelected(sets.getBoolean("portrait", false));
    }

    private void saveSettings() {
        Settings sets = sets();
        sets.putString("resolution", resolutionBox.getSelectedItem().toString());
        sets.putBoolean("fullscreen", fullscreen.isSelected());
        sets.putBoolean("portrait", portrait.isSelected());
    }

    private Settings sets() {
        return Settings.getInstance().setSettingsClass(launcher.getApplicationListener().getClass());
    }

    class ExitListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            saveSettings();

            if (needFullExit) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StandaloneLauncher(null);
            }
        });
    }
}
