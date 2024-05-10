package me.spacety.hyperionforge.network;

import me.spacety.hyperionforge.item.ModItems;
import me.spacety.hyperionforge.util.HyperionBeamUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundHyperionUsePacket {
    double radius = 0.5;

    public ServerboundHyperionUsePacket() {

    }

    public void encode(PacketBuffer buffer) {

    }

    public static ServerboundHyperionUsePacket decode(PacketBuffer buffer) {
        return new ServerboundHyperionUsePacket();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            LivingEntity entity = ctx.get().getSender();

            assert entity != null;
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (player.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.HYPERION.get() && player.getCooldownTracker().hasCooldown(player.getHeldItem(player.getActiveHand()).getItem())) {
                    return;
                }
            }
            HyperionBeamUtil.hyperionBeam(entity);

        });
        ctx.get().setPacketHandled(true);
    }
}
