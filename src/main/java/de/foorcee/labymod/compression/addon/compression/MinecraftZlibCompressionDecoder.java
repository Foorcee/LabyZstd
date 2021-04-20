package de.foorcee.labymod.compression.addon.compression;

import de.foorcee.labymod.compression.addon.SessionSettings;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class MinecraftZlibCompressionDecoder extends NettyCompressionDecoder {

    public MinecraftZlibCompressionDecoder(int threshold) {
        super(threshold);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        int uncompressed = packetBuffer.readVarInt();
        int compressed = packetBuffer.readableBytes();
        packetBuffer.resetReaderIndex();
        long start = System.nanoTime();
        super.decode(ctx, byteBuf, list);
        if (uncompressed != 0) {
            SessionSettings.COMPRESSION_TIME.add(System.nanoTime() - start);
            SessionSettings.DECOMPRESSION_RATE.add((double) uncompressed / compressed);
        }
    }
}
