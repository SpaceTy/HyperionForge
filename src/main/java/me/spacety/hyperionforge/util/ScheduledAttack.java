package me.spacety.hyperionforge.util;

import net.minecraft.entity.LivingEntity;

public class ScheduledAttack {
    public LivingEntity target;
    public int delayTicks;

    public ScheduledAttack(LivingEntity target, int delayTicks) {
        this.target = target;
        this.delayTicks = delayTicks;
    }
}
