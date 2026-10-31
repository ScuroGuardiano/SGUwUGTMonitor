package net.scuroguardiano.sguwugtmonitor.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.RayTraceResult;
import net.scuroguardiano.sguwugtmonitor.api.IMonitorScreenExtensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.metatileentities.multi.electric.centralmonitor.MetaTileEntityMonitorScreen;

@Mixin(value = MetaTileEntityMonitorScreen.class, remap = false)
public class MixinMetaTileEntityMonitorScreenClient {

    @Inject(
            method = "renderScreen",
            at = @At(
                     value = "INVOKE",
                     target = "Lnet/minecraft/client/renderer/GlStateManager;scale(FFF)V",
                     shift = At.Shift.BEFORE))
    private void sguwugtmonitor$renderScreen(float partialTicks, RayTraceResult rayTraceResult, CallbackInfo ci) {
        IMonitorScreenExtensions self = (IMonitorScreenExtensions) this;
        GlStateManager.translate(self.getOffsetX(), self.getOffsetY(), 0);
    }
}
