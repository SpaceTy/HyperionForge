package me.spacety.hyperionforge.init;

import me.spacety.hyperionforge.HyperionForgeMod;
import me.spacety.hyperionforge.network.ClientboundExplosionParticlePacket;
import me.spacety.hyperionforge.network.ServerboundHyperionAbilityPacket;
import me.spacety.hyperionforge.network.ServerboundHyperionUsePacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {
    private PacketHandler() {
    }

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HyperionForgeMod.MODID, "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    public static void init() {
        int index = 0;
        INSTANCE.messageBuilder(ServerboundHyperionUsePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerboundHyperionUsePacket::encode)
                .decoder(ServerboundHyperionUsePacket::decode)
                .consumer(ServerboundHyperionUsePacket::handle)
                .add();
        INSTANCE.messageBuilder(ServerboundHyperionAbilityPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ServerboundHyperionAbilityPacket::encode)
                .decoder(ServerboundHyperionAbilityPacket::decode)
                .consumer(ServerboundHyperionAbilityPacket::handle)
                .add();
        INSTANCE.messageBuilder(ClientboundExplosionParticlePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientboundExplosionParticlePacket::decode).encoder(ClientboundExplosionParticlePacket::encode)
                .consumer(ClientboundExplosionParticlePacket::handle)
                .add();
    }

}
