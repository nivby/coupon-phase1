package dao;

import entities.Customer;
import exception.AlreadyExistException;
import exception.NotExistException;

import java.util.List;

public interface CustomerDAO {


    boolean isCustomerExist(String email,String password) throws NotExistException;

    Customer create(Customer customer) throws AlreadyExistException;

    Customer getById(long id) throws NotExistException;

    Customer getByEmail(String email) throws NotExistException;

    Customer getByName(String firstName,String lastName) throws NotExistException;

    Customer update(Customer customer) throws NotExistException;

    Customer delete(long id) throws NotExistException;

    List<Customer> getAll();
}
