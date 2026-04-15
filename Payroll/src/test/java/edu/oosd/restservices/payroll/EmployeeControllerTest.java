package edu.oosd.restservices.payroll;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EmployeeRepository repository;

    @MockitoBean
    private EmployeeModelAssembler assembler;

    @Test
    void shouldReturnAllEmployees() throws Exception {
        Employee employee = new Employee("Bilbo Baggins", "burglar");
        employee.setId(1L);

        EntityModel<Employee> employeeModel = EntityModel.of(employee);

        when(repository.findAll()).thenReturn(List.of(employee));
        when(assembler.toModel(employee)).thenReturn(employeeModel);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.employeeList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.employeeList[0].name").value("Bilbo Baggins"))
                .andExpect(jsonPath("$._embedded.employeeList[0].role").value("burglar"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void shouldReturnOneEmployee() throws Exception {
        Employee employee = new Employee("Frodo Baggins", "thief");
        employee.setId(2L);

        EntityModel<Employee> employeeModel = EntityModel.of(employee);

        when(repository.findById(2L)).thenReturn(Optional.of(employee));
        when(assembler.toModel(employee)).thenReturn(employeeModel);

        mockMvc.perform(get("/employees/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Frodo Baggins"))
                .andExpect(jsonPath("$.role").value("thief"));
    }

    @Test
    void shouldReturnNotFoundWhenEmployeeDoesNotExist() throws Exception {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find employee 99"));
    }

    @Test
    void shouldCreateNewEmployee() throws Exception {
        Employee input = new Employee("Samwise Gamgee", "gardener");
        Employee saved = new Employee("Samwise Gamgee", "gardener");
        saved.setId(3L);

        when(repository.save(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Samwise Gamgee"))
                .andExpect(jsonPath("$.role").value("gardener"));
    }

    @Test
    void shouldReplaceExistingEmployee() throws Exception {
        Employee existing = new Employee("Old Name", "Old Role");
        existing.setId(1L);

        Employee updatedInput = new Employee("New Name", "New Role");

        Employee saved = new Employee("New Name", "New Role");
        saved.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.role").value("New Role"));
    }

    @Test
    void shouldCreateEmployeeWhenReplacingNonExistingEmployee() throws Exception {
        Employee newEmployee = new Employee("Pippin", "guard");
        Employee saved = new Employee("Pippin", "guard");
        saved.setId(7L);

        when(repository.findById(7L)).thenReturn(Optional.empty());
        when(repository.save(any(Employee.class))).thenReturn(saved);

        mockMvc.perform(put("/employees/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("Pippin"))
                .andExpect(jsonPath("$.role").value("guard"));
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        doNothing().when(repository).deleteById(eq(1L));

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk());
    }
}
