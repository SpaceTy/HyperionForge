package me.spacety.hyperionforge.item;

import me.spacety.hyperionforge.HyperionForgeMod;
import me.spacety.hyperionforge.item.custom.HyperionItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HyperionForgeMod.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, HyperionForgeMod.MODID);
    public static final RegistryObject<Item> HYPERION = ITEMS.register("hyperion",
            () -> new HyperionItem(ItemTier.NETHERITE, 20, -2.4F, new Item.Properties()
            .group(ItemGroup.COMBAT)
            .isImmuneToFire()
            .maxStackSize(1)
            .rarity(Rarity.EPIC)
            .maxDamage(0)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
