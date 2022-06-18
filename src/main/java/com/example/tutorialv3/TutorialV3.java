package com.example.tutorialv3;

import com.example.tutorialv3.setup.Config;
import com.example.tutorialv3.setup.ModSetup;
import com.example.tutorialv3.setup.ClientSetup;
import com.example.tutorialv3.setup.Registration;

//import com.example.tutorialv3.worldgen.ores.Ores;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TutorialV3.MODID)
public class TutorialV3 {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "tutorialv3";

    public TutorialV3() {

        // Register the deferred registry
        ModSetup.setup();
        Registration.init();
        Config.register();

        // Register the setup method for modloading
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));

        // Register our hit-like-a-girl handler
        MinecraftForge.EVENT_BUS.addListener(TutorialV3::HitLikeAGirlHandler);
    }
    public static void HitLikeAGirlHandler(BreakEvent ev) {
        LOGGER.info("Yes, you hit like a girl!");
    }
}
