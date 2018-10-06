package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.lib.config.Config;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;

public abstract class ModCommandBase extends CommandBase {

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        boolean serverOP, configOP;
        UserListOpsEntry userListOpsEntry;
        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();


        if (player != null) {
            userListOpsEntry = server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
            if (userListOpsEntry != null) {
                serverOP = userListOpsEntry.getPermissionLevel() >= server.getOpPermissionLevel();
            } else {
                serverOP = false;
            }
        } else {
            serverOP = false;
        }

        configOP = Config.isUserAMod(sender.getName());

        return (configOP || serverOP);
    }
}
