package dbdao;

import dao.CouponDAO;
import entities.Coupon;
import exception.AlreadyExistException;
import exception.LimitException;
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
    public Coupon addCouponPurchase(long customerId, long couponId) throws LimitException {
       Coupon coupon = null;
       try {
           getById(couponId);
       }catch (NotExistException e){
           e.printStackTrace();
       }
       Connection connection = pool.getConnection();
        String sqlSelect = "SELECT * FROM COUPONS WHERE ID = ?";
        String sqlInsert = "INSERT INTO CUSTOMERS_VS_COUPONS(CUSTOMER_ID,COUPON_ID) VALUES(?,?)";

       try(PreparedStatement selectPstmt = connection.prepareStatement(sqlSelect);
       PreparedStatement insertPstmt = connection.prepareStatement(sqlInsert)) {

           selectPstmt.setLong(1,couponId);
           ResultSet resultSet = selectPstmt.executeQuery();
           while (resultSet.next()){
               coupon = buildCoupon(resultSet);
           }
           if (coupon == null) return null;
           insertPstmt.setLong(1,customerId);
           insertPstmt.setLong(2,couponId);

           insertPstmt.executeUpdate();
       }catch (SQLException e){
           e.printStackTrace();
       }
       finally {
           pool.returnConnection(connection);
       }
        return coupon;
    }

    @Override
    public Coupon deleteCouponPurchase(long customerId, long couponId) {
     Coupon coupon =null;
     Connection connection = pool.getConnection();
     String sql = "DELETE FROM CUSTOMERS_VS_COUPONS WHERE CUSTOMER_ID = ? AND COUPON_ID = ?";
     try(PreparedStatement pstmt = connection.prepareStatement(sql)) {

         pstmt.setLong(1,customerId);
         pstmt.setLong(2,couponId);
         pstmt.executeUpdate();
     }catch (SQLException e){
         e.printStackTrace();
     }
     finally {
         pool.returnConnection(connection);
     }
        return coupon;
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
    public Coupon getById(long couponId) throws NotExistException {

        Connection connection = pool.getConnection();
        Coupon coupon = null;
        String sql = "SELECT * FROM COUPONS WHERE ID = ?";

        try(PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setLong(1,couponId);

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
        String sql = "UPDATE COUPONS SET TITLE = ?,DESCRIPTION = ?,AMOUNT = ?,PRICE = ?,IMAGE = ?" +
                "WHERE ID = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,coupon.getTitle());
            pstmt.setString(2,coupon.getDescription());
            pstmt.setInt(3,coupon.getAmount());
            pstmt.setDouble(4,coupon.getPrice());
            pstmt.setLong(5,coupon.getId());
            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            pool.returnConnection(connection);
        }
        return coupon;
    }

    @Override
    public Coupon delete(long couponId) throws NotExistException {

        Coupon coupon = getById(couponId);
        if (coupon == null){
            return null;
        }
     Connection connection = pool.getConnection();
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
