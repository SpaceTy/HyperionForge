package me.spacety.hyperionforge.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundExplosionParticlePacket {
    private final BlockPos position;

    public ClientboundExplosionParticlePacket(BlockPos position) {
        this.position = position;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(position);
    }

    public static ClientboundExplosionParticlePacket decode(PacketBuffer buffer) {
        BlockPos position = buffer.readBlockPos();
        return new ClientboundExplosionParticlePacket(position);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                net.minecraft.client.world.ClientWorld world = net.minecraft.client.Minecraft.getInstance().world;
                world.addParticle(ParticleTypes.EXPLOSION_EMITTER,
                        position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5,
                        7,
                        0.0D, 0.0D);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
