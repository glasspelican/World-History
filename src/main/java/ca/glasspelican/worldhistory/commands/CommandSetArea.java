package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.lib.config.Config;
import ca.glasspelican.worldhistory.util.Investigation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandSetArea extends CommandBase {
    @Override
    public String getCommandName() {
        return "setarea";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "setarea. set the start and stop points marking a location to investigate ";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {


    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return !(sender instanceof EntityPlayerMP) || Config.isUserAMod(sender.getName());
    }
}
