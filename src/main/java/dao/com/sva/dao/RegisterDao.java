package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.AreaModel;
import com.sva.model.RegisterModel;

@SuppressWarnings("all")
public class RegisterDao
{
    private JdbcTemplate jdbcTemplate;

    // 注入DataSource
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveRegister(RegisterModel register)
    {
        String sql = "replace into register(role,userId,userName,password,status,phoneNumber,timestamp) values(?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(sql, register.getRole(), register.getUserId(),
                register.getUserName(),register.getPassWord(),
                register.getStatus(), register.getPhoneNumber(),
                register.getTimes());
    }
    public void refreshRegister(RegisterModel register)
    {
    	String sql = "update register set userId=? where phoneNumber = ?";
    	this.jdbcTemplate.update(sql,register.getUserId(),register.getPhoneNumber());
    }

    public List<RegisterModel> getDataByUserName(String userName)
    {
        String sql = "select * from register where userName=? and status=1";
        String[] params = {userName};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataBy(String phone)
    {
        String sql = "select * from register where phoneNumber=? and status=1";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataByPhoneNumber(String phone)
    {
        String sql = "select * from register where phoneNumber=?";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataByIsTrue(String phone)
    {
        String sql = "select * from register where phoneNumber=? and isTrue=1";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataByIsTrue1(String phone)
    {
        String sql = "select * from register where phoneNumber=? and isTrue=2";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataByStatus2(String phone)
    {
        String sql = "select * from register where phoneNumber=? and status=2";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public List<RegisterModel> getDataByStatus3(String phone)
    {
        String sql = "select * from register where phoneNumber=? and status=3";
        String[] params = {phone};
        return this.jdbcTemplate.query(sql, params, new RegisterMapper());
    }

    public void updateIsTrue(String phone)
    {
        String sql = "update register set isTrue=1 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public void updateIsTrue2(String phone)
    {
        String sql = "update register set isTrue=2 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public void updateIsTrue1(String phone)
    {
        String sql = "update register set isTrue=0 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public void updataStatus(String phone, String otherPhone)
    {
        // System.out.println("status");
        String sql = "update register set status=1,otherPhone= " + phone
                + ",isTrue=0 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, otherPhone);
    }

    public void updataStatus1(String phone)
    {
        String sql = "update register set status=0 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public void updataStatus2(String phone)
    {
        String sql = "update register set status=3 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public void updataStatusByCancel(String phone)
    {
        String sql = "update register set status=2 where phoneNumber = ?";
        this.jdbcTemplate.update(sql, phone);
    }

    public List<String> getIpByUserName(String phoneNumber)
    {
        String sql = "select userId from register where phoneNumber=? ";
        // JdbcTemplate tem = this.getJdbcTemplate();
        String[] params = {phoneNumber};
        return this.jdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<String> getStatusByphoneNumber2(String phoneNumber)
    {
        String sql = "select userId from register where phoneNumber=? and status=2 ";
        // JdbcTemplate tem = this.getJdbcTemplate();
        String[] params = {phoneNumber};
        return this.jdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<String> getStatusByIsTrue2(String phoneNumber)
    {
        String sql = "select userId from register where  isTrue=2  and phoneNumber=? ";
        // JdbcTemplate tem = this.getJdbcTemplate();
        String[] params = {phoneNumber};
        return this.jdbcTemplate.queryForList(sql, params, String.class);
    }
    public List<String> getStatusByIsTrue1(String phoneNumber)
    {
        String sql = "select userId from register where  isTrue=1  and phoneNumber=? ";
        // JdbcTemplate tem = this.getJdbcTemplate();
        String[] params = {phoneNumber};
        return this.jdbcTemplate.queryForList(sql, params, String.class);
    }

    private class RegisterMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            RegisterModel phone = new RegisterModel();
            phone.setUserId(rs.getString("userId"));
            phone.setUserName(rs.getString("userName"));
            phone.setTimes(rs.getLong("timestamp"));
            phone.setPassWord(rs.getString("password"));
            phone.setPhoneNumber(rs.getString("phoneNumber"));
            phone.setStatus(rs.getInt("status"));
            phone.setIsTrue(rs.getInt("isTrue"));
            phone.setRole(rs.getInt("role"));
            phone.setOtherPhone(rs.getLong("otherPhone"));
            phone.setLoginStatus(rs.getString("loginStatus"));
            
            return phone;
        }
    }

    public int checkLogin(String userName, String password)
    {
        String sql = "SELECT count(*) res FROM register where userName = ? and password=?";

        return this.jdbcTemplate.queryForObject(sql, new Object[]{userName,
                password}, Integer.class);
    }

    public int checkLogin1(RegisterModel model)
    {
        String sql = "SELECT count(*) res FROM register where phoneNumber = ? and password=?";

        return this.jdbcTemplate.queryForObject(sql,
                new Object[]{model.getPhoneNumber(), model.getPassWord()},
                Integer.class);
    }
    
    public void setLoginStatus(StringBuffer strb,String phoneNumber)
    {
        String sql = "update register set loginStatus=? where phoneNumber = ? ";

        this.jdbcTemplate.update(sql,strb,phoneNumber);
    }
}
