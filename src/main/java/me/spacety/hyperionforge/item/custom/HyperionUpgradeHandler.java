package me.spacety.hyperionforge.item.custom;

import me.spacety.hyperionforge.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.Style;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HyperionUpgradeHandler {

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.getItem() == ModItems.HYPERION.get()) {
            ItemStack starStack = event.getPlayer().getHeldItemOffhand();
            if (!starStack.isEmpty() && starStack.getItem() == Items.NETHER_STAR && getStarCount(itemStack) < 5 ) {
                upgradeSwordWithStar(itemStack);
                starStack.shrink(1);
                event.setCanceled(true);
            }
        }
    }

    private static void upgradeSwordWithStar(ItemStack sword) {
        CompoundNBT nbt = sword.getOrCreateTag();
        int stars = nbt.getInt("Stars");
        nbt.putInt("Stars", stars + 1);
        sword.setTag(nbt);

        updateSwordName(sword, stars + 1);
    }

    private static void updateSwordName(ItemStack sword, int starCount) {
        String namePrefix = "&dHeroic Hyperion ";
        sword.setDisplayName(HyperionItem.convertColorCodes(namePrefix + "&6✪".repeat(Math.max(0, starCount))).setStyle(Style.EMPTY.setItalic(false)));
    }

    public static int getStarCount(ItemStack sword) {
        if (!sword.isEmpty() && sword.hasTag()) {
            CompoundNBT nbt = sword.getTag();
            assert nbt != null;
            if (nbt.contains("Stars")) {
                return nbt.getInt("Stars");
            }
        }
        return 0;
    }

    public static void updatePlayerHealthInNBT(ItemStack item, PlayerEntity player) {
        CompoundNBT nbt = item.getOrCreateTag();
        nbt.putFloat("PlayerMaxHealth", player.getMaxHealth());
        item.setTag(nbt);
    }
}
