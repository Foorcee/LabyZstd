package com.example.addon.mixin;

import com.example.addon.ExampleAddon;
import com.example.addon.compression.CompressionFactory;
import com.example.addon.compression.CompressionType;
import io.netty.channel.Channel;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {

    @Shadow
    private Channel channel;

    @Overwrite
    public void setCompressionThreshold(int threshold) {
        int level = ExampleAddon.LEVEL;
        CompressionType type = ExampleAddon.COMPRESSION_TYPE;

        System.out.println("set compression " + threshold);

        if (threshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) this.channel.pipeline().get("decompress")).setCompressionThreshold(threshold);
            } else {
                NettyCompressionDecoder decoder = CompressionFactory.getDecoder(type, threshold, level);
                System.out.println("Enable decompression " + type + " at level "+ level + " " + decoder.getClass().getSimpleName());
                this.channel.pipeline().addBefore("decoder", "decompress", decoder);
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) this.channel.pipeline().get("compress")).setCompressionThreshold(threshold);
            } else {
                NettyCompressionEncoder encoder = CompressionFactory.getEncoder(type, threshold, level);
                System.out.println("Enabled compression " + type + " at level " + level + " " + encoder.getClass().getSimpleName());
                this.channel.pipeline().addBefore("encoder", "compress", encoder);
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }
            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }
    }
}
