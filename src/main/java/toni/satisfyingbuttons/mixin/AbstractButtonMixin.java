package toni.satisfyingbuttons.mixin;
 
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import toni.satisfyingbuttons.accessors.IAbstractButtonAccessor;
import toni.satisfyingbuttons.SatisfyingButtons;
import toni.satisfyingbuttons.foundation.config.AllConfigs;

@Mixin(AbstractButton.class)
public class AbstractButtonMixin implements IAbstractButtonAccessor
{
    @Unique
    private boolean satisfying_buttons$wasHovered;

    @Unique
    private long satisfying_buttons$hoverOrFocusedStartTime;

    @Override
    public long satisfyingButtons$getHoverTime() { return satisfying_buttons$hoverOrFocusedStartTime; }
 
    @Inject(at = @At("RETURN"), method = "renderWidget")
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci)
    {   
        var ths = (AbstractButton) (Object) this;
        var isHovered = ths.isHovered(); 
        if (!ths.active)
            return;

        if (isHovered && !satisfying_buttons$wasHovered)
        {
            if (AllConfigs.client().SoundEnabled.get())
            {
                // play hover sound
                var handler = Minecraft.getInstance().getSoundManager();
                handler.play(SimpleSoundInstance.forUI(SatisfyingButtons.BUTTON_HOVER, AllConfigs.client().ButtonSoundPitch.getF(), AllConfigs.client().ButtonSoundVolume.getF()));
            }

            satisfying_buttons$hoverOrFocusedStartTime = Util.getMillis();
        }

        if (!isHovered && satisfying_buttons$wasHovered)
        {
            if (AllConfigs.client().SoundEnabled.get() && AllConfigs.client().ButtonUnhoverSoundEnabled.get())
            {
                // play unhover sound
                var handler = Minecraft.getInstance().getSoundManager();
                handler.play(SimpleSoundInstance.forUI(SatisfyingButtons.BUTTON_HOVER_REVERSE, AllConfigs.client().ButtonSoundPitch.getF(), AllConfigs.client().ButtonSoundVolume.getF()));
            }

            satisfying_buttons$hoverOrFocusedStartTime = 0;
        }

        if (AllConfigs.client().AnimationEnabled.get())
            SatisfyingButtons.renderButtonOverlay(graphics, ths);

        satisfying_buttons$wasHovered = isHovered;
    }

    @WrapOperation(at = @At(target = "Lnet/minecraft/client/gui/components/WidgetSprites;get(ZZ)Lnet/minecraft/resources/ResourceLocation;", value = "INVOKE"), method = "renderWidget")
    public ResourceLocation render(WidgetSprites instance, boolean enabled, boolean focused, Operation<ResourceLocation> original)
    {
        if (AllConfigs.client().FadeInVanillaWidgetTexture.get())
        {
            return instance.get(enabled, false);
        }

        return original.call(instance, enabled, focused);
    }
//    @Inject(at = @At("HEAD"), method = "getTextureY", cancellable = true)
//    public void getTextureY(CallbackInfoReturnable<Integer> cir)
//    {
//        // disable switching hovered vanilla texture immediately --- handle in renderOverlay
//        if (AllConfigs.client().FadeInVanillaWidgetTexture)
//        {
//            var ths = (AbstractWidget) (Object) this;
//            int i = ths.active ? 1 : 0;
//            cir.setReturnValue(#if PRE_CURRENT_MC_1_19_2 i #elif POST_CURRENT_MC_1_20_1 46 + i * 20 #endif);
//        }
//    }
}