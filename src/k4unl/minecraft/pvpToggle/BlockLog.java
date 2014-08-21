package k4unl.minecraft.pvpToggle;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import k4unl.minecraft.pvpToggle.commands.Commands;
import k4unl.minecraft.pvpToggle.events.EventHelper;
import k4unl.minecraft.pvpToggle.lib.Log;
import k4unl.minecraft.pvpToggle.lib.config.ConfigHandler;
import k4unl.minecraft.pvpToggle.lib.config.ModInfo;
import net.minecraftforge.common.DimensionManager;


@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	acceptableRemoteVersions="*"
)


public class BlockLog {
    @Instance(value = ModInfo.ID)
    public static BlockLog instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Log.init();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {

        EventHelper.init();
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @EventHandler
    public void onServerStart(FMLServerStartingEvent event) {

        Commands.init(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        Users.readFromFile(DimensionManager.getCurrentSaveRootDirectory());
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

        Users.saveToFile(DimensionManager.getCurrentSaveRootDirectory());
	}
}
