package me.spacety.hyperionforge.util;

import me.spacety.hyperionforge.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class HyperionBeamUtil {

    public static void hyperionBeam(LivingEntity entity) {

        PlayerEntity player = convertToPlayerEntity(entity);
        if (!player.getEntityWorld().isRemote()) {
            if (player == null || entity.getHeldItem(entity.getActiveHand()).getItem() != ModItems.HYPERION.get()) {
                return;
            }
        }else {
            if (player == null || entity.getHeldItem(Hand.MAIN_HAND).getItem() != ModItems.HYPERION.get()) {
                return;
            }
        }

        Vector3d start = entity.getPositionVec().add(0, entity.getEyeHeight(), 0);
        Vector3d direction = getLookDirection(entity);
        player.getCooldownTracker().setCooldown(ModItems.HYPERION.get(), 20);
        particleBeam(start, entity, direction);
    }

    public static Vector3d getLookDirection(LivingEntity entity) {
        float delta = Minecraft.getInstance().getRenderPartialTicks();

        float yaw = (float) Math.toRadians(entity.getYaw(delta));
        float pitch = (float) Math.toRadians(entity.getPitch(delta));

        double x = -Math.sin(yaw) * Math.cos(pitch);
        double y = -Math.sin(pitch);
        double z = Math.cos(yaw) * Math.cos(pitch);

        return new Vector3d(x, y, z).normalize();
    }

    public static float getPitch(Vector3d direction) {
        direction = direction.normalize();

        double y = direction.y;

        float pitch = (float) Math.asin(-y);

        return pitch;
    }

    public static float getYaw(Vector3d direction) {
        direction = direction.normalize();

        double x = direction.x;
        double z = direction.z;

        float yaw = (float) Math.atan2(-x, z);

        return yaw;
    }



    public static PlayerEntity convertToPlayerEntity(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return (PlayerEntity) entity;
        } else {
            return null;
        }
    }

    public static void particleBeam(Vector3d start, LivingEntity entity, Vector3d direction) {
        final double distance = 50.0;
        final double interval = 0.1;
        final double radius = 0.5;
        PlayerEntity player = convertToPlayerEntity(entity);

        Vector3d normalizedDirection = direction.normalize();
        List<LivingEntity> hitEntities = new ArrayList<>();
        for (double i = 0; i <= distance; i += interval) {

            Vector3d position = start.add(normalizedDirection.scale(i));
            if (!player.getEntityWorld().isRemote()) {
                AxisAlignedBB aabb = new AxisAlignedBB(
                        position.x - radius, position.y - radius, position.z - radius,
                        position.x + radius, position.y + radius, position.z + radius
                );

                entity.getEntityWorld().getEntitiesInAABBexcluding(player, aabb, (e) -> e != player).forEach((e) -> {
                    if (e instanceof LivingEntity livingEntity) {
                        if (hitEntities != null && !hitEntities.contains(e)) {

                            ServerTickHandler.scheduleAttack(player, livingEntity, 2);

                        }
                        hitEntities.add((LivingEntity) e);
                    }
                });
            } else {
                entity.getEntityWorld().addParticle(ParticleTypes.END_ROD, true, position.x, position.y, position.z, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
