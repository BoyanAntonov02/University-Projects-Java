package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.exception.CompanyNotFoundException;
import org.example.exception.PaySumNeedToBePositiveException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;


public class TaxDao {

    /**
     * Handles the creation of a tax entity linked to a building.
     *
     * @param tax        The Tax entity to be created.
     * @param buildingID The ID of the building to associate with the tax.
     */
    public static void createTax(Tax tax, Long buildingID){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Building building = session.get(Building.class, buildingID);
            tax.setBuilding(building);
            session.save(tax);
            transaction.commit();
        }
    }


    /**
     * Deletes a tax entity by its ID.
     *
     * @param id The ID of the tax entity to be deleted.
     */
    public static void deleteTax(Long id){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Tax tax = session.get(Tax.class, id);
            session.delete(tax);

            transaction.commit();
        }
    }


    /**
     * Updates an existing tax entity identified by its ID.
     *
     * @param newTax The updated Tax entity.
     * @param id     The ID of the tax entity to be updated.
     * @throws CompanyNotFoundException If the company for the tax is not found.
     */
    public static void updateTax(Tax newTax, Long id) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Tax oldTax = session.get(Tax.class, id);

            if(oldTax != null){
                newTax.setId(newTax.getId());
                session.saveOrUpdate(newTax);
            } else throw new CompanyNotFoundException(id);

            transaction.commit();
        }
    }


    /**
     * Calculates the total tax amount for a given apartment based on its characteristics and the building tax rules.
     *
     * @param apartmentID The ID of the apartment for which taxes need to be calculated.
     * @return The calculated total tax amount for the specified apartment.
     */
    public static double calculateTaxes(Long apartmentID){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            Apartment apartment = session.get(Apartment.class, apartmentID);

            Tax tax = apartment.getBuilding().getTax();

            double finalTax = apartment.getArea() * tax.getPerSquareFootage();
            finalTax += apartment.getPets() * tax.getPerPet();


            finalTax += apartment.getPeopleList().stream()
                    .filter(people -> Period.between(people.getBornDate(), LocalDate.now()).getYears() > 7 && people.isUsedElevator())
                    .mapToDouble(people -> tax.getPerLiver())
                    .sum();


            return finalTax;
        }
    }


    /**
     * Creates a tax record for a specific apartment based on the calculated tax amount.
     *
     * @param apartmentID The ID of the apartment for which the tax record will be created.
     */
    public static void createTaxForApartment(Long apartmentID){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

           double payTax = calculateTaxes(apartmentID);
           Apartment apartment = session.get(Apartment.class, apartmentID);
           PayTaxes payTaxes = new PayTaxes(LocalDate.now(), payTax, 0.0, apartment);

           session.save(payTaxes);

           transaction.commit();
        }
    }


    /**
     * Creates tax records for all apartments within a specified building.
     *
     * @param buildingID The ID of the building for which taxes will be created for all apartments.
     */
    public static void createTaxForBuilding(Long buildingID){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Building building = session.get(Building.class, buildingID);

            building.getApartmentList().stream().forEach(apartment -> {
                createTaxForApartment(apartment.getId());
            });
        }
    }


    /**
     * Processes the payment of taxes, updating the payment records and writing transaction details to a file.
     *
     * @param payTaxesID The ID of the tax payment record to be processed.
     * @param paySum     The payment amount to be added to the tax record.
     * @throws RuntimeException If the payment sum is negative, a RuntimeException is thrown.
     */
    public static void payTax(Long payTaxesID, double paySum){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            if (paySum < 0) {
                try {
                    throw new PaySumNeedToBePositiveException();
                } catch (PaySumNeedToBePositiveException e) {
                    throw new RuntimeException(e);
                }
            }

            PayTaxes payTaxes = session.get(PayTaxes.class, payTaxesID);

            double oldPayTax = payTaxes.getPaySum();

            double oldPaidTax = payTaxes.getPaidSum();

            if(oldPayTax - paySum > 0){
                payTaxes.setPaySum(oldPayTax - paySum);
                payTaxes.setPaidSum(oldPaidTax + paySum);

                String fileName = "paid_taxes.txt";

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    LocalDate payDate = payTaxes.getPayDate();

                    Apartment apartment = payTaxes.getApartment();
                    String apartmentInfo = "Apartment ID: " + apartment.getId() + ", Area: " + apartment.getArea() + ", Pets: " + apartment.getPets();

                    Building building = apartment.getBuilding();
                    String buildingInfo = "Building ID: " + building.getId() + ", Address: " + building.getAddress();

                    Employee employee = building.getEmployee();
                    String employeeInfo = "Employee: " + employee.getName();

                    Company company = employee.getCompany();
                    String companyInfo = "Company: " + company.getName();

                            String data = String.format("%n%s%n %s%n %s%n %s%n %s%n Paid: %.2f%n %n%n%n",
                            payDate.format(formatter),companyInfo ,employeeInfo, buildingInfo, apartmentInfo, paySum);
                    writer.write(data);

                } catch (IOException e) {
                    System.err.println("Error " + e.getMessage());
                }
            }

            transaction.commit();
        }
    }




}
