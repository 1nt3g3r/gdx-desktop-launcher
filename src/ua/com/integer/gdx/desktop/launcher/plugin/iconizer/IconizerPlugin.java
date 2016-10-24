package ua.com.integer.gdx.desktop.launcher.plugin.iconizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;

import ua.com.integer.gdx.desktop.launcher.Settings;
import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

public class IconizerPlugin implements GdxDesktopLauncherPlugin {
    public static final String SET_SOURCE_FOLDER = "Set source folder";
    public static final String ICONIZE_ANDROID = "Iconize Android";
    public static final String ICONIZE_IOS = "Iconize iOS";

    @Override
    public void onInit() {

    }

    @Override
    public void onLaunch() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Iconizer";
    }

    @Override
    public String[] getCommands() {
        return new String[] {
                SET_SOURCE_FOLDER,
                ICONIZE_ANDROID,
                ICONIZE_IOS
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (commandName.equals(SET_SOURCE_FOLDER)) {
            setSetSourceFolder();
        } else if (commandName.equals(ICONIZE_ANDROID)) {
            iconizeAndroid();
        } else if (commandName.equals(ICONIZE_IOS)) {
            iconizeIOS();
        }
    }

    private void setSetSourceFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(getSourceFolder());
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            setSourceFolder(fileChooser.getSelectedFile());
        }
    }

    private void iconizeAndroid() {
        copy("icon-hdpi.png", "drawable-hdpi");
        copy("icon-mdpi.png", "drawable-mdpi");
        copy("icon-xhdpi.png", "drawable-xhdpi");
        copy("icon-xxhdpi.png", "drawable-xxhdpi");
    }

    private void copy(String source, String destination) {
        copyFileUsingStream(new File(getSourceFolder(), source), new File("../res/" + destination + "/ic_launcher.png"));
    }

    private void copyFileUsingStream(File source, File dest) {
        try {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(source);
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                is.close();
                os.close();
            }
        } catch (Exception ex) {

        }

    }

    private void iconizeIOS() {

    }
    private Settings sets() {
        return Settings.getInstance().setSettingsClass(IconizerPlugin.class);
    }
    private File getSourceFolder() {
        return new File(sets().getString("source.path", "."));
    }

    private void setSourceFolder(File path) {
        sets().putString("source.path", path.getAbsolutePath());
    }

}
