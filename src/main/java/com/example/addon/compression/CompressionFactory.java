package com.example.addon.compression;

import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;

public class CompressionFactory {

    public static NettyCompressionDecoder getDecoder(CompressionType type, int threshold, int level) {
        if (type == CompressionType.ZSTD) {
            return new MinecraftZstdCompressionDecoder(threshold);
        }
        return new NettyCompressionDecoder(threshold);
    }

    public static NettyCompressionEncoder getEncoder(CompressionType type, int threshold, int level) {
        if (type == CompressionType.ZSTD) {
            return new MinecraftZstdCompressionEncoder(threshold, level);
        }
        return new NettyCompressionEncoder(threshold);
    }

}
