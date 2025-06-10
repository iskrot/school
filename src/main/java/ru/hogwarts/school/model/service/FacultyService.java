package ru.hogwarts.school.model.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
    private Map<Long, Faculty> allFaculty = new HashMap<>();
    private long id = 0;

    public Map<Long, Faculty> getAllFaculty() {
        return allFaculty;
    }

    public void add(Faculty faculty){
        faculty.setId(++id);
        allFaculty.put(id, faculty);
    }

    public Faculty get(long id){
        return allFaculty.get(id);
    }

    public void put(Faculty faculty){
        allFaculty.put(faculty.getId(), faculty);
    }

    public void remove(long id){
        allFaculty.remove(id);
    }

    public Collection<Faculty> findByColor(String color){
        return allFaculty.values().stream().filter(i -> i.getColor() == color).toList();
    }


}
