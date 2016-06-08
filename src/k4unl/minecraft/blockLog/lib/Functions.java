package k4unl.minecraft.blockLog.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Functions {

	public static void showMessageInChat(EntityPlayer player, String message){
		player.addChatMessage(new TextComponentString(message));
	}
	
}
