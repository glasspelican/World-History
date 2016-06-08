package k4unl.minecraft.blockLog.commands;

import k4unl.minecraft.blockLog.BlockLog;
import k4unl.minecraft.blockLog.lib.config.Config;
import k4unl.minecraft.k4lib.commands.CommandOpOnly;
import k4unl.minecraft.k4lib.lib.Functions;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;

public class CommandShowAccess extends CommandOpOnly {

    private List<String> aliases;
    public CommandShowAccess(){
        aliases = new ArrayList<String>();
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

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int entries = 10;
        if(args.length > 0){
            entries = Integer.parseInt(args[0]);
        }
        BlockLog.instance.addUserToNotLogList(sender.getName(), entries);
        sender.addChatMessage(new TextComponentString("You can now right click on a block that you want info about"));
    }

}
