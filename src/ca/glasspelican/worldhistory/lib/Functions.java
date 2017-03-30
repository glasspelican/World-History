package ca.glasspelican.worldhistory.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class Functions {

    public static void showMessageInChat(EntityPlayer player, String message) {
        player.addChatMessage(new ChatComponentText(message));
    }

}
