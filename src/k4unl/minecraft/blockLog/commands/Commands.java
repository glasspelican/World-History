package k4unl.minecraft.blockLog.commands;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class Commands {

	
	public static void init(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandShowAccess());
	}
}
