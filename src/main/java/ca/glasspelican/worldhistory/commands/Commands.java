package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.lib.Log;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public enum Commands {
    COMMAND_SHOW_ACCESS(CommandShowAccess.class),
    COMMAND_SET_AREA(CommandSetArea.class),
    COMMAND_SET_POINT(CommandSetTwo.class),
    COMMAND_QUERY(CommandQuery.class);

    private final Class<? extends CommandBase> commandClass;

    Commands(Class<? extends CommandBase> aClass) {
        this.commandClass = aClass;
    }

    public static void init(FMLServerStartingEvent event) {
        if (event.getSide().isServer()) {
            for (Commands commands : Commands.values()) {
                Class<? extends CommandBase> commandClass = commands.getCommandClass();
                ICommand iCommand = null;
                try {
                    iCommand = commandClass.newInstance();
                    event.registerServerCommand(iCommand);
                } catch (InstantiationException | IllegalAccessException e) {
                    Log.error(e);
                }
            }
        }
    }

    public Class<? extends CommandBase> getCommandClass() {
        return commandClass;
    }
}
