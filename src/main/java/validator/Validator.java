package validator;

public class Validator {
    private static final String INITIALS_TEMPLATE = "[а-яА-Яa-zA-Z]{1,45}";
    private static final String BIRTHDAY_TEMPLATE = "\\d{2}\\.\\d{2}.\\d{4}";
    private static final String ID_TEMPLATE = "\\d{1,5}";

    public static boolean validateInitials(String input){
        if (!isEmptyOrNull(input)){
            return input.matches(INITIALS_TEMPLATE);
        }
        return false;
    }

    public static boolean validateId(String input){
        if (!isEmptyOrNull(input)){
            return input.matches(ID_TEMPLATE);
        }
        return false;
    }

    public static boolean validateBirthday(String input){
        if (!isEmptyOrNull(input)){
            return input.matches(BIRTHDAY_TEMPLATE);
        }
        return false;
    }

    private static boolean isEmptyOrNull(String input){
        boolean result;
        result = (input == null || input.trim().isEmpty()) ? true: false;
        return result;
    }
}
