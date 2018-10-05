package ca.glasspelican.worldhistory;


import ca.glasspelican.worldhistory.commands.Commands;
import ca.glasspelican.worldhistory.events.EventHelper;
import ca.glasspelican.worldhistory.lib.Database;
import ca.glasspelican.worldhistory.lib.Log;
import ca.glasspelican.worldhistory.lib.config.Config;
import ca.glasspelican.worldhistory.lib.config.ConfigHandler;
import ca.glasspelican.worldhistory.lib.config.ModInfo;
import ca.glasspelican.worldhistory.util.Investigation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

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
    private static Database sqlConn;
    private static Map<String, Integer> doNotLogList = new HashMap<>();

    public static Investigation investigation;

    public static Database getSqlConn() {
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
    public static void serverStart(FMLServerStartingEvent event) throws SQLException {
        Commands.init(event);

        if (Config.getBool("useEmbeddedDatabase")) {
            sqlConn = new Database(event.getServer().getDataDirectory().getAbsolutePath() + "/world/", ModInfo.ID);
        } else {
            sqlConn = new Database(Config.getString("host"), Config.getString("username"), Config.getString("password"), Config.getString("database"));
        }
    }

    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {
        sqlConn.close();
    }

    public void addUserToNotLogList(String name, int entries) {

        if (!doNotLogList.containsKey(name)) {
            doNotLogList.put(name, entries);
        }
    }

    public int isUserOnNotLogList(String displayName) {
        return doNotLogList.getOrDefault(displayName, 0);
    }

    public void removeUserFromNotLogList(String name) {
        doNotLogList.remove(name);
    }
}
