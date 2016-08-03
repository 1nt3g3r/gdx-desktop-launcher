package ua.com.integer.gdx.desktop.launcher.plugin;

public class AtlasPackerPlugin implements GdxDesktopLauncherPlugin {
    private static final String PACK_ALL_ATLASES = "Pack all atlases";

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
        return "Atlas Packer";
    }

    @Override
    public String[] getCommands() {
        return new String[] {
                PACK_ALL_ATLASES
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (PACK_ALL_ATLASES.equals(commandName)) {
        }
    }
}
