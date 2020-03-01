package facade;

import dao.CompanyDAO;
import dao.CustomerDAO;
import dbdao.CompanyDBDAO;
import dbdao.CustomerDBDAO;
import entities.Company;
import entities.Customer;
import exception.AlreadyExistException;
import exception.NotExistException;
import exception.NotLoggedInException;

import java.util.Optional;
import java.util.function.BiPredicate;

public class AdminFacade implements ClientFacade {

    private CompanyDAO companyDAO;
    private CustomerDAO customerDAO;
    private boolean isLoggedIn;


    public AdminFacade() {
        this.companyDAO = new CompanyDBDAO();
        this.customerDAO = new CustomerDBDAO();
        this.isLoggedIn = false;
    }


    @Override
    public boolean logIn(String email, String password) {
        String originalEmail = "admin@admin.com";
        String originalPassword = "admin";
        isLoggedIn = (email.equals(originalEmail) && password.equals(originalPassword));
        return isLoggedIn;
    }

    public Company createCompany(Company company) throws NotLoggedInException, AlreadyExistException {
        if (!isLoggedIn) {
            throw new NotLoggedInException("Must be logged in in order to action");
        }

        if (company == null) { return null; }

        Company byName = companyDAO.getByName(company.getName());
        Company byEmail = companyDAO.getByEmail(company.getEmail());

        if (byName != null || byEmail != null) {
            throw new AlreadyExistException("Company already exists");
        }

        return companyDAO.create(company);
    }

}


