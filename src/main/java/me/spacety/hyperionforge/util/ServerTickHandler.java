package me.spacety.hyperionforge.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class ServerTickHandler {
    private static final Map<PlayerEntity, List<ScheduledAttack>> scheduledAttacks = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            scheduledAttacks.forEach((player, attacks) -> {
                Iterator<ScheduledAttack> iterator = attacks.iterator();
                while (iterator.hasNext()) {
                    ScheduledAttack attack = iterator.next();
                    if (attack.delayTicks == 0) {
                        player.attackTargetEntityWithCurrentItem(attack.target);
                        iterator.remove();
                    } else {
                        attack.delayTicks--;
                    }
                }
            });
        }
    }

    public static void scheduleAttack(PlayerEntity player, LivingEntity entity, int delayTicks) {
        scheduledAttacks.computeIfAbsent(player, k -> new ArrayList<>()).add(new ScheduledAttack(entity, delayTicks));
    }
}

