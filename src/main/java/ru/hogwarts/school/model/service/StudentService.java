package ru.hogwarts.school.model.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private Map<Long, Student> allStudents = new HashMap<>();
    private long id = 0;

    public Map<Long, Student> getAllStudents() {
        return allStudents;
    }

    public void add(Student student){
        student.setId(++id);
        allStudents.put(id, student);
    }

    public Student get(long id){
        return allStudents.get(id);
    }

    public void put(Student student){
        allStudents.put(student.getId(), student);
    }

    public void remove(long id){
        allStudents.remove(id);
    }

    public Collection<Student> findByAge(int age) {
        return allStudents.values().stream().filter(i -> i.getAge() == age).toList();
    }
}
