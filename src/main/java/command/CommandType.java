package command;

import command.impl.*;

public enum CommandType {
    SHOW(new ShowCommand()),
    ADD(new ChangeCommand()),
    UPDATE(new ChangeCommand()),
    PREPARE_DELETE(new ShowIdCommand()),
    PREPARE_UPDATE(new ShowIdCommand()),
    DELETE(new DeletionCommand()),
    VK(new VKCommand()),
    GOOGLE(new GoogleCommand()),
    GOOGLE_FINISHED(new FinishGoogleCommand()),
    LOGOUT(new LogoutCommand());

    private Command command;

    CommandType(Command command){
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
