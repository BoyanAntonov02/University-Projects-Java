package org.example.exception;

public class NoEmployeeInCompany extends Exception {
    public NoEmployeeInCompany(Long id) {
        super("No employee in the company with that ID=" + id);
    }
}
