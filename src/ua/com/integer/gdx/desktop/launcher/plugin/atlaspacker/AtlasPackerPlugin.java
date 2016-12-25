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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.swing.JOptionPane;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ETC1;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.StringBuilder;

import ua.com.integer.gdx.desktop.launcher.Settings;
import ua.com.integer.gdx.desktop.launcher.plugin.GdxDesktopLauncherPlugin;

/**
 * Simple Atlas Packer plugin. Packs all atlases into your android/assets/atlases folder.
 * 
 * @author 1nt3g3r
 */
public class AtlasPackerPlugin implements GdxDesktopLauncherPlugin {
    private static final String PACK_ALL_ATLASES = "Pack all atlases";
    private static final String PACK_SELECTED_ATLASES = "Setup And Pack selected atlases...";
    private static final String SETTINGS = "Settings";

    @Override
    public void onInit() {
        if (sets().getBoolean("autocheck", true)) {
            Array<String> changedAtlases = getListOfChangedAtlases();
            if (changedAtlases.size > 0) {
                askForRepackChangedAtlases(changedAtlases);
            }
        }
    }

    private Array<String> getListOfChangedAtlases() {
        Array<String> result = new Array<>();
        for(String atlasName : getAtlasNames()) {
            File atlasFolder = getAtlasFile(atlasName);
            if (ChecksumChecker.isFileChanged(atlasFolder)) {
                result.add(atlasName);
            }
        }
        return result;
    }

    private void askForRepackChangedAtlases(Array<String> changedAtlases) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("These atlases were changed from last launch:\n\n");

        for(int i = 0; i < changedAtlases.size; i++) {
            String atlasName = changedAtlases.get(i);
            strBuilder.append(atlasName);
            if (i < changedAtlases.size - 1) {
                strBuilder.append(", ");
            }
        }

        strBuilder.append("\n\nRepack these atlases?");

        int askResult = JOptionPane.showConfirmDialog(null, strBuilder.toString(), "Repack Atlases", JOptionPane.YES_NO_OPTION);
        if (askResult == JOptionPane.YES_OPTION) {
            for(String atlasName : changedAtlases) {
                packAtlas(atlasName);
            }
            JOptionPane.showMessageDialog(null, "Atlases repacked!");
        }
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
                PACK_SELECTED_ATLASES,
                SETTINGS
        };
    }

    @Override
    public void executeCommand(String commandName) {
        if (PACK_ALL_ATLASES.equals(commandName)) {
        	packAllAtlases();
        } else if (PACK_SELECTED_ATLASES.equals(commandName)) {
        	packSelectedAtlases();
        } else if (SETTINGS.equals(commandName)) {
            showSettingsDialog();
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
    	dialog.setVisible(true);
    }

    private void showSettingsDialog() {
        AtlasPackerSettingsDialog dialog = new AtlasPackerSettingsDialog(this);
        dialog.setVisible(true);
    }

	public void packAtlas(String name) {
        TexturePacker.Settings settings = null;
        try {
            settings = new Json().fromJson(TexturePacker.Settings.class, new FileInputStream(new File(getAtlasFolderPath(name), "pack.json")));
        } catch (FileNotFoundException e) {
            settings = new TexturePacker.Settings();
        }

        if (settings.outputFormat.equals("etc1")) {
            new File("./atlases/" + name + ".etc1").delete();

            TexturePacker.process(settings, "../../images/" + name, "./atlases", name + ".atlas");
            GdxNativesLoader.load();

            Pixmap pixmap = new Pixmap(new FileHandle("./atlases/" + name + ".etc1"));
            if (pixmap.getFormat() != Pixmap.Format.RGB888 && pixmap.getFormat() != Pixmap.Format.RGB565) {
                Pixmap tmp = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGB888);
                tmp.drawPixmap(pixmap, 0, 0, 0, 0, pixmap.getWidth(), pixmap.getHeight());
                pixmap.dispose();
                pixmap = tmp;
            }
            ETC1.encodeImagePKM(pixmap).write(new FileHandle(new File("./atlases/" + name + ".etc1")));
            pixmap.dispose();
        } else {
            TexturePacker.process("../../images/" + name, "./atlases", name + ".atlas");
        }

        ChecksumChecker.saveFileChecksum(getAtlasFile(name));
	}

    private File getAtlasFile(String atlasName) {
        return new File(getAtlasFolderPath(atlasName));
    }

    public String getAtlasFolderPath(String atlasName) {
        return "../../images/" + atlasName;
    }
    
    public String[] getAtlasNames() {
        File[] result = new File("../../images").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return file.isDirectory();
			}
		});
        Arrays.sort(result);

        String[] toReturn = new String[result.length];
        for(int i = 0; i < toReturn.length; i++) {
            toReturn[i] = result[i].getName();
        }
        return toReturn;
	}

    private Settings sets() {
        return Settings.getInstance().setSettingsClass(AtlasPackerPlugin.class);
    }
}
