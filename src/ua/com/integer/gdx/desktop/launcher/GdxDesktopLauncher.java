package ua.com.integer.gdx.desktop.launcher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;

import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

import javax.swing.SwingUtilities;

public class GdxDesktopLauncher {
    private ApplicationListener listener;
    private LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    private Array<GdxDesktopLauncherPlugin> plugins = new Array<GdxDesktopLauncherPlugin>();

    public static GdxDesktopLauncher with(ApplicationListener listener) {
        GdxDesktopLauncher launcher = new GdxDesktopLauncher();
        launcher.listener = listener;
        return launcher;
    }

    public GdxDesktopLauncher addPlugin(GdxDesktopLauncherPlugin plugin) {
        plugin.onInit();
        plugins.add(plugin);
        return this;
    }

    public GdxDesktopLauncher resolution(int width, int height, float scale) {
        config.width = (int) (width * scale);
        config.height = (int) (height * scale);
        return this;
    }

    public GdxDesktopLauncher title(String title) {
        config.title = title;
        return this;
    }

    public void showUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GdxDesktopLauncherUI(GdxDesktopLauncher.this);
            }
        });
    }

    public void launch() {
        for(int i = 0; i < plugins.size; i++) {
            plugins.get(i).onLaunch();
        }

        new LwjglApplication(listener, config).addLifecycleListener(new LifecycleListener() {
            @Override
            public void pause() {}
            @Override
            public void resume() {}
            @Override
            public void dispose() {
                for(int i = 0; i < plugins.size; i++) {
                    plugins.get(i).onExit();
                }
            }
        });
    }

    public ApplicationListener getListener() {
        return listener;
    }

    public LwjglApplicationConfiguration getConfig() {
        return config;
    }

    public Array<GdxDesktopLauncherPlugin> getPlugins() {
        return plugins;
    }
}
