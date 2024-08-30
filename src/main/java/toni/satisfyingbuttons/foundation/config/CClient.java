package toni.satisfyingbuttons.foundation.config;

public class CClient extends ConfigBase {
    public ConfigBool AnimationEnabled = b(true, "AnimationEnabled", "Whether button hover animations are enabled or not. Turning this off toggles off all graphics changes.");
    public ConfigBool DarkenButtonOnHover = b(true, "DarkenButtonOnHover", "Whether buttons should have a darkened overlay.");

    public ConfigInt DarkenButtonOnHoverTime = i(300, "DarkenButtonOnHoverTime", "Time in milliseconds for hover fade.");
    public ConfigInt DarkenButtonColor = i(1610612736, "DarkenButtonColor", "ARGB packed color (default: 1610612736 (0,0,0,96))");

    public ConfigBool FadeInVanillaWidgetTexture = b(true, "FadeInVanillaWidgetTexture", "Whether the vanilla widget texture (white border by default) should fade in instead of applying immediately.");
    public ConfigInt FadeInVanillaWidgetTextureOnHoverTime = i(300, "FadeInVanillaWidgetTextureOnHoverTime", "Time in milliseconds for hover fade.");

    public ConfigBool SoundEnabled = b(true, "SoundEnabled", "Whether button sounds are enabled or not. Turning this off toggles off all audio changes.");
    public ConfigBool ButtonUnhoverSoundEnabled = b(true, "ButtonUnhoverSoundEnabled", "Whether a secondary, quieter sound should play when a button is unhovered..");

    public ConfigFloat ButtonSoundVolume = f(0.5f, 0f, 1f, "ButtonSoundVolume", "Button sound Volume (range 0f - 1f).");
    public ConfigFloat ButtonSoundPitch = f(1f, 0f, "ButtonSoundPitch", "Button sound pitch (Default 1f)");


    @Override
    public String getName() {
        return "client";
    }
}
