package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.AreaModel;
import com.sva.web.models.GeofencingModel;

@SuppressWarnings("all")
public class GeofencingDao
{
    private JdbcTemplate jdbcTemplate;

    // 注入DataSource
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private class AreaMapper3 implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            GeofencingModel area = new GeofencingModel();
            area.setEntre((rs.getString("enter")));
            // area.setId(rs.getInt("id"));
            area.setMapid(rs.getLong("mapid"));
            area.setZoneid(rs.getLong("zoneid"));
            area.setIdType(rs.getString("idtype"));
            area.setTimestamp(rs.getLong("timestamp"));
            area.setUserid(rs.getString("userid"));
            return area;
        }
    }

    public Collection<GeofencingModel> getGeofencing(String zoneid,
            String userid)
    {
        String sql = "SELECT * FROM geofencing where zoneid = ? and userid=? order by timestamp asc ";
        String[] params = {zoneid, userid};
        return this.jdbcTemplate.query(sql, params, new AreaMapper3());
    }
    
    public Collection<GeofencingModel> getGeofencingByZoneId(String zoneid
         )
{
    String sql = "SELECT * FROM geofencing where zoneid = ? order by timestamp asc ";
    String[] params = {zoneid};
    return this.jdbcTemplate.query(sql, params, new AreaMapper3());
}

}
