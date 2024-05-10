package me.spacety.hyperionforge.item.custom;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ItemTier;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static me.spacety.hyperionforge.item.custom.HyperionUpgradeHandler.getStarCount;

public class HyperionItem extends SwordItem {
    public HyperionItem(ItemTier tier ,int pAttackDamageIn, float pAttackSpeedIn, Properties pPropertiesIn) {
        super(tier ,pAttackDamageIn, pAttackSpeedIn, pPropertiesIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new StringTextComponent("                "));
        boolean isPlayerSet = stack.getTag() != null && stack.getTag().contains("PlayerMaxHealth");

        float maxHealth = stack.getOrCreateTag().getFloat("PlayerMaxHealth");
        int stars = getStarCount(stack);
        float calculatedDamage = maxHealth / (9 - stars);

        if (isPlayerSet) {
            tooltip.add(convertColorCodes("&aScroll Abilities:"));
            tooltip.add(convertColorCodes("&6Item Ability: Wither Impact &e&lRIGHT CLICK"));
            tooltip.add(convertColorCodes("&7Teleport &a10 blocks &7ahead of"));
            tooltip.add(convertColorCodes("&7you. Then implode dealing"));
            tooltip.add(convertColorCodes("&c" + String.format("%.2f", calculatedDamage) + " damage&7 within a range of &a10 blocks &7to all enemies."));
        } else {
            tooltip.add(convertColorCodes("&aScroll Abilities:"));
            tooltip.add(convertColorCodes("&6Use To Discover &d&lAbility"));
        }
        tooltip.add(convertColorCodes("   "));
    }

    public static StringTextComponent convertColorCodes(String message) {
        message = message.replace("&", "§");
        StringBuilder formattedMessage = new StringBuilder();
        StringTextComponent component = new StringTextComponent("");

        Style currentStyle = Style.EMPTY;

        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '§') {
                if (i + 1 < message.length()) {
                    char code = message.charAt(i + 1);
                    TextFormatting format = TextFormatting.fromFormattingCode(code);
                    if (format != null) {
                        if (!formattedMessage.isEmpty()) {
                            component.appendSibling(new StringTextComponent(formattedMessage.toString()).setStyle(currentStyle));
                            formattedMessage = new StringBuilder();
                        }
                        currentStyle = Style.EMPTY.applyFormatting(format);
                        i++;
                        continue;
                    }
                }
            }
            formattedMessage.append(c);
        }

        if (!formattedMessage.isEmpty()) {
            component.appendSibling(new StringTextComponent(formattedMessage.toString()).setStyle(currentStyle));
        }

        return component;
    }
}
