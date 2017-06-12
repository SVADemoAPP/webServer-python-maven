package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.PrruModel;

@SuppressWarnings("all")
public class PrruDao
{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Collection<PrruModel> getPrruInfoByflooNo(String floorNo, String eNodeBid, String cellId)
    {
    	
        String sql = "SELECT * from pRRU where floorNo=? and eNodeBid=? and cellId=?";
        String[] params = {floorNo,eNodeBid,cellId};
        if(eNodeBid.isEmpty() && cellId.isEmpty()){
        	sql = "SELECT * from pRRU where floorNo=?";
        	params = new String[]{floorNo};
        }
        return this.jdbcTemplate.query(sql, params, new pRRUMapper());
    }
    
    public List<PrruModel> getPrruInfo(String floorNo, String eNodeBid)
    {
    	
        String sql = "select DISTINCT eNodeBid,cellId from prru where floorNo=? and eNodeBid=?";
        String[] params = {floorNo, eNodeBid};
        return this.jdbcTemplate.query(sql, params, new pRRUInfoMapper());
    }

    public void saveInfo(PrruModel bm) throws SQLException
    {
        String sql = "INSERT INTO pRRU(floorNo,x,y,pRRUid,name,placeId,neCode,cellId,eNodeBid) VALUES(?,?,?,?,?,?,?,?,?)";
        this.jdbcTemplate.update(sql, bm.getFloorNo(), bm.getX(), bm.getY(),
                bm.getNeId(), bm.getNeName(), bm.getPlaceId(), bm.getNeCode(),bm.getCellId(), bm.geteNodeBid());
    }

    public void deleteInfo(String floorNo, String eNodeBid) throws SQLException
    {
    	String sql = "delete from pRRU  where floorNo=? and eNodeBid=?";
    	if(eNodeBid.isEmpty()){
    		sql = "delete from pRRU  where floorNo=?";
    		this.jdbcTemplate.update(sql, floorNo);
    	}else {
    		
    		this.jdbcTemplate.update(sql, new Object[]{floorNo, eNodeBid});
    	}
        
    }

    public int checkByFloorNo(String floorNo, String id)
    {
        String sql = "SELECT count(*) res FROM pRRU where floorNo=? and floorNo != ?";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{floorNo, id},
                Integer.class);
    }

    public void updateInfo(String floorNo, String newfloorNo)
            throws SQLException
    {
        String sql = "update  pRRU set floorNo=? where floorNo=?";
        String[] params = {newfloorNo, floorNo};
        this.jdbcTemplate.update(sql, params);
    }

    public List<Map<String, Object>> getSignal(long time,String userId)
    {
        String sql = "select * from prrusignal where timestamp = (select max(timestamp) from prrusignal where timestamp > "+time+" and userId = '"+userId+"');";
        return this.jdbcTemplate.queryForList(sql);
    }

    private class pRRUMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            PrruModel bm = new PrruModel();
            bm.setFloorNo(rs.getString("FLOORNO"));
            bm.setNeCode(rs.getString("NECODE"));
            bm.setNeId(rs.getString("PRRUID"));
            bm.setNeName(rs.getString("NAME"));
            bm.setX(rs.getString("X"));
            bm.setY(rs.getString("Y"));
            bm.setPlaceId(rs.getInt("PLACEID"));
            bm.setCellId(rs.getString("CELLID"));
            bm.seteNodeBid(rs.getString("ENODEBID"));
            return bm;
        }
    }
    
    private class pRRUInfoMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            PrruModel bm = new PrruModel();
            bm.setCellId(rs.getString("CELLID"));
            bm.seteNodeBid(rs.getString("ENODEBID"));
            return bm;
        }
    }

}
