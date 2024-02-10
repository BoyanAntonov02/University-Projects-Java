package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Building;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.exception.CompanyNotFoundException;
import org.example.exception.EmployeeNotFoundException;
import org.example.exception.NoEmployeeInCompany;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EmployeeDao {

    /**
     * Creates a new employee and associates them with a company.
     *
     * @param employee  The employee object to be created.
     * @param companyID The ID of the company to which the employee belongs.
     * @throws CompanyNotFoundException If the company with the provided ID is not found.
     */
    public static void createEmployee(Employee employee, Long companyID) throws CompanyNotFoundException {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Company company = session.get(Company.class, companyID);

            if(company != null){
                employee.setCompany(company);
                session.save(employee);
            }else {
                throw new CompanyNotFoundException(companyID);
            }

            transaction.commit();
        }
    }



    /**
     * Removes an employee by their ID.
     * Unassigns the employee from their associated company and updates the buildings accordingly.
     *
     * @param id The ID of the employee to be removed.
     */
    public static void removalEmployee(Long id){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {


            Employee employee = session.get(Employee.class, id);

            List<Building> buildingList = new ArrayList<>(employee.getBuilding());

            Company company = employee.getCompany();

            employee.setCompany(null);

            buildingList.forEach(b -> {
                try {
                    BuildingDao.changeEmployee(b, company.getId(), session);
                } catch (CompanyNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NoEmployeeInCompany e) {
                    throw new RuntimeException(e);
                }
            });


        }
    }


    /**
     * Updates an existing employee with new information.
     *
     * @param newEmployee The updated employee object.
     * @param id          The ID of the employee to be updated.
     * @throws EmployeeNotFoundException If the employee with the provided ID is not found.
     */
    public static void updateEmployee(Employee newEmployee, Long id) throws EmployeeNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Employee oldEmployee = session.get(Employee.class, id);

            if(oldEmployee != null){
                newEmployee.setId(oldEmployee.getId());
                session.saveOrUpdate(newEmployee);
            } else {
                 throw new EmployeeNotFoundException(id);
            }

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

            employeeList = session.createQuery("select c from org.example.entity.Employee c", Employee.class).getResultList();


            transaction.commit();
        }
        return employeeList;
    }



    /**
     * Retrieves a list of employees sorted by name and the number of associated buildings.
     *
     * @return A list of employees sorted by name and the number of associated buildings.
     */
    public static List<Employee> employeesByBuildings() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT DISTINCT e FROM Employee e " +
                    "LEFT JOIN e.building";

            List<Employee> employees = session.createQuery(hql, Employee.class).getResultList();

            Comparator<Employee> byName = Comparator.comparing(Employee::getName);

            Comparator<Employee> byBuildingSize = Comparator
                    .comparing((Employee e) -> e.getBuilding().size())
                    .reversed();


            employees.sort(byName.thenComparing(byBuildingSize));

            return employees;
        }
    }


    /**
     * Retrieves a list of buildings associated with a specific employee.
     *
     * @param employeeID The ID of the employee.
     * @return A list of buildings associated with the specified employee.
     */
    public static List<Building> employeeBuildings(Long employeeID){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {


            CriteriaBuilder cb =session.getCriteriaBuilder();
            CriteriaQuery<Building> cr = cb.createQuery(Building.class);
            Root<Building> root = cr.from(Building.class);
            cr.select(root).where(cb.equal(root.get("employee").get("id"), employeeID));
            TypedQuery<Building> typedQuery = session.createQuery(cr);
            List<Building> buildingList = typedQuery.getResultList();


            return buildingList;
        }


    }
}
