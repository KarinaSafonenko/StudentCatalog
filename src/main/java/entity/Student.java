package entity;

public class Student {
    private int id;
    private Initials initials;
    private Birthday birthday;
    private Sex sex;

    public Student(int id, Initials initials, Birthday birthday, Sex sex){
        this.id = id;
        this.initials = initials;
        this.birthday = birthday;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public Initials getInitials() {
        return initials;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public Sex getSex() {
        return sex;
    }

}
