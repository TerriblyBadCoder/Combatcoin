package net.atired.coinmod.client.render;

import net.atired.coinmod.CombatCoin;
import net.atired.coinmod.Items.custom.ChainedSawItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ChainedSawHudOverlay {

    private static final ResourceLocation HEATTEST2 = new ResourceLocation(CombatCoin.MODID, "textures/gui/heattest2.png");
    private static final ResourceLocation HEATTEST1 = new ResourceLocation(CombatCoin.MODID, "textures/gui/heattest1.png");
    private static GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance().gameRenderer.getMinecraft(), Minecraft.getInstance().renderBuffers().bufferSource());
    public static final IGuiOverlay HUD_CHARGE = ((gui, poseStack, partialTick, width, height) -> {
        if (Minecraft.getInstance().player != null) {
            Player player = Minecraft.getInstance().player;
            ItemStack itemStack = player.getMainHandItem();
            if(!(itemStack.getItem() instanceof ChainedSawItem))
            {
                itemStack = player.getOffhandItem();
            }
            if(itemStack.getItem() instanceof ChainedSawItem)
            {
                int x = width / 2;
                int y = height-39;
                int charge = ((CompoundTag)itemStack.serializeNBT().get("tag")).getInt("coinmod_heatpower");
                if(charge>40)
                {
                    guiGraphics.setColor(1,0.7F,0.6F,1);
                }
                guiGraphics.blit(HEATTEST1,x-22,y-10,0,0,44,6,44,6);
                guiGraphics.blit(HEATTEST2,x-(int)(charge/2*0.36),y-8,0,Minecraft.getInstance().player.tickCount%24,(int)(charge*0.36),2,24,24);
            }


        }
    });

}
