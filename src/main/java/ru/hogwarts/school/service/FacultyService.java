package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {


    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty add(Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public Faculty get(long id){
        return facultyRepository.findById(id).get();
    }

    public Faculty put(Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public void remove(long id){
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByColor(String color){
        return facultyRepository.findAll().stream().filter(i -> i.getColor() == color).toList();
    }


}
