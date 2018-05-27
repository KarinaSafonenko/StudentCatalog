package command.impl;

import command.Command;
import controller.Trigger;
import exception.FailedOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.StudentLogic;
import util.PagePath;
import util.ParameterName;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class ShowIdCommand implements Command {
    private static Logger logger = LogManager.getLogger(ShowCommand.class);
    private static final String ID_SET = "idSet";
    private static final String MESSAGE = "Something went wrong while finding student id set.";
    private StudentLogic studentLogic;

    private enum Type{
        PREPARE_UPDATE, PREPARE_DELETE
    }

    public ShowIdCommand(){
        studentLogic = new StudentLogic();
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        try {
            Set<Integer> idSet = studentLogic.getIdSet();
            request.setAttribute(ID_SET, idSet);
        } catch (FailedOperationException e) {
            logger.catching(e);
            request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), MESSAGE);
            return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
        }
        Type type = Type.valueOf(request.getParameter(ParameterName.COMMAND.name().toLowerCase()).toUpperCase());
        switch (type){
            case PREPARE_DELETE:
                return new Trigger(PagePath.DELETION_PATH, Trigger.TriggerType.FORWARD);
            case PREPARE_UPDATE:
                return new Trigger(PagePath.UPDATE_PATH, Trigger.TriggerType.FORWARD);
                default:
                    return new Trigger(PagePath.INDEX_PATH, Trigger.TriggerType.REDIRECT);
        }
    }
}
