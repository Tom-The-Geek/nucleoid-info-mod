package com.github.tom_the_geek.nim.mixin;

import com.github.tom_the_geek.nim.NucleoidInfoMod;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen {
    @Inject(method = "init",
            at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        NucleoidInfoMod.refresh();
    }
}
