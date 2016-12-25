package ua.com.integer.gdx.desktop.launcher.plugin.atlaspacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.compression.CRC;

import java.io.File;

import ua.com.integer.gdx.desktop.launcher.Settings;

public class ChecksumChecker {
    public static void saveFileChecksum(File file) {
        String name = file.getName();
        sets().putInt(name + "-digest", getChecksum(file));
    }
    public static boolean isFileChanged(File file) {
        String name = file.getName();
        int oldChecksum = sets().getInt(name + "-digest", 0);
        int newChecksum = getChecksum(file);
        return oldChecksum != newChecksum;
    }

    public static int getChecksum(File file) {
        return getChecksum(new FileHandle(file));
    }

    public static int getChecksum(FileHandle handle) {
        CRC crc = new CRC();
        addBytesToCRC(crc, handle);
        return crc.GetDigest();
    }

    private static void addBytesToCRC(CRC crc, FileHandle handle) {
        if (handle.isDirectory()) {
            for(FileHandle child : handle.list()) {
                addBytesToCRC(crc, child);
            }
        } else {
            crc.Update(handle.readBytes());
        }
    }

    private static Settings sets() {
        return Settings.getInstance().setSettingsClass(ChecksumChecker.class);
    }
}
