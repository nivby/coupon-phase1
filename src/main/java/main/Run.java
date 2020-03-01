package main;


import dao.CouponDAO;
import dbdao.CouponDBDAO;
import entities.Company;
import entities.Coupon;
import exception.AlreadyExistException;
import exception.LimitException;
import exception.NotExistException;
import exception.NotLoggedInException;
import facade.AdminFacade;
import facade.ClientFacade;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Run {
    public static void main(String[] args) {
        AdminFacade af = new AdminFacade();

        af.logIn("admin@admin.com", "admin");

        Company teva = new Company("Tevale", "tevalos", "123");
        try {
            Company company = af.createCompany(teva);
            System.out.println(company);
        } catch (NotLoggedInException | AlreadyExistException e) {
            e.printStackTrace();
        }

    }


}

