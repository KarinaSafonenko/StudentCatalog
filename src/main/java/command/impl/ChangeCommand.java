package command.impl;

import command.Command;
import controller.Trigger;
import entity.Birthday;
import entity.Initials;
import entity.Sex;
import entity.Student;
import exception.FailedOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.StudentLogic;
import util.PagePath;
import util.ParameterName;
import validator.Validator;

import javax.servlet.http.HttpServletRequest;

public class ChangeCommand implements Command {
    private static Logger logger = LogManager.getLogger(ShowCommand.class);
    private static final String SUCCESS_MESSAGE = "Command has finished successfully.";
    private static final String FAIL_MESSAGE = "Something went wrong while adding student.";
    private StudentLogic studentLogic;

    private enum Type{
        ADD, UPDATE
    }

    public ChangeCommand(){
        studentLogic = new StudentLogic();
    }

    @Override
    public Trigger execute(HttpServletRequest request) {
        String id = request.getParameter(ParameterName.ID.name().toLowerCase()).trim();
        String surname = request.getParameter(ParameterName.SURNAME.name().toLowerCase()).trim();
        String name = request.getParameter(ParameterName.NAME.name().toLowerCase()).trim();
        String patronymic = request.getParameter(ParameterName.PATRONYMIC.name().toLowerCase()).trim();
        String birthday = request.getParameter(ParameterName.BIRTHDAY.name().toLowerCase()).trim();
        String sex = request.getParameter(ParameterName.SEX.name().toLowerCase()).trim();

        if (!Validator.validateId(id) || !Validator.validateInitials(surname) || !Validator.validateInitials(name)
                || !Validator.validateInitials(patronymic) || !Validator.validateBirthday(birthday)){
            request.setAttribute(ParameterName.INCORRECT.name().toLowerCase(), true);
            return new Trigger(PagePath.ADDITION_PATH, Trigger.TriggerType.FORWARD);
        }else{
            int studentId = Integer.parseInt(id);
            String [] birthdayValues = birthday.split("\\.");
            byte day = (byte) Integer.parseInt(birthdayValues[0]);
            byte month = (byte) Integer.parseInt(birthdayValues[1]);
            short year = (short) Integer.parseInt(birthdayValues[2]);
            Initials studentInitials = new Initials(name, surname, patronymic);
            Birthday studentBirthday = new Birthday(day, month, year);
            Sex studentSex = Sex.valueOf(sex);
            Student currentStudent = new Student(studentId, studentInitials, studentBirthday, studentSex);
            try {
                Type type = Type.valueOf(request.getParameter(ParameterName.COMMAND.name().toLowerCase()).toUpperCase());
                switch (type){
                    case ADD:
                        studentLogic.addStudent(currentStudent);
                        break;
                    case UPDATE:
                        studentLogic.updateStudent(currentStudent);
                        break;
                    default:
                        return new Trigger(PagePath.INDEX_PATH, Trigger.TriggerType.REDIRECT);
                }
                request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), SUCCESS_MESSAGE);
            } catch (FailedOperationException e) {
                logger.catching(e);
                request.setAttribute(ParameterName.MESSAGE.name().toLowerCase(), FAIL_MESSAGE);
                return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
            }
            return new Trigger(PagePath.RESPONSE_PATH, Trigger.TriggerType.FORWARD);
        }
    }
}
