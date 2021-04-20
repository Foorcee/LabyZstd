package de.foorcee.labymod.compression.addon.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.PacketBuffer;

public class MinecraftNoCompressionEncoder extends NettyCompressionEncoder {

    public MinecraftNoCompressionEncoder(int threshold) {
        super(threshold);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, ByteBuf byteBuf2) throws Exception {
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf2);
        packetBuffer.writeVarInt(0);
        packetBuffer.writeBytes(byteBuf);
    }
}
