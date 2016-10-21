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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.io.IOException;

import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;
import ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker.AtlasPackerPlugin;

import javax.swing.SwingUtilities;

/**
 * Desktop launcher for {@link ApplicationListener}. Allows you to set title, resolution and needed plugins before 
 * start given {@link ApplicationListener}
 * 
 * You can't create instance of this class directly. Use GdxDesktopLauncher.with() instead.
 * 
 * @author 1nt3g3r
 */
public class GdxDesktopLauncher {
    private ApplicationListener applicationListener;
    private LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

    private Array<GdxDesktopLauncherPlugin> plugins = new Array<GdxDesktopLauncherPlugin>();

    private GdxDesktopLauncher() {
	}
    
    /**
     * Entry point.
     */
    public static GdxDesktopLauncher with(ApplicationListener listener) {
        GdxDesktopLauncher launcher = new GdxDesktopLauncher();
        launcher.applicationListener = listener;
        return launcher;
    }

    /**
     * Add {@link GdxDesktopLauncherPlugin}. plugin.onInit() will be called immediately after adding.
     */
    public GdxDesktopLauncher addPlugin(GdxDesktopLauncherPlugin plugin) {
    	plugins.add(plugin);
        plugin.onInit();
        return this;
    }
    
    public GdxDesktopLauncher addDefaultPlugins() {
    	return addPlugin(new AtlasPackerPlugin());
    }

    /**
     * Set resolution to launch
     * 
     * @param scale width and height will be multiplied to scale. It can be useful if width and height are too big, 
     * but you want to see if game works good with this aspect ratio
     */
    public GdxDesktopLauncher resolution(int width, int height, float scale) {
        config.width = (int) (width * scale);
        config.height = (int) (height * scale);
        return this;
    }

    /**
     * Should game launch in full screen
     * @param fullScreen
     */
    public GdxDesktopLauncher fullScreen(boolean fullScreen) {
        config.fullscreen = fullScreen;
        return this;
    }

    /**
     * Set window title
     */
    public GdxDesktopLauncher title(String title) {
        config.title = title;
        return this;
    }

    /**
     * Show UI window, where you can setup launcher parameters, use plugins, and run game.
     *
     * If launcher launches under IDE, it will just launch game
     */
    public void showUI() {
        boolean runUnderIDE = false;

        try {
            String fullPath = new File(".").getCanonicalPath();
            if (fullPath.endsWith("android/assets")) {
                runUnderIDE = true;
            }
        } catch ( Exception ex) {
            ex.printStackTrace();
        }

        if (runUnderIDE) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new GdxDesktopLauncherUI(GdxDesktopLauncher.this);
                }
            });
        } else {
            launch();
        }
    }

    /**
     * Launch game
     */
    public void launch() {
        for(int i = 0; i < plugins.size; i++) {
            plugins.get(i).onLaunch();
        }

        new LwjglApplication(applicationListener, config).addLifecycleListener(new LifecycleListener() {
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

    public ApplicationListener getApplicationListener() {
        return applicationListener;
    }

    public LwjglApplicationConfiguration getConfig() {
        return config;
    }

    /**
     * @return list of loaded plugins
     */
    public Array<GdxDesktopLauncherPlugin> getPlugins() {
        return plugins;
    }
}
