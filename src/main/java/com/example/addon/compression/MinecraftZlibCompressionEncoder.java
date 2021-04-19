package com.example.addon.compression;

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
        double rate = (double) uncompressed / compressed;
//        System.out.println("compress rate: "+ rate);
    }
}
