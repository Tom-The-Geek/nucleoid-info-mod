package com.github.tom_the_geek.nim.mixin;

import com.github.tom_the_geek.nim.NucleoidInfoMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class MixinServerEntry extends MultiplayerServerListWidget.Entry {
    @Shadow @Final private ServerInfo server;

    @Shadow @Final private MultiplayerScreen screen;

    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if ("nucleoid.xyz".equals(this.server.address) || "play.nucleoid.xyz".equals(this.server.address)) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            MinecraftClient.getInstance().getTextureManager().bindTexture(NucleoidInfoMod.ICONS_TEXTURE);
            DrawableHelper.drawTexture(matrices, x + entryWidth - 15, y + 10, 10, 10, 0, 0, 128, 128, 256, 256);
            int relMouseX = mouseX - x;
            int relMouseY = mouseY - y;
            if (relMouseX >= entryWidth - 15 && relMouseX <= entryWidth - 9 && relMouseY >= 10 && relMouseY <= 20) {
                this.screen.setTooltip(NucleoidInfoMod.getTooltip());
            }
        }
    }
}
