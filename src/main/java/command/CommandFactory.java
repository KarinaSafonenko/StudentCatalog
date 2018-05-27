package command;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CommandFactory {
    private static Logger logger = LogManager.getLogger();

    public Optional<Command> defineCommand(String commandType) {
        Optional<Command> command = Optional.empty();
        if (commandType == null){
            return command;
        }
        try {
            CommandType type = CommandType.valueOf(commandType.toUpperCase());
            command = Optional.of(type.getCommand());
        }catch (IllegalArgumentException e){
            logger.catching(Level.WARN, e);
        }
        return command;
    }
}
