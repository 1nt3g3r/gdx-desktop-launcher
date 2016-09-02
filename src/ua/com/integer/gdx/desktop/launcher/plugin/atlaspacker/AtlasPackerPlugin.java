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

package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JOptionPane;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

/**
 * Simple Atlas Packer plugin. Packs all atlases into your android/assets/atlases folder.
 * 
 * @author 1nt3g3r
 */
public class AtlasPackerPlugin implements GdxDesktopLauncherPlugin {
    private static final String PACK_ALL_ATLASES = "Pack all atlases";
    private static final String PACK_SELECTED_ATLASES = "Pack selected atlases...";

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
                PACK_ALL_ATLASES,
                PACK_SELECTED_ATLASES
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (PACK_ALL_ATLASES.equals(commandName)) {
        	packAllAtlases();
        } else if (PACK_SELECTED_ATLASES.equals(commandName)) {
        	packSelectedAtlases();
        }
    }
    
    private void packAllAtlases() {
    	String[] atlasNames = getAtlasNames();
		for(String name: atlasNames) {
			packAtlas(name);
		}
		JOptionPane.showMessageDialog(null, "All atlases packed");
    }
    
    private void packSelectedAtlases() {
    	PackSelectedAtlasDialog dialog = new PackSelectedAtlasDialog(this);
    	dialog.setModal(true);
    	dialog.setVisible(true);
    }

	public void packAtlas(String name) {
		TexturePacker.process("../../images/" + name, "./atlases", name + ".atlas");
	}
    
    public String[] getAtlasNames() {
		return new File("../../images").list(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return file.isDirectory();
			}
		});
	};
}
