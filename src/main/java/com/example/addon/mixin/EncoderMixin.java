package com.example.addon.mixin;

import com.github.luben.zstd.Zstd;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NettyCompressionEncoder;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NettyCompressionEncoder.class)
public class EncoderMixin {

    @Shadow
    private int threshold;

    @Overwrite(remap = false)
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

            packetBuffer.writeBytes(Zstd.compress(data));
        }
    }
}
