package ru.hogwarts.school.controller;


import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(StudentController.class)
@DisabledInAotMode
@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoSpyBean
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentController studentController;

    @Test
    void initTest(){
        Assertions.assertThat(true).isNotNull();
    }

    @Test
    void postTest() throws Exception {
        JSONObject messenger = new JSONObject();
        messenger.put("name", "1");
        messenger.put("age", 1);

        Long id = 1L;String name = "1"; int age = 1;

        Student student = new Student(id, name, age);

        when(studentRepository.save(any(Student.class))).thenReturn(student);


        mockMvc.perform(MockMvcRequestBuilders
                .post("/student")
                .content(messenger.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/student/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(age));

    }

    @Test
    void getTest() throws Exception
}
