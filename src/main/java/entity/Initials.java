package entity;

public class Initials {
    private String name;
    private String surname;
    private String patronymic;

    public Initials(String name, String surname, String patronymic){
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    @Override
    public String toString() {
        return surname + " " + name + " " + patronymic;
    }
}
