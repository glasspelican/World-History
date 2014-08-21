package k4unl.minecraft.blockLog;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import k4unl.minecraft.blockLog.commands.Commands;
import k4unl.minecraft.blockLog.events.EventHelper;
import k4unl.minecraft.blockLog.lib.Log;
import k4unl.minecraft.blockLog.lib.MySQL;
import k4unl.minecraft.blockLog.lib.config.Config;
import k4unl.minecraft.blockLog.lib.config.ConfigHandler;
import k4unl.minecraft.blockLog.lib.config.ModInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Mod(
	modid = ModInfo.ID,
	name = ModInfo.NAME,
	version = ModInfo.VERSION,
	acceptableRemoteVersions="*"
)


public class BlockLog {
    @Instance(value = ModInfo.ID)
    public static BlockLog instance;

    public static MySQL sqlConn;
    private static List<String> doNotLogList = new ArrayList<String>();
    private static List<String> helpers = new ArrayList<String>();

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
    public void serverStart(FMLServerStartingEvent event) throws SQLException {
        sqlConn = new MySQL(Config.getString("host"), Config.getString("username"), Config.getString("password"), Config.getString("database"));
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

	}

    public void addUserToNotLogList(String name){
        if(!doNotLogList.contains(name)){
            doNotLogList.add(name);
        }
    }

    public boolean isUserOnNotLogList(String displayName) {

        return doNotLogList.contains(displayName);
    }

    public void removeUserFromNotLogList(String name){
        if(doNotLogList.contains(name)){
            doNotLogList.remove(name);
        }
    }
}
