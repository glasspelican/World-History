package ca.glasspelican.worldhistory;


import ca.glasspelican.worldhistory.commands.Commands;
import ca.glasspelican.worldhistory.events.EventHelper;
import ca.glasspelican.worldhistory.lib.Log;
import ca.glasspelican.worldhistory.lib.MySQL;
import ca.glasspelican.worldhistory.lib.config.Config;
import ca.glasspelican.worldhistory.lib.config.ConfigHandler;
import ca.glasspelican.worldhistory.lib.config.ModInfo;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Mod(
        modid = ModInfo.ID,
        name = ModInfo.NAME,
        version = ModInfo.VERSION,
        acceptableRemoteVersions = "*"
)


public class WorldHistory {
    @Instance(value = ModInfo.ID)
    public static WorldHistory instance;
    private static MySQL sqlConn;
    private static Map<String, Integer> doNotLogList = new HashMap<String, Integer>();

    public static MySQL getSqlConn() {
        return sqlConn;
    }

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

    public void addUserToNotLogList(String name, int entries) {

        if (!doNotLogList.containsKey(name)) {
            doNotLogList.put(name, entries);
        }
    }

    public int isUserOnNotLogList(String displayName) {
        if (doNotLogList.containsKey(displayName)) {
            return doNotLogList.get(displayName);
        } else {
            return 0;
        }
    }

    public void removeUserFromNotLogList(String name) {
        if (doNotLogList.containsKey(name)) {
            doNotLogList.remove(name);
        }
    }
}
