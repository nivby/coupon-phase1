package dbdao;

import dao.CustomerDAO;
import entities.Customer;
import exception.AlreadyExistException;
import exception.NotExistException;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDBDAO implements CustomerDAO {


    ConnectionPool pool;

    public CustomerDBDAO(){
        pool = ConnectionPool.getInstance();
    }

    private Customer buildCustomer(ResultSet resultSet) throws SQLException {
            long id = resultSet.getLong(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String email = resultSet.getString(4);
            String password = resultSet.getString(5);
            return new Customer(id,firstName,lastName,email,password);
    }

    @Override
    public Customer create(Customer customer) throws AlreadyExistException {

        Connection connection = pool.getConnection();
        String sql = "INSERT INTO CUSTOMERS(FIRST_NAME,LAST_NAME,EMAIL,PASSWORD)" +
                "VALUES(?,?,?,?)";

        try(PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,customer.getFirstName());
            pstmt.setString(2,customer.getLastName());
            pstmt.setString(3,customer.getEmail());
            pstmt.setString(4,customer.getPassword());
            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();
            while (resultSet.next()){
            customer.setId(resultSet.getLong(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return customer;
    }

    @Override
    public Customer getById(long id) throws NotExistException {

        Customer customer = null;
        Connection connection = pool.getConnection();
        String sql = "SELECT * FROM CUSTOMERS WHERE ID = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setLong(1,id);
            ResultSet resultSet = pstmt.executeQuery();
            while(resultSet.next()){
                customer = buildCustomer(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return customer;
    }

    @Override
    public Customer getByName(String firstName, String lastName) throws NotExistException {

        Connection connection = pool.getConnection();
        Customer customer = null;
        String sql = "SELECT * FROM CUSTOMERS WHERE FIRST_NAME = ?,LAST_NAME = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,firstName);
            pstmt.setString(2,lastName);

            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                long id = resultSet.getLong(1);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                customer = new Customer(id,firstName,lastName,email,password);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return customer;
    }

    @Override
    public Customer update(Customer customer) throws NotExistException {
        Connection connection = pool.getConnection();
        String sql = "UPDATE FROM CUSTOMERS WHERE FIRST_NAME = ?,LAST_NAME = ?,EMAIL = ?,PASSWORD = ?" +
                "WHERE ID = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,customer.getFirstName());
            pstmt.setString(2,customer.getLastName());
            pstmt.setString(3,customer.getEmail());
            pstmt.setString(4,customer.getPassword());
            pstmt.setLong(5,customer.getId());

            pstmt.executeUpdate();
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                customer = buildCustomer(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return customer;
    }

    @Override
    public Customer delete(Customer customer) throws NotExistException {

        Connection connection = pool.getConnection();
        String sql = "DELETE FROM CUSTOMERS WHERE ID = ?";

        try(PreparedStatement preParedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            preParedStatement.setLong(1,customer.getId());

            preParedStatement.executeUpdate();
            ResultSet resultSet = preParedStatement.getGeneratedKeys();
            while (resultSet.next()){
            customer = buildCustomer(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() {

     Connection connection = pool.getConnection();
     List<Customer> customersList = new ArrayList<>();
     String sql = "SELECT * FROM CUSTOMERS";

     try(PreparedStatement pstmt = connection.prepareStatement(sql)){
         ResultSet resultSet = pstmt.executeQuery();
         while (resultSet.next()){
             customersList.add(buildCustomer(resultSet));
         }
         for (Customer current:customersList) {
             System.out.println(current);
         }
     }catch (SQLException e){
         e.printStackTrace();
     }
     finally {
         pool.returnConnection(connection);
     }
        return customersList;
    }
}
