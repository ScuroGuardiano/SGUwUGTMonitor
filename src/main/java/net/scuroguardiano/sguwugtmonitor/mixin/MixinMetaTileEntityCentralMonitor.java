package net.scuroguardiano.sguwugtmonitor.mixin;

import java.util.List;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.AdvancedTextWidget;
import gregtech.common.metatileentities.multi.electric.centralmonitor.MetaTileEntityCentralMonitor;

@Mixin(value = MetaTileEntityCentralMonitor.class, remap = false)
public class MixinMetaTileEntityCentralMonitor {

    @Inject(method = "addDisplayText", at = @At("TAIL"))
    private void sguwugtmonitor$addTestLine(List<ITextComponent> textList, CallbackInfo ci) {
        ITextComponent line = new TextComponentString("[SGUwUGTMonitor] ");
        line.appendSibling(AdvancedTextWidget.withButton(new TextComponentString("[Kliknij mnie]"), "sguwu_test"));
        textList.add(line);
    }

    @Inject(method = "handleDisplayClick", at = @At("HEAD"))
    private void sguwugtmonitor$onTestClick(String componentData, Widget.ClickData clickData, CallbackInfo ci) {
        if ("sguwu_test".equals(componentData)) {
            System.out.println("[SGUwUGTMonitor] Działa!");
        }
    }
}
