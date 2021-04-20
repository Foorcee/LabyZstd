package de.foorcee.labymod.compression.addon.compression;

import com.github.luben.zstd.Zstd;
import de.foorcee.labymod.compression.addon.SessionSettings;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.NettyCompressionDecoder;
import net.minecraft.network.PacketBuffer;

import java.util.List;

public class MinecraftZstdCompressionDecoder extends NettyCompressionDecoder {

    private int threshold;

    public MinecraftZstdCompressionDecoder(int threshold) {
        super(threshold);
        this.threshold = threshold;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        boolean compressed = false;
        long start = System.nanoTime();
        try {
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

                try {
                    packetBuffer.readBytes(data);

                    byte[] uncompress = Zstd.decompress(data, uncompressedSize);

                    list.add(Unpooled.wrappedBuffer(uncompress));
                    SessionSettings.DECOMPRESSION_RATE.add((double) uncompress.length / data.length);
                    compressed = true;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
        } finally {
            if (compressed) {
                SessionSettings.COMPRESSION_TIME.add(System.nanoTime() - start);
            }
        }
    }

    @Override
    public void setCompressionThreshold(int threshold) {
        this.threshold = threshold;
    }
}
