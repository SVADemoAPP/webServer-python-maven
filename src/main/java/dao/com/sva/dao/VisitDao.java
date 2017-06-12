package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.VisitModel;

@SuppressWarnings("all")
public class VisitDao
{
    private JdbcTemplate jdbcTemplate;

    // 注入DataSource
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public Collection<VisitModel> getIpExistence(String ip)
    {
        String sql = "SELECT * FROM website_visit where  ip=? ";
        String[] params = {ip};
        return this.jdbcTemplate.query(sql,params, new AccountMapper());
    }
    
    public List<String> getAllCount()
    {
        String sql = "select visitCount from website_visit";
        // JdbcTemplate tem = this.getJdbcTemplate();
        return this.jdbcTemplate.queryForList(sql,String.class);
    }
    
    public void updateData(VisitModel visi)
    {
        String sql = "update website_visit set userName=?,visitCount=?,last_visitTime=? where ip =?";
        this.jdbcTemplate.update(sql, visi.getUserName(), visi.getVisitCount(),
            visi.getLastVisitTime(), visi.getIp());
    }
    public void saveData(VisitModel visi)
    {
        String sql = "insert into website_visit(userName,visitCount,first_visitTime,last_visitTime,ip) values(?,?,?,?,?)";
        this.jdbcTemplate.update(sql, visi.getUserName(), visi.getVisitCount(),visi.getFirstVisitTime(),
            visi.getLastVisitTime(), visi.getIp());
    }

    private class AccountMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            VisitModel sm = new VisitModel();
            sm.setIp(rs.getString("IP"));
            sm.setUserName(rs.getString("USERNAME"));
            sm.setFirstVisitTime(rs.getLong("first_visitTime"));
            sm.setLastVisitTime(rs.getLong("last_visitTime"));
            sm.setVisitCount(rs.getInt("visitCount"));
            return sm;
        }
    }
}
