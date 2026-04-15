package edu.oosd.restservices.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeNotFoundExceptionTest {
    @Test
    void shouldHaveCorrectMessage() {
        EmployeeNotFoundException ex = new EmployeeNotFoundException(99L);

        assertEquals("Could not find employee 99", ex.getMessage());
    }
}
