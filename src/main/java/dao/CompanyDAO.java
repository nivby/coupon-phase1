package dao;

import entities.Company;
import exception.AlreadyExistException;
import exception.NotExistException;
import exception.ValidException;

import java.sql.SQLException;
import java.util.List;

public interface CompanyDAO {



    boolean isCompanyExist(String email, String password) throws NotExistException, ValidException;

    Company create(Company company) throws AlreadyExistException;

    Company getByID(long id) throws NotExistException;

    Company getByName(String name);

    Company getByEmail(String email);

    Company update(Company company) throws NotExistException;

    Company delete(long id) throws NotExistException;

    List<Company> getAll();

    boolean logIn(String email,String password);

}
