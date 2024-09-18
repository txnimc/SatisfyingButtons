package toni.satisfyingbuttons;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.MappedRegistry;

#if FORGE
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
#endif


public class SatisfyingButtonsClient
{

    #if FORGE
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SatisfyingButtons.MODID);

        public static final RegistryObject<SoundEvent> BUTTON_HOVER = SOUNDS.register("button_hover", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(SatisfyingButtons.MODID, "button_hover")));
        public static final RegistryObject<SoundEvent> BUTTON_HOVER_REVERSE = SOUNDS.register("button_hover_reverse", () -> SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(SatisfyingButtons.MODID, "button_hover_reverse")));
    #else
        #if NEO private static boolean unfrozen = unfreeze(); #endif
        public static final SoundEvent BUTTON_HOVER = Registry.register(BuiltInRegistries.SOUND_EVENT, "button_hover", SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(SatisfyingButtons.MODID, "button_hover")));
        public static final SoundEvent BUTTON_HOVER_REVERSE = Registry.register(BuiltInRegistries.SOUND_EVENT, "button_hover_reverse", SoundEvent.createVariableRangeEvent(#if AFTER_21_1 ResourceLocation.fromNamespaceAndPath #else new ResourceLocation #endif(SatisfyingButtons.MODID, "button_hover_reverse")));
        #if NEO private static boolean frozen = refreeze(); #endif
    #endif

    public static void init() {

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
}
