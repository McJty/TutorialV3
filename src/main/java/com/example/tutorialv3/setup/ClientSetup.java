package com.example.tutorialv3.setup;

import com.example.tutorialv3.TutorialV3;
import com.example.tutorialv3.client.GeneratorModelLoader;
import com.example.tutorialv3.client.PowergenRenderer;
import com.example.tutorialv3.client.PowergenScreen;
import com.example.tutorialv3.entities.ThiefModel;
import com.example.tutorialv3.entities.ThiefRenderer;
import com.example.tutorialv3.manasystem.client.KeyBindings;
import com.example.tutorialv3.manasystem.client.KeyInputHandler;
import com.example.tutorialv3.manasystem.client.ManaOverlay;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TutorialV3.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.POWERGEN_CONTAINER.get(), PowergenScreen::new);
            // @todo move to json
//            ItemBlockRenderTypes.setRenderLayer(Registration.POWERGEN.get(), RenderType.translucent());
            PowergenRenderer.register();
        });
        MinecraftForge.EVENT_BUS.addListener(KeyInputHandler::onKeyInput);
//        GuiOverlayManager.registerOverlayAbove(ForgeIngameGui.HOTBAR_ELEMENT, "name", ManaOverlay.HUD_MANA);
    }

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "name", ManaOverlay.HUD_MANA);
    }
    
    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(GeneratorModelLoader.GENERATOR_LOADER.toString(), new GeneratorModelLoader());
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThiefModel.THIEF_LAYER, ThiefModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(Registration.THIEF.get(), ThiefRenderer::new);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }
        event.addSprite(PowergenRenderer.HALO);
    }
}
