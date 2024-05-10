package me.spacety.hyperionforge.item.custom;

import me.spacety.hyperionforge.init.PacketHandler;
import me.spacety.hyperionforge.network.ServerboundHyperionAbilityPacket;
import me.spacety.hyperionforge.network.ServerboundHyperionUsePacket;
import me.spacety.hyperionforge.util.HyperionBeamUtil;
import net.minecraft.util.Hand;
import me.spacety.hyperionforge.item.ModItems;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static me.spacety.hyperionforge.util.HyperionBeamUtil.hyperionBeam;

public class HyperionUseListener {

    @SubscribeEvent
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getPlayer().getCooldownTracker().hasCooldown(ModItems.HYPERION.get())) {
            HyperionBeamUtil.convertToPlayerEntity(event.getPlayer()).sendStatusMessage(ITextComponent.getTextComponentOrEmpty("Hyperion is on cooldown!"), true);
            return;
        }
        PacketHandler.INSTANCE.sendToServer(new ServerboundHyperionUsePacket());
        hyperionBeam(event.getEntityLiving());
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent event) {
        if (event instanceof PlayerInteractEvent.RightClickBlock || event instanceof PlayerInteractEvent.RightClickEmpty) {
            if (event.getPlayer().getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.HYPERION.get()) {
                PacketHandler.INSTANCE.sendToServer(new ServerboundHyperionAbilityPacket());
            }
        }
    }
}