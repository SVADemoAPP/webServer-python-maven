package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.MessagePush;

/**
 * 
 * 
 * @author wwx283823
 * @version iSoftStone 2016-3-16
 * @since iSoftStone
 */
@SuppressWarnings("all")
public class MessagePushDao
{
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private class MessagePushMapper implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            MessagePush mes = new MessagePush();
            mes.setId(rs.getInt("ID"));
            mes.setPlaceId(rs.getInt("PLACEID"));
            mes.setFloorNo(rs.getBigDecimal("FLOORNO"));
            mes.setPushRight(rs.getDouble("PUSHRIGHT"));
            mes.setPushWrong(rs.getDouble("PUSHWRONG"));
            mes.setNotPush(rs.getDouble("NOTPUSH"));
            mes.setCenterRadius(rs.getString("CENTERRADIUS"));
            mes.setCenterReality(rs.getString("CENTERREALITY"));
            mes.setIsRigth(rs.getInt("ISRIGTH"));
            mes.setPlace(rs.getString("place"));
            mes.setFloor(rs.getString("floor"));
            mes.setUpdateTime(rs.getLong("updateTime"));
            return mes;
        }
    }

    public Collection<MessagePush> getDataByPlaceIdTime(long startTime, long endTime, String placeId)
    {
        
        String sql = "select a.*,b.floor,c.deviation, d.name place from messagepush a left join maps b "
            + "on a.floorNo = b.floorNo left join store d on a.placeId = d.id left join estimatedev c on b.floorNo = c.floorNo "
            + "where a.placeId = ? and a.updateTime > ? and a.updateTime< ?";
        return jdbcTemplate.query(sql, new Object[] {placeId, startTime, endTime}, new MessagePushMapper());
    }  
    
    public Collection<MessagePush> getAllDatas(long startTime, long endTime)
    {
        
        String sql = "select a.*,b.floor,c.deviation, d.name place from messagepush a left join maps b "
            + "on a.floorNo = b.floorNo left join store d on a.placeId = d.id left join estimatedev c on b.floorNo = c.floorNo "
            + " where a.updateTime > ? and a.updateTime< ? ";
        return jdbcTemplate.query(sql,new Object[]{startTime,endTime},new MessagePushMapper());
    }   
    
    public List<MessagePush> getAllData()
    {
        String sql = "select a.*,b.name place,d.floor from messagepush a "
                + "left join store b on a.placeId = b.id "
                + "left join maps d on a.floorNo = d.floorNo ";
        return this.jdbcTemplate.query(sql, new MessagePushMapper());
    }

    public List<MessagePush> getAllDataByStoreId(int storeid)
    {
        String sql = "select a.*,b.name place,d.floor from messagepush a "
                + "left join store b on a.placeId = b.id "
                + "left join maps d on a.floorNo = d.floorNo where a.placeId ="
                + storeid;
        return this.jdbcTemplate.query(sql, new MessagePushMapper());
    }

    public int savaMessagePush(MessagePush aam) throws SQLException
    {
        String sql = "INSERT INTO messagepush(placeId,floorNo,pushRight,pushWrong,notPush,centerRadius,centerReality,isRigth,updateTime) VALUES(?,?,?,?,?,?,?,?,?)";
        return this.jdbcTemplate.update(sql, aam.getPlaceId(),
                aam.getFloorNo(), aam.getPushRight(), aam.getPushWrong(),
                aam.getNotPush(), aam.getCenterRadius(),
                aam.getCenterReality(), aam.getIsRigth(), aam.getUpdateTime());
    }

}
