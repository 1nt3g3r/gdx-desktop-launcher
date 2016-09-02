package ua.com.integer.gdx.desktop.launcher;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;

public class Util {
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static int screenCenterX = screenSize.width / 2;
    private static int screenCenterY = screenSize.height / 2;

	public static void situateOnCenter(JDialog dialog) {
		int frameWidth = dialog.getSize().width;
		int frameHeight = dialog.getSize().height;

		int frameX = screenCenterX - frameWidth / 2;
		int frameY = screenCenterY - frameHeight / 2;

		dialog.setLocation(frameX, frameY);
	}
}
