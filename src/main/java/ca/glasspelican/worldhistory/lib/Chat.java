package ca.glasspelican.worldhistory.lib;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class Chat {

    public static void showMessage(EntityPlayer player, String message) {
        player.sendMessage(new TextComponentString(message));
    }

    public static void showMessage(EntityPlayer player, TextComponentString string) {
        player.sendMessage(string);
    }

    public static void showMessage(ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

}
