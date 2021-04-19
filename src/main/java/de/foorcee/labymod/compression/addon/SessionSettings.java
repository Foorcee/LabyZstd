package de.foorcee.labymod.compression.addon;

import de.foorcee.labymod.compression.addon.compression.CompressionType;
import de.foorcee.labymod.compression.addon.utils.SimpleMovingAverage;

public class SessionSettings {
    public static CompressionType SESSION_COMPRESSION_TYPE;
    public static Integer SESSION_COMPRESSION_LEVEL;

    public static SimpleMovingAverage COMPRESSION_RATE = new SimpleMovingAverage(50);
    public static SimpleMovingAverage DECOMPRESSION_RATE = new SimpleMovingAverage(50);
}
