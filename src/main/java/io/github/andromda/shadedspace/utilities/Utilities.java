package io.github.andromda.shadedspace.utilities;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Utilities {

    public static Object[] extractLocationData(Location loc) {
        if (loc != null) {
            float x, y, z, pitch, yaw;
            String world = loc.getWorld().getName();
            x = (float) loc.getX();
            y = (float) loc.getY();
            z = (float) loc.getZ();
            pitch = loc.getPitch();
            yaw = loc.getYaw();
            return new Object[]{world, x, y, z, pitch, yaw};
        } else throw new UnsupportedOperationException("Location was null");
    }

    public static void saveCustom(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
