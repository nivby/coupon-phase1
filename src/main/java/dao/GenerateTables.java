package dao;

import pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface GenerateTables {

    ConnectionPool pool = ConnectionPool.getInstance();

    Runnable createCompanies = () -> {
        String sql = "CREATE TABLE COMPANIES (ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                "NAME VARCHAR (30) NOT NULL," +
                "EMAIL VARCHAR (50) NOT NULL," +
                "PASSWORD VARCHAR (255) NOT NULL)" ;
        Connection connection = pool.getConnection();

        try(PreparedStatement preparedStatemen= connection.prepareStatement(sql)) {

            preparedStatemen.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    };

    Runnable createCustomers = () -> {
        String sql = "CREATE TABLE CUSTOMERS (ID BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                "FIRST_NAME VARCHAR(20) NOT NULL," +
                "LAST_NAME VARCHAR(20) NOT NULL," +
                "EMAIL VARCHAR(50) NOT NULL," +
                "PASSWORD VARCHAR(255) NOT NULL)";
        Connection connection = pool.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    };

    Runnable createCategories = () -> {

        String sql = "CREATE TABLE CATEGORIES (ID BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "NAME VARCHAR(50) NOT NULL)";

        Connection connection = pool.getConnection();

        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
    };

    Runnable createCoupons = () -> {

        String sql = "CREATE TABLE COUPONS(ID BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "COMPANY_ID BIGINT NOT NULL," +
                "CATEGORY_ID BIGINT NOT NULL," +
                "TITLE VARCHAR(50) NOT NULL," +
                "DESCRIPTION VARCHAR(255) NOT NULL," +
                "START_DATE DATE NOT NULL," +
                "END_DATE DATE NOT NULL," +
                "AMOUNT INT NOT NULL," +
                "PRICE DOUBLE NOT NULL," +
                "IMAGE VARCHAR (255) NOT NULL," +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COUPONS.COMPANIES(ID)," +
                "FOREIGN KEY (CATEGORY_ID) REFERENCES COUPONS.CATEGORIES(ID))";

        Connection connection = pool.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
    };

    Runnable createCustomersVsCoupons = () -> {

        String sql = "CREATE TABLE CUSTOMERS_VS_COUPONS(CUSTOMER_ID BIGINT NOT NULL," +
                "FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS(ID)," +
                "COUPON_ID BIGINT NOT NULL," +
                "FOREIGN KEY (COUPON_ID) REFERENCES COUPONS(ID))";

        Connection connection = pool.getConnection();
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
    };

}
