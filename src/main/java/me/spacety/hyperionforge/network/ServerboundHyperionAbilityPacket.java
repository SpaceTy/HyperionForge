package me.spacety.hyperionforge.network;

import me.spacety.hyperionforge.item.ModItems;
import me.spacety.hyperionforge.util.HyperionAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static me.spacety.hyperionforge.item.custom.HyperionUpgradeHandler.updatePlayerHealthInNBT;

public class ServerboundHyperionAbilityPacket {
    double radius = 0.5;

    public ServerboundHyperionAbilityPacket() {

    }

    public void encode(PacketBuffer buffer) {

    }

    public static ServerboundHyperionAbilityPacket decode(PacketBuffer buffer) {
        return new ServerboundHyperionAbilityPacket();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            PlayerEntity entity = ctx.get().getSender();
            assert entity != null;
            if (entity.getHeldItem(entity.getActiveHand()).getItem() != ModItems.HYPERION.get()) {
                return;
            }
            HyperionAbility.teleport(entity);
            updatePlayerHealthInNBT(entity.getHeldItem(entity.getActiveHand()), entity);
            HyperionAbility.explosion(entity);
        });
        ctx.get().setPacketHandled(true);
    }
}
