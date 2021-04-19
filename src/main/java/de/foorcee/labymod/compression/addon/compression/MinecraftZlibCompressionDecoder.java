package de.foorcee.labymod.compression.addon.compression;

import de.foorcee.labymod.compression.addon.SessionSettings;
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
        SessionSettings.DECOMPRESSION_RATE.add((double) uncompressed / compressed);
    }
}
