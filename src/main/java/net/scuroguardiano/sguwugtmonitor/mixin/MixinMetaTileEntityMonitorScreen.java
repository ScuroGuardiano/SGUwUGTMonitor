package net.scuroguardiano.sguwugtmonitor.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.scuroguardiano.sguwugtmonitor.api.IMonitorScreenExtensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.sugar.Local;

import gregtech.api.capability.GregtechDataCodes;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.*;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.gui.widget.monitor.WidgetPluginConfig;
import gregtech.common.metatileentities.multi.electric.centralmonitor.MetaTileEntityMonitorScreen;

@Mixin(value = MetaTileEntityMonitorScreen.class, remap = false)
public abstract class MixinMetaTileEntityMonitorScreen extends MetaTileEntity implements IMonitorScreenExtensions {

    private float sguwugtmonitor$offsetX = 0;
    private float sguwugtmonitor$offsetY = 0;
    private float sguwugtmonitor$zIndex = 0;

    public MixinMetaTileEntityMonitorScreen(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
    }

    @Shadow
    protected abstract void writeSync(PacketBuffer buf);

    @ModifyConstant(
                    method = "setConfig",
                    constant = @Constant(floatValue = 8))
    private float sguwugtmonitor$modifyScaleLimit(float original) {
        return 16;
    }

    @Inject(
            method = "createUI",
            at = @At(value = "RETURN"))
    private void sguwugtmonitor$addOffsetControls(EntityPlayer entityPlayer,
                                                  CallbackInfoReturnable<ModularUI> cir,
                                                  @Local(name = "mainGroup") WidgetPluginConfig mainGroup) {
        mainGroup.widget(new LabelWidget(160, 55, "Offset X:", 0xFFFFFFFF))
                .widget(new ClickButtonWidget(205, 50, 20, 20, "-1", d -> {
                    setOffsetX((float) Math.round((sguwugtmonitor$offsetX - (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ClickButtonWidget(275, 50, 20, 20, "+1", d -> {
                    setOffsetX((float) Math.round((sguwugtmonitor$offsetX + (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ImageWidget(225, 50, 50, 20, GuiTextures.DISPLAY))
                .widget(new SimpleTextWidget(250, 60, "", 16777215, () -> Float.toString(sguwugtmonitor$offsetX)));

        mainGroup.widget(new LabelWidget(160, 80, "Offset Y:", 0xFFFFFFFF))
                .widget(new ClickButtonWidget(205, 75, 20, 20, "-1", d -> {
                    setOffsetY((float) Math.round((sguwugtmonitor$offsetY - (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ClickButtonWidget(275, 75, 20, 20, "+1", d -> {
                    setOffsetY((float) Math.round((sguwugtmonitor$offsetY + (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ImageWidget(225, 75, 50, 20, GuiTextures.DISPLAY))
                .widget(new SimpleTextWidget(250, 85, "", 16777215, () -> Float.toString(sguwugtmonitor$offsetY)));

        mainGroup.widget(new LabelWidget(160, 105, "Z-Index:", 0xFFFFFFFF))
                .widget(new ClickButtonWidget(205, 100, 20, 20, "-1", d -> {
                    setZIndex((float) Math.round((sguwugtmonitor$zIndex - (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ClickButtonWidget(275, 100, 20, 20, "+1", d -> {
                    setZIndex((float) Math.round((sguwugtmonitor$zIndex + (d.isShiftClick ? 1.0f : 0.1f)) * 10) / 10);
                }))
                .widget(new ImageWidget(225, 100, 50, 20, GuiTextures.DISPLAY))
                .widget(new SimpleTextWidget(250, 110, "", 16777215, () -> Float.toString(sguwugtmonitor$zIndex)));
    }

    @Inject(method = "writeSync", at = @At("TAIL"))
    private void sguwugtmonitor$writeSync(PacketBuffer buf, CallbackInfo ci) {
        buf.writeFloat(sguwugtmonitor$offsetX);
        buf.writeFloat(sguwugtmonitor$offsetY);
        buf.writeFloat(sguwugtmonitor$zIndex);
    }

    @Inject(
            method = "readSync",
            at = @At(value = "INVOKE",
                     target = "Lgregtech/common/metatileentities/multi/electric/centralmonitor/MetaTileEntityMonitorScreen;updateProxyPlugin()V",
                     shift = At.Shift.BEFORE))
    private void sguwugtmonitor$readSync(PacketBuffer buf, CallbackInfo ci) {
        sguwugtmonitor$offsetX = buf.readFloat();
        sguwugtmonitor$offsetY = buf.readFloat();
        sguwugtmonitor$zIndex = buf.readFloat();
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"))
    public void sguwugtmonitor$writeToNBT(NBTTagCompound data, CallbackInfoReturnable<NBTTagCompound> cir) {
        data.setFloat("offsetX", sguwugtmonitor$offsetX);
        data.setFloat("offsetY", sguwugtmonitor$offsetY);
        data.setFloat("zIndex", sguwugtmonitor$zIndex);
    }

    @Inject(method = "readFromNBT", at = @At("TAIL"))
    public void readFromNBT(NBTTagCompound data, CallbackInfo ci) {
        sguwugtmonitor$offsetX = data.hasKey("offsetX") ? data.getFloat("offsetX") : 0;
        sguwugtmonitor$offsetY = data.hasKey("offsetY") ? data.getFloat("offsetY") : 0;
        sguwugtmonitor$zIndex = data.hasKey("zIndex") ? data.getInteger("zIndex") : 0;
    }

    @Override
    public float getOffsetX() {
        return sguwugtmonitor$offsetX;
    }

    @Override
    public void setOffsetX(float x) {
        sguwugtmonitor$offsetX = x;
        sguwugtmonitor$applyConfig();
    }

    @Override
    public float getOffsetY() {
        return sguwugtmonitor$offsetY;
    }

    @Override
    public void setOffsetY(float y) {
        sguwugtmonitor$offsetY = y;
        sguwugtmonitor$applyConfig();
    }

    @Override
    public float getZIndex() {
        return sguwugtmonitor$zIndex;
    }

    @Override
    public void setZIndex(float z) {
        sguwugtmonitor$zIndex = z;
        sguwugtmonitor$applyConfig();
    }

    private void sguwugtmonitor$applyConfig() {
        writeCustomData(GregtechDataCodes.UPDATE_ALL, this::writeSync);
        markDirty();
    }
}
