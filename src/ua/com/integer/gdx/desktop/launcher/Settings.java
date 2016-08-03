package ua.com.integer.gdx.desktop.launcher;

import java.util.prefs.Preferences;

public class Settings {
	private Preferences prefs;
	private static Settings instance = new Settings();

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

	public void setScreenResolution(int width, int height) {
		prefs.putInt("screen-width", width);
		prefs.putInt("screen-height", height);
		flush();
	}

	public void setPortraitOrientaion(boolean portrait) {
		prefs.putBoolean("portrait-orientation", portrait);
	}

	public boolean isPortraitOrientation() {
		return prefs.getBoolean("portrait-orientation", false);
	}

	public int getScreenWidth() {
		return prefs.getInt("screen-width", 800);
	}

	public int getScreenHeight() {
		return prefs.getInt("screen-height", 480);
	}

	public void setScale(String scale) {
		prefs.put("scale-coeff", scale);
		flush();
	}

	public String getScale() {
		return prefs.get("scale-coeff", "1");
	}

	private void flush() {
		try {
			prefs.flush();
		} catch(Exception e) {
		}
	}
}