package ua.com.integer.gdx.desktop.launcher.plugin.screenshoter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ua.com.integer.gdx.desktop.launcher.GdxDesktopLauncher;
import ua.com.integer.gdx.desktop.launcher.Settings;
import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

public class ScreenshoterPlugin implements GdxDesktopLauncherPlugin {
    public static final String SET_FOLDER = "Set output folder";
    public static final String MAKE_SCREENSHOT = "Make screenshot";

    @Override
    public void onInit() {

    }

    @Override
    public void onLaunch() {
        GdxDesktopLauncher.getInstance().getUi().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    makeScreenshot();
                }
            }
        });
    }

    @Override
    public void onExit() {

    }

    @Override
    public String getName() {
        return "Screenshoter";
    }

    @Override
    public String[] getCommands() {
        return new String[] {
                MAKE_SCREENSHOT,
                SET_FOLDER
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (commandName.equals(SET_FOLDER)) {
            setFolder();
        } else if (commandName.equals(MAKE_SCREENSHOT)) {
            makeScreenshot();
        }
    }

    private void setFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(getScreenshotFolder());
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            setScreenshotFolder(fileChooser.getSelectedFile());
        }
    }

    private void makeScreenshot() {
        if (Gdx.graphics == null) {
            JOptionPane.showMessageDialog(null, "You should launch app!");
            return;
        }

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

                Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
                BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
                PixmapIO.writePNG(Gdx.files.absolute(new File(getScreenshotFolder(), "screenshot " + getHumanReadableDate() + ".png").getAbsolutePath()), pixmap);
                pixmap.dispose();
            }
        });
    }

    private String getHumanReadableDate() {
        Calendar c = new GregorianCalendar();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);

        String dateRecord = getTwoDigitsString(day) + "." + getTwoDigitsString(month + 1) + "." + getTwoDigitsString(year);
        String timeRecord = getTwoDigitsString(hour) + "-" + getTwoDigitsString(minute) + "-" + getTwoDigitsString(second);

        return timeRecord + ", " + dateRecord;
    }

    private String getTwoDigitsString(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return value + "";
        }
    }

    private Settings sets() {
        return Settings.getInstance().setSettingsClass(ScreenshoterPlugin.class);
    }
    private File getScreenshotFolder() {
        return new File(sets().getString("screenshot.path", "."));
    }

    private void setScreenshotFolder(File path) {
        sets().putString("screenshot.path", path.getAbsolutePath());
    }
}
