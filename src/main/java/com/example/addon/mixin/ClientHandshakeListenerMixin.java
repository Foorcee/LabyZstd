package com.example.addon.mixin;

import com.example.addon.ExampleAddon;
import com.example.addon.compression.CompressionType;
import com.example.addon.reflection.ReflectionAPI;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.login.ClientLoginNetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.client.CCustomPayloadLoginPacket;
import net.minecraft.network.login.server.SCustomPayloadLoginPacket;
import net.minecraft.network.login.server.SEncryptionRequestPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientLoginNetHandler.class)
public class ClientHandshakeListenerMixin {

    @Shadow
    private NetworkManager networkManager;

    @Shadow
    private Consumer<ITextComponent> statusMessageConsumer;

    @Inject(method = "handleCustomPayloadLogin", at = @At("HEAD"), cancellable = true)
    public void handleCustomPayloadLogin(SCustomPayloadLoginPacket packet, CallbackInfo callbackInfo) {
        this.statusMessageConsumer.accept(new TranslationTextComponent("connect.negotiating"));
        ResourceLocation resourceLocation = ReflectionAPI.getValuePrintException(SCustomPayloadLoginPacket.class, packet, "b");
        if (resourceLocation.toString().equals("labymod3:compression")) {
            ExampleAddon.COMPRESSION_TYPE = CompressionType.ZSTD;
            ExampleAddon.LEVEL = 3;

            PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeString(ExampleAddon.COMPRESSION_TYPE.name());
            packetBuffer.writeVarInt(ExampleAddon.LEVEL);
            networkManager.sendPacket(new CCustomPayloadLoginPacket(packet.getTransaction(), packetBuffer));
            callbackInfo.cancel();
        }
    }

    @Inject(method = "handleEncryptionRequest", at = @At("HEAD"))
    public void handleEncryptionRequest(SEncryptionRequestPacket packet, CallbackInfo callbackInfo) {
        ExampleAddon.COMPRESSION_TYPE = CompressionType.ZLIB;
        ExampleAddon.LEVEL = -1;
        System.out.println("reset compression");
    }
}
