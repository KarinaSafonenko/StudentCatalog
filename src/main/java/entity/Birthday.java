package entity;

public class Birthday {
    private byte day;
    private byte month;
    private short year;

    public Birthday(byte day, byte month, short year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public byte getDay() {
        return day;
    }

    public byte getMonth() {
        return month;
    }

    public short getYear() {
        return year;
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }
}
