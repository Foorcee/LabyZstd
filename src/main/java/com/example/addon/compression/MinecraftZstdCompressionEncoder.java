package com.example.addon.compression;

import com.github.luben.zstd.Zstd;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.PacketBuffer;

public class MinecraftZstdCompressionEncoder extends NettyCompressionEncoder {

    private int threshold;
    private int level;

    public MinecraftZstdCompressionEncoder(int threshold, int level) {
        super(threshold);
        this.threshold = threshold;
        this.level = level;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBufIn, ByteBuf byteBufOut) throws Exception {
        int size = byteBufIn.readableBytes();
        PacketBuffer packetBuffer = new PacketBuffer(byteBufOut);
        if (size < this.threshold) {
            packetBuffer.writeVarInt(0);
            packetBuffer.writeBytes(byteBufIn);
        } else {
            byte[] data = new byte[size];
            byteBufIn.readBytes(data);
            packetBuffer.writeVarInt(data.length);

            byte[] compress = Zstd.compress(data);
            packetBuffer.writeBytes(compress);
        }
    }

    @Override
    public void setCompressionThreshold(int threshold) {
        this.threshold = threshold;
    }
}
