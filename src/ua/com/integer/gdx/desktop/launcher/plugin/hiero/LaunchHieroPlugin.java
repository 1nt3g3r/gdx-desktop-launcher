package ua.com.integer.gdx.desktop.launcher.plugin.hiero;

import com.badlogic.gdx.tools.hiero.Hiero;

import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

public class LaunchHieroPlugin implements GdxDesktopLauncherPlugin {
    public static final String LAUNCH_HIERO = "Launch Hiero";

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
        return "Hiero";
    }

    @Override
    public String[] getCommands() {
        return new String[] {
                LAUNCH_HIERO
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (commandName.equals(LAUNCH_HIERO)) {
            try {
                Hiero.main(new String[] {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
