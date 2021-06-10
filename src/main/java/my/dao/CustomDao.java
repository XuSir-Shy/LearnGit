package my.dao;


import my.pojo.Customer;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CustomDao {
    @Select("select * from customer where name=#{name} and pwd=#{pwd}")
    Customer login(Customer user);
    @Select("select * from customer where name=#{name}")
    Customer selectByName(Customer user);
    @Insert("INSERT into customer (name,idcard,salt,sex,birth,phone,eaddress,education,company,manager,remarks,pwd,photo) VALUES (#{name},#{idcard},#{salt},#{sex},#{birth},#{phone},#{eaddress},#{education},#{company},#{manager},#{remarks},#{pwd},#{photo})")
    boolean register(Customer customer);
    @Insert("INSERT into customer (name,salt,eaddress,pwd) VALUES (#{name},#{salt},#{eaddress},#{pwd})")
    boolean register1(Customer customer);
    @Select("select count(*) from customer where name=#{name}")
    int valName(Customer customer);
    @Select("SELECT eaddress from customer where name=#{name}")
    String selectEmail(Customer customer);
    @Select("select * from customer ")
    List<Customer> select();
    @Select("select * from customer where id=#{id}")
    List<Customer> findByUserId(Customer customer);
    @Select("select * from customer where name=#{name}")
    List<Customer> findByUserName(Customer customer);
    @Update("update customer set name=#{name},eaddress=#{eaddress},phone=#{phone},company=#{company},manager=#{manager} where id=#{id}")//修改信息
    int updateShow(Customer customer);
    @Select("select * from customer where id=#{id}")
    Customer selectByUserId(Customer customer);
    @Delete("delete from customer where id=#{id}")
    int delete(Customer customer);
}
