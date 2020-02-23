package main;


import categories.Category;
import dao.CompanyDAO;
import dao.CouponDAO;
import dao.GenerateTables;
import dbdao.CompanyDBDAO;
import dbdao.CouponDBDAO;
import entities.Company;
import entities.Coupon;
import exception.AlreadyExistException;
import exception.NotExistException;
import exception.ValidException;
import pool.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;

public class Run {
    public static void main(String[] args) {

        CompanyDAO dao = new CompanyDBDAO();
        try {
            System.out.println(dao.isCompanyExist("Aroma@gmail.com","Aroma12345"));
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (ValidException e) {
            e.printStackTrace();
        }

    }
}

