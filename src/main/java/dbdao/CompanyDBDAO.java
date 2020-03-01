package dbdao;

import dao.CompanyDAO;
import entities.Company;
import exception.AlreadyExistException;
import exception.NotExistException;
import exception.ValidException;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CompanyDBDAO implements CompanyDAO {

    ConnectionPool pool;



    public CompanyDBDAO() {
        pool = ConnectionPool.getInstance();
    }

    private Company buildCompany(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong(1);
        String name = resultSet.getString(2);
        String email = resultSet.getString(3);
        String password = resultSet.getString(4);
        return new Company(id, name, email, password);
    }


    @Override
    public boolean isCompanyExist(String email, String password) throws NotExistException, ValidException {

        boolean isCompanyExist = false;
        Connection connection = pool.getConnection();
        String sql = "select * from COMPANIES WHERE EMAIL = ? and PASSWORD = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Company company = buildCompany(resultSet);
                isCompanyExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return isCompanyExist;
    }

    @Override
    public Company create(Company company) throws AlreadyExistException {
        Connection connection = pool.getConnection();
        String sql = "INSERT INTO companies (NAME,EMAIL,PASSWORD)" +
                "VALUES(?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getEmail());
            pstmt.setString(3, company.getPassword());

            pstmt.executeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                company.setId(resultSet.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return company;
    }

    @Override
    public Company getByID(long id) throws NotExistException {
        Connection connection = pool.getConnection();
        Company company = null;
        String sql = "SELECT * FROM companies WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                String password = resultSet.getString(4);
                company = new Company(name, email, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return company;
    }

    @Override
    public Company getByName(String name) {
        Connection connection = pool.getConnection();
        Company company = null;
        String sql = "SELECT * FROM COMPANIES WHERE NAME = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                company = buildCompany(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return company;
    }


    @Override
    public Company getByEmail(String email) {
        Connection connection = pool.getConnection();
        Company company = null;
        String sql = "SELECT * FROM COMPANIES WHERE EMAIL = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);

            ResultSet resultSet = pstmt.executeQuery();

            company = resultSet.next() ? buildCompany(resultSet) : null;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return company;
    }

    @Override
    public Company update(Company company) throws NotExistException {

        if (getByID(company.getId()) == null) {
            throw new NotExistException("this id is not exist ");
        }

        Connection connection = pool.getConnection();
        String sql = "UPDATE COMPANIES SET NAME = ?,EMAIL = ?, PASSOWRD = ?" +
                "WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getEmail());
            pstmt.setString(3, company.getPassword());
            pstmt.setLong(4, company.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return company;
    }

    @Override
    public Company delete(long companyId) throws NotExistException {

        if (getByID(companyId) == null) {
            throw new NotExistException("Company is not exist");
        }

        Connection connection = pool.getConnection();
        Company company = null;
        String sql = "DELETE FROM COMPANIES WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, companyId);
            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                company = buildCompany(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return company;
    }

    @Override
    public List<Company> getAll() {

        Connection connection = pool.getConnection();
        List<Company> companiesList = new ArrayList<>();
        String sql = "SELECT * FROM COMPANIES";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                companiesList.add(buildCompany(resultSet));
            }
            for (Company current : companiesList) {
                System.out.println(current);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
        return companiesList;
    }

    @Override
    public boolean logIn(String name, String password) {
        return false;
    }
}
