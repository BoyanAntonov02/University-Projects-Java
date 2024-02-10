package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.People;
import org.example.exception.CompanyNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PeopleDao {

    /**
     * Creates a new People and associates them with an Apartment.
     *
     * @param people      The People object to be created.
     * @param apartmentID The ID of the apartment to which the People belong.
     */
    public static void createPeople(People people, Long apartmentID){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Apartment apartment = session.get(Apartment.class, apartmentID);
            people.setApartment(apartment);
            session.save(people);
            transaction.commit();
        }
    }

    /**
     * Deletes a People by their ID.
     *
     * @param id The ID of the People to be deleted.
     */
    public static void deletePeople(Long id){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            People people = session.get(People.class, id);
            session.delete(people);

            transaction.commit();
        }
    }


    /**
     * Updates an existing People with new information.
     *
     * @param newPeople The updated People object.
     * @param id        The ID of the People to be updated.
     * @throws CompanyNotFoundException If the People with the provided ID is not found.
     */
    public static void updatePeople(People newPeople, Long id) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            People oldPeople = session.get(People.class, id);

            if(oldPeople != null){
                newPeople.setId(oldPeople.getId());
                session.saveOrUpdate(newPeople);
            } else throw new CompanyNotFoundException(id);

            transaction.commit();
        }
    }


    /**
     * Retrieves a list of all People.
     *
     * @return A list containing all People objects.
     */
    public static List<People> allPeople(){
        List<People> peopleList = new ArrayList<>();

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            peopleList = session.createQuery("select c from org.example.entity.Company c", People.class).getResultList();


            transaction.commit();
        }
        return peopleList;
    }



    /**
     * Retrieves a list of People sorted by name and age (descending order).
     *
     * @return A list of People sorted by name and age (descending order).
     */
    public static List<People> peopleByNameAndAged(){
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {

            String hql = "SELECT DISTINCT p FROM People p";

            List<People> people = session.createQuery(hql, People.class).getResultList();

            Comparator<People> byName = Comparator.comparing(People::getName);

            Comparator<People> byAged = Comparator
                    .comparing(People::getBornDate)
                    .reversed();


            people.sort(byName.thenComparing(byAged));

            return people;
        }
    }


}
