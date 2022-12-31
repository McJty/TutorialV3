package com.example.tutorialv3.setup;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.entities.ThiefEntity;
import com.example.tutorialv3.manasystem.data.ManaEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = TutorialV3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {

    public static final String TAB_NAME = "tutorialv3";

    public static void setup() {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(Entity.class, ManaEvents::onAttachCapabilitiesPlayer);
        bus.addListener(ManaEvents::onPlayerCloned);
        bus.addListener(ManaEvents::onRegisterCapabilities);
        bus.addListener(ManaEvents::onWorldTick);
    }

    public static void init(FMLCommonSetupEvent event) {
        Messages.register();
    }

    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(Registration.THIEF.get(), ThiefEntity.prepareAttributes().build());
    }

    @SubscribeEvent
    public static void onCustomTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(TutorialV3.MODID, "tutorial"), builder -> {
            builder.title(Component.translatable("itemGroup." + TAB_NAME))
                    .icon(() -> new ItemStack(Registration.MYSTERIOUS_INGOT.get()))
                    .displayItems((enabledFeatures, output, tab) -> {
                        output.accept(Registration.MYSTERIOUS_ORE_OVERWORLD.get());
                        output.accept(Registration.MYSTERIOUS_ORE_NETHER.get());
                        output.accept(Registration.MYSTERIOUS_ORE_END.get());
                        output.accept(Registration.PORTAL_BLOCK.get());
                        output.accept(Registration.THIEF_EGG.get());
                        output.accept(Registration.POWERGEN.get());
                        output.accept(Registration.GENERATOR.get());
                        output.accept(Registration.MYSTERIOUS_INGOT.get());
                        output.accept(Registration.RAW_MYSTERIOUS_CHUNK.get());
                    });
        });
    }
}
