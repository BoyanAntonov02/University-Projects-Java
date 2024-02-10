package org.example.exception;

public class CannotCreateApartmentWithoutValidBuildingID extends Exception {
    public CannotCreateApartmentWithoutValidBuildingID()  {

        super("Building doesn't exist. Can't create apartment without building.");

    }
}
