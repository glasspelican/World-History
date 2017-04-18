package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.Chat;
import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class CommandShowAccess extends CommandBase {

    private List<String> aliases;

    public CommandShowAccess() {
        aliases = new ArrayList<>();
        aliases.add("sa");
    }

    @Override
    public String getCommandName() {

        return "showaccess";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "showaccess. Then right click with any tool on a block or TE to get the last 10 events on that location.";
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    /**
     * Callback for when the command is executed
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int entries = 10;
        if (args.length > 0) {
            entries = Integer.parseInt(args[0]);
        }
        WorldHistory.instance.addUserToNotLogList(sender.getName(), entries);
        Chat.showMessage(sender,"You can now right click on a block that you want info about");
    }

    /**
     * Check if the given ICommandSender has permission to execute this command
     *
     * @param server
     * @param sender
     */
    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return !(sender instanceof EntityPlayerMP) || Config.isUserAMod(sender.getName());
    }
}
