package me.spacety.hyperionforge.util;

import me.spacety.hyperionforge.init.PacketHandler;
import me.spacety.hyperionforge.item.custom.HyperionUpgradeHandler;
import me.spacety.hyperionforge.network.ClientboundExplosionParticlePacket;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class HyperionAbility {

    public static void teleport(LivingEntity entity) {
        Vector3d lookingDirection = HyperionBeamUtil.getLookDirection(entity);
        Vector3d startPosition = entity.getPositionVec().add(0, entity.getEyeHeight(), 0);
        Vector3d endPosition = startPosition.add(lookingDirection.scale(10));

        for (int i = 0; i <= 10; i++) {
            Vector3d checkPosition = startPosition.add(lookingDirection.scale(i));

            if (isBlockSolid(checkPosition, entity)) {
                endPosition = checkPosition.subtract(lookingDirection.scale(1));
                endPosition = findAirBlockCenterNear(endPosition, entity);
                break;
            }
        }
        entity.setPositionAndUpdate(endPosition.x, endPosition.y + 0.1, endPosition.z);
    }

    private static boolean isBlockSolid(Vector3d position, LivingEntity entity) {
        return entity.world.getBlockState(new BlockPos(position)).isSolid();
    }

    private static Vector3d findAirBlockCenterNear(Vector3d position, LivingEntity entity) {
        BlockPos pos = new BlockPos(position);
        while (!entity.world.isAirBlock(pos) && !entity.world.getBlockState(pos).getBlock().equals(Blocks.WATER) && !entity.world.getBlockState(pos).getBlock().equals(Blocks.LAVA) && !entity.world.getBlockState(pos).getBlock().equals(Blocks.CAVE_AIR)) {
            pos = pos.up();
        }
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
    }

    public static void explosion(LivingEntity entity) {
        double explosionRadius = 10.0;
        World world = entity.getEntityWorld();
        float damage = entity.getMaxHealth() / (9 - (HyperionUpgradeHandler.getStarCount(entity.getHeldItem(entity.getActiveHand()))));

        List<LivingEntity> nearbyEntities = world.getEntitiesWithinAABB(LivingEntity.class,
                new AxisAlignedBB(
                        entity.getPosX() - explosionRadius, entity.getPosY() - explosionRadius, entity.getPosZ() - explosionRadius,
                        entity.getPosX() + explosionRadius, entity.getPosY() + explosionRadius, entity.getPosZ() + explosionRadius),
                e -> e != entity && e instanceof LivingEntity);

        DamageSource source = DamageSource.causeExplosionDamage(entity);

        for (LivingEntity target : nearbyEntities) {
            target.attackEntityFrom(source, damage);
        }

        List<PacketDistributor.PacketTarget> targets = new ArrayList<>();
        for (LivingEntity target : nearbyEntities) {
            targets.add(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(target.getPosX(), target.getPosY(), target.getPosZ(), 100, world.getDimensionKey())));
        }

        PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(entity.getPosX(), entity.getPosY(), entity.getPosZ(), 100, world.getDimensionKey())),
                new ClientboundExplosionParticlePacket(entity.getPosition()));
    }

}

