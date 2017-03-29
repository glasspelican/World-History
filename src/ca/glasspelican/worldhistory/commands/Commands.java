package ca.glasspelican.worldhistory.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class Commands {

	
	public static void init(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandShowAccess());
	}
}
