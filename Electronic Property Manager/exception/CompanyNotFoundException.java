package org.example.exception;

public class CompanyNotFoundException extends Exception {
    public CompanyNotFoundException(Long companyID)  {

        super("Company doesn't exist ID=" + companyID);

    }
}
