package org.example.dao;

import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Apartment;
import org.example.entity.Company;
import org.example.entity.Owner;
import org.example.exception.CompanyNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class OwnerDao {

    /**
     * Creates a new owner.
     *
     * @param owner The owner object to be created.
     */
    public static void createOwner(Owner owner){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(owner);
            transaction.commit();
        }
    }

    /**
     * Deletes an owner by their ID.
     *
     * @param id The ID of the owner to be deleted.
     */
    public static void deleteOwner(Long id){

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Owner owner = session.get(Owner.class, id);
            session.delete(owner);

            transaction.commit();
        }
    }

    /**
     * Updates an existing owner with new information.
     *
     * @param newOwner The updated owner object.
     * @param id       The ID of the owner to be updated.
     * @throws CompanyNotFoundException If the owner with the provided ID is not found.
     */
    public static void updateOwner(Owner newOwner, Long id) throws CompanyNotFoundException {

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Owner oldOwner = session.get(Owner.class, id);

            if(oldOwner != null){
                newOwner.setId(oldOwner.getId());
                session.saveOrUpdate(newOwner);
            } else throw new CompanyNotFoundException(id);

            transaction.commit();
        }
    }


    /**
     * Retrieves a list of all owners.
     *
     * @return A list containing all owner objects.
     */
    public static List<Owner> allOwners(){
        List<Owner> ownerList  = new ArrayList<>();

        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            ownerList = session.createQuery("select c from org.example.entity.Company c", Owner.class).getResultList();


            transaction.commit();
        }
        return ownerList;
    }


}
