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

import java.io.File;
import java.io.FilenameFilter;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Simple Atlas Packer plugin. Packs all atlases into your android/assets/atlases folder.
 * 
 * @author 1nt3g3r
 */
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
        	String[] atlasNames = getAtlases();
    		for(String name: atlasNames) {
    			TexturePacker.process("../../images/" + name, "./atlases", name + ".atlas");
    		}
        }
    }
    
    private String[] getAtlases() {
		return new File("../../images").list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return file.isDirectory();
			}
		});
	};
}
