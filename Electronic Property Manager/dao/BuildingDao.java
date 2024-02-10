package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.*;
import org.example.exception.CompanyNotFoundException;
import org.example.exception.EmployeeNotFoundException;
import org.example.exception.NoEmployeeInCompany;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BuildingDao {


    /**
     * Creates a new building associated with the specified company ID.
     *
     * @param building  The building object to be created.
     * @param companyID The ID of the company to which the building belongs.
     * @throws CompanyNotFoundException If the company with the provided ID is not found.
     * @throws NoEmployeeInCompany      If there are no employees in the company.
     */
    public static void createBuilding(Building building, Long companyID) throws CompanyNotFoundException, NoEmployeeInCompany {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, companyID);

            if (company != null) {


                Employee employee = getEmployeeWithLeastBuilding(companyID, session);

                    building.setEmployee(employee);
                    session.saveOrUpdate(building);


            } else {
                throw new CompanyNotFoundException(companyID);
            }

            transaction.commit();
        }
    }


    /**
     * Changes the employee associated with a building within a company.
     *
     * @param building  The building object to be updated.
     * @param companyID The ID of the company to which the building belongs.
     * @param session   The Hibernate session.
     * @throws CompanyNotFoundException If the company with the provided ID is not found.
     * @throws NoEmployeeInCompany      If there are no employees in the company.
     */
    public static void changeEmployee(Building building, Long companyID, Session session) throws CompanyNotFoundException, NoEmployeeInCompany {

        Transaction transaction = session.beginTransaction();

        Company company = session.get(Company.class, companyID);

            if (company != null) {


                Employee employee = getEmployeeWithLeastBuilding(companyID, session);

                building.setEmployee(employee);
                session.saveOrUpdate(building);


            } else {
                throw new CompanyNotFoundException(companyID);
            }

            transaction.commit();
    }


    /**
     * Retrieves the employee with the least number of associated buildings in a specific company.
     *
     * @param companyID The ID of the company.
     * @param session   The Hibernate session.
     * @return The employee with the least number of buildings.
     * @throws NoEmployeeInCompany If there are no employees in the company.
     */
    public static Employee getEmployeeWithLeastBuilding(Long companyID, Session session) throws NoEmployeeInCompany {
        String sqlQuery = "SELECT e.id AS employee_id, COUNT(b.employee_id) AS number_of_buildings " +
                "FROM housemanager.employee e " +
                "LEFT JOIN housemanager.building b ON b.employee_id = e.id " +
                "WHERE e.company_id = :companyId " +
                "GROUP BY e.id " +
                "ORDER BY COUNT(b.employee_id) ASC " +
                "LIMIT 1";

        SQLQuery query = session.createSQLQuery(sqlQuery);
        query.setParameter("companyId", companyID);

        List<Object[]> result = query.list();

        if(result.isEmpty()) {
            throw new NoEmployeeInCompany(companyID);
        }

        Object[] row = result.get(0);
        BigInteger employeeId = (BigInteger) row[0];

        Employee employee = session.get(Employee.class, employeeId.longValue());


        return employee;
    }



    /**
     * Updates an existing employee with new information.
     *
     * @param newEmployee The updated employee object.
     * @param id          The ID of the employee to be updated.
     * @throws CompanyNotFoundException If the employee with the provided ID is not found.
     */
    public static void updateEmployee(Employee newEmployee, Long id) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee oldEmployee = session.get(Employee.class, id);

            if(oldEmployee != null){
                newEmployee.setId(oldEmployee.getId());
                session.saveOrUpdate(newEmployee);
            } else throw new CompanyNotFoundException(id);

            transaction.commit();
        }
    }

    /**
     * Retrieves a list of all employees.
     *
     * @return A list containing all employee objects.
     */
    public static List<Employee> allEmployee(){
        List<Employee> employeeList = new ArrayList<>();

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            employeeList = session.createQuery("select e from org.example.entity.Employee e", Employee.class).getResultList();


            transaction.commit();
        }
        return employeeList;
    }


    /**
     * Retrieves a list of apartments in a specified building.
     *
     * @param buildingID The ID of the building.
     * @return A list containing apartments in the specified building.
     */
    public static List<Apartment> apartmentsInBuilding(Long buildingID){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Apartment> cr = cb.createQuery(Apartment.class);
            Root<Apartment> root = cr.from(Apartment.class);
            cr.select(root).where(cb.equal(root.get("building").get("id"), buildingID));
            TypedQuery<Apartment> typedQuery = session.createQuery(cr);


            return typedQuery.getResultList();

        }
    }


}
