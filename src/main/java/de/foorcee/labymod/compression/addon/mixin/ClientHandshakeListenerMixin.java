package de.foorcee.labymod.compression.addon.mixin;

import de.foorcee.labymod.compression.addon.LabyCompressionAddon;
import de.foorcee.labymod.compression.addon.SessionSettings;
import de.foorcee.labymod.compression.addon.compression.CompressionType;
import de.foorcee.labymod.compression.addon.reflection.ReflectionAPI;
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
        ResourceLocation resourceLocation = ReflectionAPI.getValuePrintException(SCustomPayloadLoginPacket.class, packet, "b");
        if (LabyCompressionAddon.ENABLED) {
            if (resourceLocation.toString().equals("labymod3:compression")) {
                SessionSettings.SESSION_COMPRESSION_TYPE = LabyCompressionAddon.COMPRESSION_TYPE;
                SessionSettings.SESSION_COMPRESSION_LEVEL = LabyCompressionAddon.LEVEL;

                PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
                packetBuffer.writeString(SessionSettings.SESSION_COMPRESSION_TYPE.name());
                packetBuffer.writeVarInt(SessionSettings.SESSION_COMPRESSION_LEVEL);
                networkManager.sendPacket(new CCustomPayloadLoginPacket(packet.getTransaction(), packetBuffer));
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "handleEncryptionRequest", at = @At("HEAD"))
    public void handleEncryptionRequest(SEncryptionRequestPacket packet, CallbackInfo callbackInfo) {
        SessionSettings.SESSION_COMPRESSION_TYPE = CompressionType.ZLIB;
        SessionSettings.SESSION_COMPRESSION_LEVEL = -1;
        System.out.println("reset compression");
    }
}
