package dao;

import entities.Coupon;
import exception.AlreadyExistException;
import exception.LimitException;
import exception.NotExistException;

import java.util.List;

public interface CouponDAO {

    Coupon addCouponPurchase(long customerId,long couponId) throws LimitException;

    Coupon deleteCouponPurchase(long customerId, long couponId);

    Coupon create(Coupon coupon) throws AlreadyExistException;

    Coupon getById(long id) throws NotExistException;

    Coupon update(Coupon coupon) throws NotExistException;

    Coupon delete(long id) throws NotExistException;

    List<Coupon> getall();


}
