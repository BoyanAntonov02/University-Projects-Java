package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.example.exception.CompanyNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CompanyDao {

    /**
     * Creates a new company.
     *
     * @param company The company object to be created.
     */
    public static void createCompany(Company company) {

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }
    }

    /**
     * Deletes a company by its ID.
     *
     * @param id The ID of the company to be deleted.
     */
    public static void deleteCompany(Long id) {

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, id);
            session.delete(company);

            transaction.commit();
        }
    }


    /**
     * Updates an existing company with new information.
     *
     * @param newCompany The updated company object.
     * @param id         The ID of the company to be updated.
     * @throws CompanyNotFoundException If the company with the provided ID is not found.
     */
    public static void updateCompany(Company newCompany, Long id) throws CompanyNotFoundException {

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company oldCompany = session.get(Company.class, id);

            if (oldCompany != null) {
                newCompany.setId(oldCompany.getId());
                session.saveOrUpdate(newCompany);
            } else throw new CompanyNotFoundException(id);

            transaction.commit();
        }
    }

    /**
     * Retrieves a list of all companies.
     *
     * @return A list containing all company objects.
     */
    public static List<Company> allCompany() {
        List<Company> companyList = new ArrayList<>();

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            companyList = session.createQuery("select c from org.example.entity.Company c", Company.class).getResultList();


            transaction.commit();
        }
        return companyList;
    }

    /**
     * Calculates the total income of a company based on paid taxes.
     *
     * @param companyID The ID of the company.
     * @return The total income of the company.
     */
    public static double companyIncome(Long companyID) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String jpql = "SELECT SUM(tp.paidSum) AS TotalIncome " +
                    "FROM Company c " +
                    "JOIN c.employeeList bm " +
                    "JOIN bm.building b " +
                    "JOIN b.apartmentList a " +
                    "JOIN a.payTaxesList tp " +
                    "WHERE c.id =:companyId";

            double totalIncome = session.createQuery(jpql, Double.class)
                    .setParameter("companyId", companyID)
                    .getSingleResult();

            return totalIncome;

        }
    }

    /**
     * Retrieves a list of companies sorted by income in descending order.
     *
     * @return A list of companies sorted by income.
     */
    public static List<Company> companiesByIncome() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT c FROM Company c " +
                    "JOIN c.employeeList e " +
                    "JOIN e.building b " +
                    "JOIN b.apartmentList a " +
                    "JOIN a.payTaxesList pt " +
                    "GROUP BY c " +
                    "ORDER BY SUM(pt.paidSum) DESC";

            List<Company> companyList = session.createQuery(hql, Company.class).getResultList();

            return companyList;
        }
    }

}