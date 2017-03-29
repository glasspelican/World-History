package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.WorldHistory;
import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class CommandShowAccess implements ICommand {

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
    public void processCommand(ICommandSender sender, String[] args) {
        int entries = 10;
        if(args.length > 0){
            entries = Integer.parseInt(args[0]);
        }
        WorldHistory.instance.addUserToNotLogList(sender.getCommandSenderName(), entries);
        sender.addChatMessage(new ChatComponentText("You can now right click on a block that you want info about"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender){
        if(sender instanceof EntityPlayerMP){
            if(Config.isUserAMod(((EntityPlayerMP) sender).getDisplayName())){
                return true;
            }
            return MinecraftServer.getServer().getConfigurationManager().func_152596_g(((EntityPlayerMP) sender).getGameProfile());
        } else {
            return true;
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {

        return false;
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }
}
