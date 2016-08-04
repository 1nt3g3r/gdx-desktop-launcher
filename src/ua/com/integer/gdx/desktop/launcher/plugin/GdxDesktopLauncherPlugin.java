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

package ua.com.integer.gdx.desktop.launcher.plugin;

/**
 * Plugin class for GdxDesktopLauncher
 * 
 * @author 1nt3g3r
 */
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
