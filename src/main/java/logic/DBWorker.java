package logic;

import connection.ConnectionPool;
import connection.ProxyConnection;
import entity.Birthday;
import entity.Initials;
import entity.Sex;
import entity.Student;
import exception.FailedOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DBWorker {
    private static Logger logger = LogManager.getLogger(DBWorker.class);
    private static String INSERT = "INSERT INTO students.student(id, name, surname, patronymic, birth_day, birth_month, birth_year, sex) values(?, ?, ?, ?, ?, ?, ?, ?)";
    private static String SELECT_WHERE = "SELECT id, name, surname, patronymic, birth_day, birth_month, birth_year, sex FROM students.student WHERE id = ?";
    private static String SELECT = "SELECT id, name, surname, patronymic, birth_day, birth_month, birth_year, sex FROM students.student";
    private static String UPDATE = "UPDATE students.student SET name = ?, surname = ?, patronymic = ?, birth_day = ?, birth_month = ?, birth_year = ?, sex = ? WHERE id = ?";
    private static String DELETE = "DELETE FROM students.student WHERE id = ?";

    private enum StudentParameter{
        ID, NAME, SURNAME, PATRONYMIC, BIRTH_DAY, BIRTH_MONTH, BIRTH_YEAR, SEX
    }

    public boolean add(Student student) throws FailedOperationException {
        ProxyConnection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setInt(1, student.getId());
            statement.setString(2, student.getInitials().getName());
            statement.setString(3,student.getInitials().getSurname());
            statement.setString(4,student.getInitials().getPatronymic());
            statement.setInt(5, student.getBirthday().getDay());
            statement.setInt(6,student.getBirthday().getMonth());
            statement.setInt(7,student.getBirthday().getYear());
            statement.setString(8,student.getSex().name());
            int inserted = statement.executeUpdate();
            return inserted != 0;
        } catch (SQLException e) {
            throw  new FailedOperationException(e);
        }finally {
            releaseConnection(connection);
        }
    }

    public boolean find(Student student) throws FailedOperationException {
        ProxyConnection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(SELECT_WHERE)) {
            statement.setInt(1, student.getId());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw  new FailedOperationException(e);
        }finally {
            releaseConnection(connection);
        }
    }

    public Set<Student> findStudents() throws FailedOperationException {
        Set<Student> result = new HashSet<>();
        ProxyConnection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(SELECT)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString(StudentParameter.ID.name().toLowerCase());
                String name = resultSet.getString(StudentParameter.NAME.name().toLowerCase());
                String surname = resultSet.getString(StudentParameter.SURNAME.name().toLowerCase());
                String patronymic = resultSet.getString(StudentParameter.PATRONYMIC.name().toLowerCase());
                String birthDay = resultSet.getString(StudentParameter.BIRTH_DAY.name().toLowerCase());
                String birthMonth = resultSet.getString(StudentParameter.BIRTH_MONTH.name().toLowerCase());
                String birthYear = resultSet.getString(StudentParameter.BIRTH_YEAR.name().toLowerCase());
                String sex = resultSet.getString(StudentParameter.SEX.name().toLowerCase());
                Initials initials = new Initials(name, surname, patronymic);
                Birthday birthday = new Birthday((byte)Integer.parseInt(birthDay),(byte)Integer.parseInt(birthMonth), (short)Integer.parseInt(birthYear));
                result.add(new Student(Integer.parseInt(id), initials, birthday, Sex.valueOf(sex)));
            }
        } catch (SQLException e) {
            throw  new FailedOperationException(e);
        }
        return result;
    }

    public boolean update(Student student) throws FailedOperationException {
        ProxyConnection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, student.getInitials().getName());
            statement.setString(2,student.getInitials().getSurname());
            statement.setString(3,student.getInitials().getPatronymic());
            statement.setInt(4, student.getBirthday().getDay());
            statement.setInt(5,student.getBirthday().getMonth());
            statement.setInt(6,student.getBirthday().getYear());
            statement.setString(7,student.getSex().name());
            statement.setInt(8,student.getId());
            int updated = statement.executeUpdate();
            return updated != 0;
        } catch (SQLException e) {
            throw  new FailedOperationException(e);
        }
    }

    public boolean delete(int id) throws FailedOperationException {
        ProxyConnection connection = ConnectionPool.getInstance().getConnection();
        try(PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setInt(1, id);
            int deleted = statement.executeUpdate();
            return deleted != 0;
        } catch (SQLException e) {
            throw  new FailedOperationException(e);
        }
    }

    void releaseConnection(ProxyConnection proxyConnection) {
        if (proxyConnection != null) {
            proxyConnection.close();
        }
    }
}
