package dbdao;

import dao.CouponDAO;
import entities.Coupon;
import exception.AlreadyExistException;
import exception.NotExistException;
import pool.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CouponDBDAO implements CouponDAO {

    ConnectionPool pool;

    public CouponDBDAO(){
        pool = ConnectionPool.getInstance();
    }


    private Coupon buildCoupon(ResultSet resultSet) throws SQLException{
        long id = resultSet.getLong(1);
        long companyId = resultSet.getLong(2);
        long categoryId = resultSet.getLong(3);
        String title = resultSet.getString(4);
        String description = resultSet.getString(5);
        LocalDate startDate = resultSet.getDate(6).toLocalDate();
        LocalDate endDate = resultSet.getDate(7).toLocalDate();
        int amount = resultSet.getInt(8);
        double price = resultSet.getDouble(9);
        String image = resultSet.getString(10);
        return  new Coupon(id,categoryId,categoryId,title,description,startDate,endDate,amount,price,image);
    }


    @Override
    public Coupon create(Coupon coupon) throws AlreadyExistException {

        Connection connection = pool.getConnection();
        String sql = "INSERT INTO COUPONS (COMPANY_ID,CATEGORY_ID,TITLE,DESCRIPTION,START_DATE,END_DATE,AMOUNT,PRICE,IMAGE)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        try(PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            pstmt.setLong(1,coupon.getCompanyId());
            pstmt.setLong(2,coupon.getCategoryId());
            pstmt.setString(3,coupon.getTitle());
            pstmt.setString(4,coupon.getDescription());
            pstmt.setDate(5, Date.valueOf(coupon.getStartDate()));
            pstmt.setDate(6, Date.valueOf(coupon.getEndDate()));
            pstmt.setInt(7,coupon.getAmount());
            pstmt.setDouble(8,coupon.getPrice());
            pstmt.setString(9,coupon.getImage());
            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()){
            coupon.setId(resultSet.getLong(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return coupon;
    }

    @Override
    public Coupon getById(long id) throws NotExistException {

        Connection connection = pool.getConnection();
        Coupon coupon = null;
        String sql = "SELECT * FROM COUPONS WHERE ID = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setLong(1,id);

            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()){
                coupon = buildCoupon(resultSet);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return coupon;
    }

    @Override
    public Coupon update(Coupon coupon) throws NotExistException {

        Connection connection = pool.getConnection();
        Coupon coupon1 = null;
        String sql = "UPDATE FROM COUPONS SET TITLE = ?,DESCRIPTION = ?,AMOUNT = ?,PRICE = ?,IMAGE = ?" +
                "WHERE ID = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,coupon.getTitle());
            pstmt.setString(2,coupon.getDescription());
            pstmt.setInt(3,coupon.getAmount());
            pstmt.setDouble(4,coupon.getPrice());
            pstmt.setLong(5,coupon.getId());

            pstmt.executeUpdate();
            ResultSet resultSet = pstmt.getGeneratedKeys();
            if (resultSet.next()){
                coupon1 = buildCoupon(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return coupon1;
    }

    @Override
    public Coupon delete(long id) throws NotExistException {

     Connection connection = pool.getConnection();
     Coupon coupon = null;
     String sql = "DELETE FROM COUPONS WHERE ID = ?";
     try(PreparedStatement pstmt = connection.prepareStatement(sql)){
         pstmt.setLong(1,coupon.getId());
         pstmt.executeUpdate();

         ResultSet resultSet = pstmt.executeQuery();
         if (resultSet.next()){
             coupon = buildCoupon(resultSet);
         }
     }catch (SQLException e){
         e.printStackTrace();
     }
     finally {
         pool.returnConnection(connection);
     }
        return coupon;
    }

    @Override
    public List<Coupon> getall() {

        Connection connection = pool.getConnection();
       List<Coupon> couponList = new ArrayList<>();
       String sql = "SELECT * FROM COUPONS";

       try(PreparedStatement pstmt = connection.prepareStatement(sql)){
           ResultSet resultSet = pstmt.executeQuery();
           while (resultSet.next()){
               couponList.add(buildCoupon(resultSet));
           }
           for (Coupon current:couponList) {
               System.out.println(current);
           }
       }catch (SQLException e){
           e.printStackTrace();
       }
       finally {
           pool.returnConnection(connection);
       }
        return couponList;
    }
}
