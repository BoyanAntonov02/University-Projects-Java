package org.example.exception;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(Long id) {
        super("Employee doesn't exist ID=" + id);
    }
}
