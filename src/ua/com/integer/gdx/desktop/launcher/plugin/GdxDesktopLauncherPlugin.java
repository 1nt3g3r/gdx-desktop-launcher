package ua.com.integer.gdx.desktop.launcher.plugin;

public interface GdxDesktopLauncherPlugin {
    /**
     * This method called only once when you add plugin
     */
    public void onInit();

    /**
     * This method called immediately before launch you ApplicationListener
     */
    public void onLaunch();

    /**
     * This method called when you dispose your ApplicationListener (close your app)
     */
    public void onExit();

    /**
     * Returns plugin name
     */
    public String getName();

    /**
     * Return list of available commands. You can pass any of these commands to executeCommand methods.
     * These commands will be visible in user interface
     */
    public String[] getCommands();

    /**
     * Execute command. You should handle at least all getCommands() commands
     */
    public void executeCommand(String commandName);
}
