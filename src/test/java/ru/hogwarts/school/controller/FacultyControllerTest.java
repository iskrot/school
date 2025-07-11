package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisabledInAotMode
@WebMvcTest(FacultyController.class)
@Import(FacultyService.class)
public class FacultyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FacultyRepository facultyRepository;
    @MockitoSpyBean
    FacultyService facultyService;
    @Autowired
    FacultyController facultyController;

    @Test
    void initTest() {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    void putAndGetTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1L);
        jsonObject.put("name", "1");
        jsonObject.put("color", "1");
        Faculty faculty = new Faculty(1L, "1", "1");

        when(facultyRepository.save(any())).thenReturn(faculty);
        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("1"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("1"));
    }

    @Test
    void findByColorTest() throws Exception {
        List<Faculty> list = new ArrayList<>();
        for (byte i = 0; i < 10; i++) {
            list.add(new Faculty((long) i, "1", String.valueOf(i)));
            list.add(new Faculty(i + 1L, "2", String.valueOf(i)));
        }

        when(facultyRepository.findByColorContains(any())).thenReturn(list.stream().filter(i -> i.getColor().contains("2")).toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":2,\"name\":\"1\",\"color\":\"2\",\"students\":null},{\"id\":3,\"name\":\"2\",\"color\":\"2\",\"students\":null}]"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    void findTest() throws Exception {
        List<Faculty> list = new ArrayList<>();
        for (byte i = 0; i < 10; i += 2) {
            list.add(new Faculty((long) i, "1", String.valueOf(i)));
            list.add(new Faculty(i + 1L, "2", String.valueOf(i)));
        }

        when(facultyRepository.findAll()).thenReturn(list);
        when(facultyRepository.findAllByNameContainsIgnoreCase(any())).thenReturn(new ArrayList<>(list.stream().filter(i -> i.getName().contains("1")).toList()));
        when(facultyRepository.findByColorContains(any())).thenReturn(new ArrayList<>(list.stream().filter(i -> i.getColor().contains("4")).toList()));
        System.out.println(list.stream().filter(i -> i.getColor().contains("4")).toList());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/searchToNameOrColor")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/searchToNameOrColor?color=4")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":4,\"name\":\"1\",\"color\":\"4\",\"students\":null},{\"id\":5,\"name\":\"2\",\"color\":\"4\",\"students\":null}]"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/searchToNameOrColor?name=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":0,\"name\":\"1\",\"color\":\"0\",\"students\":null},{\"id\":2,\"name\":\"1\",\"color\":\"2\",\"students\":null},{\"id\":4,\"name\":\"1\",\"color\":\"4\",\"students\":null},{\"id\":6,\"name\":\"1\",\"color\":\"6\",\"students\":null},{\"id\":8,\"name\":\"1\",\"color\":\"8\",\"students\":null}]"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/searchToNameOrColor?color=4&name=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":2,\"name\":\"1\",\"color\":\"2\",\"students\":null},{\"id\":4,\"name\":\"1\",\"color\":\"4\",\"students\":null},{\"id\":5,\"name\":\"2\",\"color\":\"4\",\"students\":null},{\"id\":6,\"name\":\"1\",\"color\":\"6\",\"students\":null},{\"id\":0,\"name\":\"1\",\"color\":\"0\",\"students\":null},{\"id\":8,\"name\":\"1\",\"color\":\"8\",\"students\":null}]"));
    }

    @Test
    void getStudentsTest() throws Exception {
        Faculty faculty = new Faculty(1L, "1", "1");
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "1", 1));
        students.add(new Student(2L, "2", 2));
        faculty.setStudents(students);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/faculty/1/getStudents")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{\"id\":1,\"name\":\"1\",\"age\":1,\"facultyName\":null},{\"id\":2,\"name\":\"2\",\"age\":2,\"facultyName\":null}]  "));
    }
}
