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

import java.util.prefs.Preferences;

/**
 * This class holds all persistent settings. Use it to save some params (like values that user input).
 * 
 * Settings are unique for each calling class. Call {@link #setSettingsClass} to set working class
 * 
 * @author 1nt3g3r
 */
public class Settings {
	private Preferences prefs;
	private static Settings instance = new Settings();

	/**
	 * Set working class. All settings are class-dependent
	 */
	public Settings setSettingsClass(Class<? extends Object> cl) {
		prefs = Preferences.userNodeForPackage(cl);
		return this;
	}

	public static Settings getInstance() {
		if (instance.prefs == null) {
			instance.setSettingsClass(Settings.class);
		}
		return instance;
	}
	
	public void putString(String key, String value) {
		prefs.put(key, value);
		flush();
	}
	
	public String getString(String key, String defValue) {
		return prefs.get(key, defValue);
	}
	
	public void putBoolean(String key, boolean value) {
		prefs.putBoolean(key, value);
		flush();
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		return prefs.getBoolean(key, defValue);
	}
	
	public void putInt(String key, int value) {
		prefs.putInt(key, value);
		flush();
	}
	
	public int getInt(String key, int defValue) {
		return prefs.getInt(key, defValue);
	}
	
	public void putFloat(String key, float value) {
		prefs.putFloat(key, value);
		flush();
	}
	
	public float getFloat(String key, float defValue) {
		return prefs.getFloat(key, defValue);
	}

	private void flush() {
		try {
			prefs.flush();
		} catch(Exception e) {
		}
	}
}