package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty){
        return facultyService.add(faculty);
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable(name = "id") long id){
        return  facultyService.get(id);
    }

    @PutMapping
    public Faculty putFaculty(@RequestBody Faculty faculty){
        return facultyService.put(faculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable(name = "id") long id){
        facultyService.remove(id);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.findByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }


}
