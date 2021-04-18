package com.example.addon.mixin;

import com.github.luben.zstd.Zstd;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.List;

@Mixin(NettyCompressionDecoder.class)
public class DecoderMixin {

    @Shadow
    private int threshold;

    @Overwrite
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() == 0) {
            return;
        }
        PacketBuffer packetBuffer = new PacketBuffer(byteBuf);
        int uncompressedSize = packetBuffer.readVarInt();
        if (uncompressedSize == 0) {
            list.add(packetBuffer.readBytes(packetBuffer.readableBytes()));
        } else {
            if (uncompressedSize < this.threshold) {
                throw new DecoderException("Badly compressed packet - size of " + uncompressedSize + " is below server threshold of " + this.threshold);
            }
            if (uncompressedSize > 2097152) {
                throw new DecoderException("Badly compressed packet - size of " + uncompressedSize + " is larger than protocol maximum of " + 2097152);
            }
            byte[] data = new byte[packetBuffer.readableBytes()];
            packetBuffer.readBytes(data);

            byte[] uncompress = Zstd.decompress(data, uncompressedSize);
            list.add(Unpooled.wrappedBuffer(uncompress));
        }
    }
}
