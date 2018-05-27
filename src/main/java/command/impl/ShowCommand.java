package command.impl;

import command.Command;
import controller.Trigger;
import entity.Student;
import exception.FailedOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.StudentLogic;
import util.PagePath;
import util.ParameterName;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class ShowCommand implements Command {
    private static Logger logger = LogManager.getLogger(ShowCommand.class);
    private static final String STUDENT_SET = "studentSet";
    private static final String MESSAGE = "Something went wrong while finding student set.";
    private StudentLogic studentLogic;

    public ShowCommand(){
        studentLogic = new StudentLogic();
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        try {
            Set<Student> students = studentLogic.getStudentSet();
            request.setAttribute(STUDENT_SET, students);
        } catch (FailedOperationException e) {
            logger.catching(e);
            request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), MESSAGE);
            return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
        }
        return new Trigger(PagePath.RESULT_PATH, Trigger.TriggerType.FORWARD);
    }

}
