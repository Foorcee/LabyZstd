package de.foorcee.labymod.compression.addon.compression;

import de.foorcee.labymod.compression.addon.SessionSettings;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionEncoder;

public class MinecraftZlibCompressionEncoder extends NettyCompressionEncoder {

    public MinecraftZlibCompressionEncoder(int threshold) {
        super(threshold);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBufIn, ByteBuf byteBufOut) throws Exception {
        int uncompressed = byteBufIn.readableBytes();
        super.encode(channelHandlerContext, byteBufIn, byteBufOut);
        int compressed = byteBufOut.readableBytes();
        SessionSettings.COMPRESSION_RATE.add((double) uncompressed / compressed);
    }
}
