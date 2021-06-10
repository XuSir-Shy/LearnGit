package my.dao;

import my.pojo.Customer;
import my.pojo.Manager;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ManagerDao {
    @Select("select * from manager where name=#{name} and pwd=#{pwd}")
    Manager login(Manager user);
    @Select("select * from manager where name=#{name}")
    Manager selectByName(Manager user);
    @Insert("INSERT into manager (name,idcard,salt,sex,birth,phone,eaddress,education,remarks,pwd) VALUES (#{name},#{idcard},#{salt},#{sex},#{birth},#{phone},#{eaddress},#{education},#{remarks},#{pwd})")
    boolean register(Manager manager);
    @Select("select count(*) from manager where name=#{name}")
    int valName(Manager manager);
    @Select("SELECT eaddress from manager where name=#{name}")
    String selectEmail(Manager manager);
    @Select("select * from customer where manager=#{name}")
    List<Customer> selectByManger(Manager manager);
    @Select("select * from customer where id=#{conValue} and manager = #{name}")
    List<Customer> findByUserId(Manager manager);
    @Select("select * from customer where name=#{conValue} and manager = #{name}")
    List<Customer> findByUserName(Manager manager);
    @Select("select * from customer and manager=#{name}")
    List<Customer> select();
}
