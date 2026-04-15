package edu.oosd.restservices.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    @Test
    void constructorShouldSetNameAndRole() {
        Employee employee = new Employee("Bilbo Baggins", "burglar");

        assertEquals("Bilbo Baggins", employee.getName());
        assertEquals("burglar", employee.getRole());
    }

    @Test
    void settersAndGettersShouldWork() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Frodo Baggins");
        employee.setRole("thief");

        assertEquals(1L, employee.getId());
        assertEquals("Frodo Baggins", employee.getName());
        assertEquals("thief", employee.getRole());
    }

    @Test
    void equalsShouldReturnTrueForSameValues() {
        Employee e1 = new Employee("Sam", "gardener");
        e1.setId(10L);

        Employee e2 = new Employee("Sam", "gardener");
        e2.setId(10L);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void equalsShouldReturnFalseForDifferentValues() {
        Employee e1 = new Employee("Sam", "gardener");
        e1.setId(10L);

        Employee e2 = new Employee("Pippin", "fool of a Took");
        e2.setId(11L);

        assertNotEquals(e1, e2);
    }

    @Test
    void toStringShouldContainFieldValues() {
        Employee employee = new Employee("Merry", "companion");
        employee.setId(5L);

        String result = employee.toString();

        assertTrue(result.contains("5"));
        assertTrue(result.contains("Merry"));
        assertTrue(result.contains("companion"));
    }
}
