package dao;

import entities.Coupon;
import exception.AlreadyExistException;
import exception.NotExistException;

import java.util.List;

public interface CouponDAO {

    Coupon create(Coupon coupon) throws AlreadyExistException;

    Coupon getById(long id) throws NotExistException;

    Coupon update(Coupon coupon) throws NotExistException;

    Coupon delete(long id) throws NotExistException;

    List<Coupon> getall();


}
