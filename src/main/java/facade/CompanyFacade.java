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
import pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyFacade implements ClientFacade {


    private long companyid;

    private CompanyDAO companyDAO;
    private CouponDAO couponDAO;
    private CustomerDAO customerDAO;
    private boolean isLoggedIn;


    public CompanyFacade() {
        this.companyDAO = new CompanyDBDAO();
        this.couponDAO = new CouponDBDAO();
        this.customerDAO = new CustomerDBDAO();
        this.isLoggedIn = false;
    }


    @Override
    public boolean logIn(String email, String password) {
       isLoggedIn = companyDAO.logIn(email,password);
       if (isLoggedIn){
           companyid = companyDAO.getByEmail(email).getId();
       }

       return isLoggedIn;}

    public Coupon createCoupon(Coupon coupon) throws NotLoggedInException,AlreadyExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("first you need to log-in");
        }
        if (!couponDAO.existByTitle(coupon.getTitle())){
            return couponDAO.create(coupon);
        }
        System.out.println("there is a coupon with a same title... please insert a new title name");
        return null;
    }

    public Coupon updateCoupon(Coupon coupon) throws NotLoggedInException, NotExistException {

        if (!isLoggedIn) {
            throw new NotLoggedInException("you need to login first please");
        }

        return couponDAO.updateCouponWithOutIdAndCompanyId(coupon);

    }

    public Company getInfo() throws NotLoggedInException, NotExistException {

        if (!isLoggedIn){
            throw new NotLoggedInException("first please log in");
        }

        return companyDAO.getByID(companyid);
    }

    public void deleteCoupon(long couponId) throws NotLoggedInException, NotExistException{

        if (!isLoggedIn){
            throw new NotLoggedInException("first you need to log in ! ");
        }
        if (couponDAO.getById(couponId) == null){
            throw new NotExistException("this coupon is not exist ! please try another one ! ");
        }

        for (Customer current: customerDAO.getAll()) {
        couponDAO.deleteCouponPurchase(current.getId(),couponId);
        }
    }

}

