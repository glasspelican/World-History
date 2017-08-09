package ca.glasspelican.worldhistory.commands;

import ca.glasspelican.worldhistory.lib.Log;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.lang.reflect.InvocationTargetException;

public enum Commands {
    COMMAND_SHOW_ACCESS(CommandShowAccess.class),
    COMMAND_SET_AREA(CommandSetArea.class),
    COMMAND_SET_POINT(CommandSetTwo.class),;

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
                    iCommand = commandClass.getConstructor(commandClass).newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    Log.error(e);
                }
                event.registerServerCommand(iCommand);
            }
        }
    }

    public Class<? extends CommandBase> getCommandClass() {
        return commandClass;
    }
}
