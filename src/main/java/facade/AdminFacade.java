package facade;

import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import dbdao.CustomerDBDAO;
import entities.Company;
import entities.Coupon;
import entities.Customer;
import exception.AlreadyExistException;
import exception.NotExistException;
import exception.NotLoggedInException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class AdminFacade implements ClientFacade {

    private CompanyDAO companyDAO;
    private CustomerDAO customerDAO;
    private CouponDAO couponDAO;
    private boolean isLoggedIn;


    public AdminFacade() {
        this.companyDAO = new CompanyDBDAO();
        this.customerDAO = new CustomerDBDAO();
        this.couponDAO = new CouponDBDAO();
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

    public Company updateCompany(Company company) throws NotLoggedInException, NotExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("you must to log in before");
        }
        if (company == null){return null;}

        return companyDAO.updateEmailAndPassword(company);

    }

    public boolean deleteCompany(long companyId,long couponId) throws NotLoggedInException, NotExistException {

        boolean isDelete = false;
        if (!isLoggedIn) {
            throw new NotLoggedInException("you need to login first ! ");
        }
        if (!isDelete){
            couponDAO.deleteFromCustomerAndCoupon(couponId);
            couponDAO.deleteAllCouponOfCompany(companyId);
            companyDAO.delete(companyId);
        isDelete = true;
        }
        return isDelete;
    }

    public List<Company> allCompanies() throws NotLoggedInException{

        if (!isLoggedIn){
            throw new NotLoggedInException("you need to log in !");
        }
        return companyDAO.getAllCompanies();
    }

    public Company getCompanyById(long id) throws NotLoggedInException,NotExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("first, you must to log in ! ");
        }
        if (companyDAO.getByID(id) == null) {
            throw new NotExistException("this id is not good");
        }
        return companyDAO.getByID(id);
    }

    public Customer createCustomer(Customer customer) throws NotLoggedInException, AlreadyExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("please log in first ! ");
        }

        try {
            Customer byEmail = customerDAO.getByEmail(customer.getEmail());
            if (byEmail != null) {
                throw new AlreadyExistException("this email is exist ! change email ");
            }
        } catch (NotExistException e) {
            e.printStackTrace();
        }
        return customerDAO.create(customer);
    }

    public Customer updateCustomer(Customer customer) throws NotLoggedInException, AlreadyExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("you must to login ! ");
        }
        try {
            return customerDAO.update(customer);
        } catch (NotExistException e) {
            e.printStackTrace();
        }
    return customer;
    }

    public List<Customer> allCustomers() throws NotLoggedInException {

        if (!isLoggedIn){
            throw new NotLoggedInException("you need to login ! ");
        }
        return customerDAO.getAll();
    }

    public Customer getCustomerById(long id) throws NotLoggedInException, NotExistException {

        if (!isLoggedIn) {
            throw new NotLoggedInException("you need first to login ! ");
        }
        if (customerDAO.getById(id) == null) {
            throw new NotExistException("this id " + id + " is not good");
        }

        return customerDAO.getById(id);
    }



}

