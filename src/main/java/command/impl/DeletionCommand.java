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

public class DeletionCommand implements Command {
    private static Logger logger = LogManager.getLogger(ShowCommand.class);
    private static final String SUCCESS_MESSAGE = "Student has been successfully deleted.";
    private static final String FAIL_MESSAGE = "Something went wrong while deleting student.";
    private StudentLogic studentLogic;

    public DeletionCommand(){
        studentLogic = new StudentLogic();
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        String id = request.getParameter(ParameterName.ID.name().toLowerCase());
        int studentId = Integer.parseInt(id);
        try {
            studentLogic.removeStudent(studentId);
            request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), SUCCESS_MESSAGE);
        } catch (FailedOperationException e) {
            logger.catching(e);
            request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), FAIL_MESSAGE);
            return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
        }
        return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
    }
}
