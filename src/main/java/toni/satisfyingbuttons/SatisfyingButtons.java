package toni.satisfyingbuttons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import toni.satisfyingbuttons.accessors.IAbstractButtonAccessor;
import toni.satisfyingbuttons.foundation.config.AllConfigs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.CommonColors;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

#if FABRIC
    import net.fabricmc.api.ClientModInitializer;
    import net.fabricmc.api.ModInitializer;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
    import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry;
#endif

#if NEO
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
#endif


#if FORGELIKE
@Mod("satisfying_buttons")
#endif
public class SatisfyingButtons #if FABRIC implements ModInitializer, ClientModInitializer #endif
{
    public static final String MODNAME = "Satisfying Buttons";
    public static final String MODID = "satisfying_buttons";
    public static final Logger LOGGER = LogManager.getLogger(MODNAME);

    #if NEO private static boolean unfrozen = unfreeze(); #endif
	public static final SoundEvent BUTTON_HOVER = Registry.register(BuiltInRegistries.SOUND_EVENT, "button_hover", SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "button_hover")));
	public static final SoundEvent BUTTON_HOVER_REVERSE = Registry.register(BuiltInRegistries.SOUND_EVENT, "button_hover_reverse", SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "button_hover_reverse")));
    #if NEO private static boolean frozen = refreeze(); #endif

    public SatisfyingButtons(#if NEO IEventBus modEventBus, ModContainer modContainer #endif) {
        #if FORGE
        var context = FMLJavaModLoadingContext.get();
        var modEventBus = context.getModEventBus();
        #endif

        #if FORGELIKE
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        AllConfigs.register(modContainer::registerConfig);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        #endif
    }


    #if FABRIC @Override #endif
    public void onInitialize() {

        #if FABRIC
        AllConfigs.register((type, spec) -> {
           NeoForgeConfigRegistry.INSTANCE.register(SatisfyingButtons.MODID, type, spec);
        });
        ConfigScreenFactoryRegistry.INSTANCE.register(SatisfyingButtons.MODID, ConfigurationScreen::new);
        #endif

    }

    #if FABRIC @Override #endif
    public void onInitializeClient() {

    }


    #if NEO
    public static boolean unfreeze() {
        ((MappedRegistry<SoundEvent>) BuiltInRegistries.SOUND_EVENT).unfreeze();
        return true;
    }

    public static boolean refreeze() {
        BuiltInRegistries.SOUND_EVENT.freeze();
        return true;
    }
    #endif

    public static void renderButtonOverlay(GuiGraphics graphics, AbstractButton ths)
    {
        var accessor = (IAbstractButtonAccessor) ths;
        var millis = Util.getMillis();
        var hovertime = accessor.satisfyingButtons$getHoverTime();

        var ticks = millis - hovertime;
        if (ticks <= 0 || !ths.isHovered())
            return;

        if (AllConfigs.client().FadeInVanillaWidgetTexture.get())
        {
            var alpha = Mth.clamp(1f / ((float) AllConfigs.client().FadeInVanillaWidgetTextureOnHoverTime.get() / ticks), 0f, 1f);
            setColor(graphics, 1.0F, 1.0F, 1.0F, alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();

            graphics.blitSprite(AbstractButton.SPRITES.get(ths.active, ths.isHoveredOrFocused()), ths.getX(), ths.getY(), ths.getWidth(), ths.getHeight());

            setColor(graphics, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (AllConfigs.client().DarkenButtonOnHover.get())
        {
            var overlayAlpha = Mth.clamp(1f / ((float) AllConfigs.client().DarkenButtonOnHoverTime.get() / ticks), 0f, 1f);
            var color = AllConfigs.client().DarkenButtonColor.get();
            var lerped = FastColor.ARGB32.color(
                    (int) Mth.lerp(overlayAlpha, 0, FastColor.ARGB32.alpha(color)),
                    FastColor.ARGB32.red(color),
                    FastColor.ARGB32.green(color),
                    FastColor.ARGB32.blue(color));


            graphics.fill(ths.getX(), ths.getY(), ths.getX() + ths.getWidth(), ths.getY() +ths.getHeight(), lerped);
        }
    }



    private static void setColor(GuiGraphics graphics, float v, float v1, float v2, float alpha) { graphics.setColor(v, v1, v2, alpha); }






    // Forg event stubs to call the Fabric initialize methods, and set up cloth config screen
    #if FORGELIKE
    public void commonSetup(FMLCommonSetupEvent event) { onInitialize(); }
    public void clientSetup(FMLClientSetupEvent event) { onInitializeClient(); }
    #endif
}
