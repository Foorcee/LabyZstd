package de.foorcee.labymod.compression.addon.compression;

import de.foorcee.labymod.compression.addon.LabyCompressionAddon;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;

public class CompressionFactory {

    public static NettyCompressionDecoder getDecoder(CompressionType type, int threshold, int level) {
        if (LabyCompressionAddon.ENABLED) {
            if (type == CompressionType.ZSTD) {
                return new MinecraftZstdCompressionDecoder(threshold);
            }
            return new MinecraftZlibCompressionDecoder(threshold);
        }
        return new NettyCompressionDecoder(threshold);
    }

    public static NettyCompressionEncoder getEncoder(CompressionType type, int threshold, int level) {
        if (LabyCompressionAddon.ENABLED) {
            if (LabyCompressionAddon.DISABLE_ENCODE_COMPRESSION) {
                return new MinecraftNoCompressionEncoder(0);
            }
            if (type == CompressionType.ZSTD) {
                return new MinecraftZstdCompressionEncoder(threshold, level);
            }
            return new MinecraftZlibCompressionEncoder(threshold);
        }
        return new NettyCompressionEncoder(threshold);
    }

}
