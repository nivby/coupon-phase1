package dao;

import entities.Coupon;
import exception.AlreadyExistException;
import exception.LimitException;
import exception.NotExistException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface CouponDAO {


    Coupon updateEndDate(long companyId, long couponId, LocalDate endDate) throws NotExistException;

    Coupon updateAmount(long companyId,long couponId, int amount) throws NotExistException;

    Coupon addCouponAmount(long couponId) throws NotExistException;

    Coupon lessOneCouponAmount(long couponId) throws NotExistException;

    Coupon couponOfCompany(long companyId,long couponId) throws NotExistException;

    Coupon addCouponPurchase(long customerId,long couponId) throws LimitException;

    Coupon deleteCouponPurchase(long customerId, long couponId);

    Coupon create(Coupon coupon) throws AlreadyExistException;

    Coupon getById(long id) throws NotExistException;

    Coupon update(Coupon coupon) throws NotExistException;

    Coupon delete(long id) throws NotExistException;

    List<Coupon> getall();


}
