package toni.satisfyingbuttons.mixin;

import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toni.satisfyingbuttons.SatisfyingButtons;
import toni.satisfyingbuttons.accessors.IAbstractButtonAccessor;
import toni.satisfyingbuttons.foundation.config.AllConfigs;

#if AFTER_21_1
import net.minecraft.client.gui.components.WidgetSprites;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
#else
import net.minecraft.client.gui.components.AbstractWidget;
#endif

@Mixin(value = ExtendedButton.class, remap = true)
public class FancyMenuButtonMixin implements IAbstractButtonAccessor
{
    @Unique
    private boolean satisfying_buttons$wasHovered;

    @Unique
    private long satisfying_buttons$hoverOrFocusedStartTime;

    @Override
    public long satisfyingButtons$getHoverTime() { return satisfying_buttons$hoverOrFocusedStartTime; }

    @Inject(at = @At("RETURN"), method = "renderWidget", remap = true)
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {
        var ths = (ExtendedButton) (Object) this;

        var isHovered = ths.isHovered();
        if (!ths.active)
            return;

        if (isHovered && !satisfying_buttons$wasHovered)
        {
            if (AllConfigs.client().SoundEnabled.get())
            {
                // play hover sound
                var handler = Minecraft.getInstance().getSoundManager();
                handler.play(SimpleSoundInstance.forUI(SatisfyingButtons.BUTTON_HOVER #if FORGE .get() #endif, AllConfigs.client().ButtonSoundPitch.getF(), AllConfigs.client().ButtonSoundVolume.getF()));
            }

            satisfying_buttons$hoverOrFocusedStartTime = Util.getMillis();
        }

        if (!isHovered && satisfying_buttons$wasHovered)
        {
            if (AllConfigs.client().SoundEnabled.get() && AllConfigs.client().ButtonUnhoverSoundEnabled.get())
            {
                // play unhover sound
                var handler = Minecraft.getInstance().getSoundManager();
                handler.play(SimpleSoundInstance.forUI(SatisfyingButtons.BUTTON_HOVER_REVERSE #if FORGE .get() #endif, AllConfigs.client().ButtonSoundPitch.getF(), AllConfigs.client().ButtonSoundVolume.getF()));
            }

            satisfying_buttons$hoverOrFocusedStartTime = 0;
        }

        var custom = ths.getExtendedAsCustomizableWidget();
        if (custom.getCustomBackgroundNormalFancyMenu() != null || custom.isNineSliceCustomBackgroundTexture_FancyMenu() || ths.getBackgroundColorNormal() != null)
        {
            satisfying_buttons$wasHovered = isHovered;
            return;
        }

        if (AllConfigs.client().AnimationEnabled.get())
        {
            SatisfyingButtons.renderButtonOverlay(graphics, ths);
        }

        satisfying_buttons$wasHovered = isHovered;
    }

    #if AFTER_21_1
    @WrapOperation(at = @At(target = "Lnet/minecraft/client/gui/components/WidgetSprites;get(ZZ)Lnet/minecraft/resources/ResourceLocation;", value = "INVOKE"), method = "renderBackground")
    public ResourceLocation render(WidgetSprites instance, boolean enabled, boolean focused, Operation<ResourceLocation> original)
    {
        if (AllConfigs.client().FadeInVanillaWidgetTexture.get())
        {
            return instance.get(enabled, false);
        }

        return original.call(instance, enabled, focused);
    }
    #else

    #if FORGE
    @Inject(at = @At("HEAD"), method = "m_274533_", cancellable = true, remap = false)
    #else
    @Inject(at = @At("HEAD"), method = "getTextureY", cancellable = true, remap = false)
    #endif
    public void getTextureY(CallbackInfoReturnable<Integer> cir)
    {
        var ths = (ExtendedButton) (Object) this;
        var custom = ths.getExtendedAsCustomizableWidget();
        if (custom.getCustomBackgroundNormalFancyMenu() != null || custom.isNineSliceCustomBackgroundTexture_FancyMenu() || ths.getBackgroundColorNormal() != null)
            return;

        // disable switching hovered vanilla texture immediately --- handle in renderOverlay
        if (AllConfigs.client().FadeInVanillaWidgetTexture.get())
        {
            int i = ths.active ? 1 : 0;
            cir.setReturnValue(46 + i * 20);
        }
    }
    #endif
}