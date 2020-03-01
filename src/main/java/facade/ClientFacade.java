package facade;

import dao.CompanyDAO;
import dao.CouponDAO;
import dao.CustomerDAO;

public interface  ClientFacade {

    boolean logIn(String email,String password);
}