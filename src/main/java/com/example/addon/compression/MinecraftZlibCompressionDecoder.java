package com.example.addon.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionDecoder;

import java.util.List;

public class MinecraftZlibCompressionDecoder extends NettyCompressionDecoder {


    public MinecraftZlibCompressionDecoder(int threshold) {
        super(threshold);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        int compressed = byteBuf.readableBytes();
        super.decode(ctx, byteBuf, list);
        int uncompressed = 0;
        for (Object o : list) {
            if (o instanceof ByteBuf) {
                uncompressed += ((ByteBuf) o).readableBytes();
            }
        }
        double rate = (double) uncompressed / compressed;
//        System.out.println("decompress rate: "+ rate);
    }
}
