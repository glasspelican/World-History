package ca.glasspelican.worldhistory.lib;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Chat {

    public static void showMessage(EntityPlayer player, String message) {
        player.addChatMessage(new TextComponentString(message));
    }

    public static void showMessage(EntityPlayer player, TextComponentString string) {
        player.addChatMessage(string);
    }

    public static void showMessage(ICommandSender sender, String message) {
        sender.addChatMessage(new TextComponentString(message));
    }

}
