package edu.oosd.restservices.payroll;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class EmployeeModelAssemblerTest {
    private final EmployeeModelAssembler assembler = new EmployeeModelAssembler();

    @Test
    void toModelShouldCreateSelfAndCollectionLinks() {
        Employee employee = new Employee("Bilbo Baggins", "burglar");
        employee.setId(1L);

        EntityModel<Employee> model = assembler.toModel(employee);

        assertNotNull(model.getContent());
        assertEquals(employee, model.getContent());

        Link selfLink = model.getRequiredLink("self");
        Link employeesLink = model.getRequiredLink("employees");

        assertTrue(selfLink.getHref().endsWith("/employees/1"));
        assertTrue(employeesLink.getHref().endsWith("/employees"));
    }
}
