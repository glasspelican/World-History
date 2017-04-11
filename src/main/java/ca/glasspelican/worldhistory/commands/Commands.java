package ca.glasspelican.worldhistory.commands;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class Commands {


    public static void init(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandShowAccess());
    }
}
