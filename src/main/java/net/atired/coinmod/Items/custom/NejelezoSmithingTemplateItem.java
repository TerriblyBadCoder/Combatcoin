package net.atired.coinmod.Items.custom;

import net.atired.coinmod.CombatCoin;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NejelezoSmithingTemplateItem extends SmithingTemplateItem {
    //I AM REALLY STUPID, most of this is referenced from https://github.com/Uraneptus/Sullys-Mod/blob/1.20.x/src/main/java/com/uraneptus/sullysmod/core/other/SMTextUtil.java#L12
    public static Map<String, String> TRANSLATABLES = new HashMap<>();
    private static final Component NEJELEZO_UPGRADE_APPLIES_TO;
    private static final Component NEJELEZO_UPGRADE_INGREDIENTS;
    private static final Component NEJELEZO_UPGRADE_BASE_SLOT_DESCRIPTION;
    private static final Component NEJELEZO_UPGRADE_ADDITIONS_SLOT_DESCRIPTION;
    private static final Component NEJELEZO_UPGRADE;
    private static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_NEJELEZO = CombatCoin.modPrefix("item/empty_nejelezo_ingot");
    public NejelezoSmithingTemplateItem() {
        super(NEJELEZO_UPGRADE_APPLIES_TO, NEJELEZO_UPGRADE_INGREDIENTS, NEJELEZO_UPGRADE, NEJELEZO_UPGRADE_BASE_SLOT_DESCRIPTION, NEJELEZO_UPGRADE_ADDITIONS_SLOT_DESCRIPTION, List.of(EMPTY_SLOT_SWORD), List.of(EMPTY_SLOT_NEJELEZO));
    }
    static {
        NEJELEZO_UPGRADE = Component.translatable(Util.makeDescriptionId("upgrade", CombatCoin.modPrefix("nejelezo_upgrade"))).withStyle(ChatFormatting.GRAY);
        NEJELEZO_UPGRADE_APPLIES_TO = Component.translatable(Util.makeDescriptionId("item", CombatCoin.modPrefix("smithing_template.nejelezo_upgrade.applies_to"))).withStyle(ChatFormatting.BLUE);
        NEJELEZO_UPGRADE_INGREDIENTS = Component.translatable(Util.makeDescriptionId("item", CombatCoin.modPrefix("smithing_template.nejelezo_upgrade.ingredients"))).withStyle(ChatFormatting.BLUE);
        NEJELEZO_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", CombatCoin.modPrefix("smithing_template.nejelezo_upgrade.base_slot_description")));
        NEJELEZO_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(Util.makeDescriptionId("item", CombatCoin.modPrefix("smithing_template.nejelezo_upgrade.additions_slot_description")));
    }


}
