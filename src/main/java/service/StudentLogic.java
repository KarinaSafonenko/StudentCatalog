package service;

import entity.Student;
import exception.FailedOperationException;
import logic.DBWorker;

import java.util.HashSet;
import java.util.Set;

public class StudentLogic {
    private DBWorker dbWorker = new DBWorker();

    public void addStudent(Student student) throws FailedOperationException {
        if (dbWorker.find(student)){
            throw new FailedOperationException("Student has already been in set!");
        }else{
            if (!dbWorker.add(student)){
                throw new FailedOperationException("Something went wrong while adding student!");
            }
        }
    }

    public Set<Integer> getIdSet()throws FailedOperationException{
        Set<Integer> idSet = new HashSet<>();
        Set<Student> studentSet = dbWorker.findStudents();
        for (Student current: studentSet){
            idSet.add(current.getId());
        }
        return idSet;
    }

    public Set<Student> getStudentSet() throws FailedOperationException {
        return dbWorker.findStudents();
    }

    public void removeStudent(int id) throws FailedOperationException {
        if (!dbWorker.delete(id)){
            throw new FailedOperationException("Remove operation failed!");
        }
    }

    public void updateStudent(Student student) throws FailedOperationException {
        if (!dbWorker.update(student)){
            throw new FailedOperationException(" Update operation failed!");
        }
    }
}
