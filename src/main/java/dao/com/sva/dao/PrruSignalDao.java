/**   
 * @Title: PrruSignalDao.java 
 * @Package com.sva.dao 
 * @Description: PrruSignalDao数据库操作类
 * @author labelCS   
 * @date 2016年9月27日 上午10:49:15 
 * @version V1.0   
 */
package com.sva.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.sva.model.FeatureBaseExportModel;
import com.sva.model.PrruFeatureDetailModel;
import com.sva.model.PrruFeatureModel;
import com.sva.model.PrruSignalModel;

/** 
 * @ClassName: PrruSignalDao 
 * @Description: PrruSignalDao数据库操作类
 * @author labelCS 
 * @date 2016年9月27日 上午10:49:15 
 * 
 */
public class PrruSignalDao {
    private JdbcTemplate jdbcTemplate;

    // 注入DataSource
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** 
     * @Title: getSignalByUserIdTime 
     * @Description: 获取符合条件的信号数据
     * @param userId
     * @param timestamp
     * @return 
     */
    public List<PrruSignalModel> getSignalByUserIdTime(String userId, long timestamp){
        String sql = "SELECT * FROM prruSignal WHERE timestamp > ? and userId = ? order by timestamp";
        
        return jdbcTemplate.query(sql, new PrruMapper(), timestamp, userId);
    }
    
    /** 
     * @Title: getOneSignalByUserIdTime 
     * @Description: 获取符合条件的一个时间戳的数据
     * @param userId
     * @param timestamp
     * @return 
     */
    public List<PrruSignalModel> getOneSignalByUserIdTime(String userId, long timestamp){
        String sql = "SELECT * FROM prruSignal WHERE userId = ? and timestamp = "
                + "(SELECT timestamp from prruSignal WHERE timestamp > ? and userId = ? order by timestamp limit 1)";
        
        return jdbcTemplate.query(sql, new PrruMapper(), userId, timestamp, userId);
    }
    
    /** 
     * @Title: getCurrentSignalByUserId 
     * @Description: 获取当前指定用户的信号数据
     * @param userId
     * @return 
     */
    public List<PrruSignalModel> getCurrentSignalByUserId(String userId){
        String sql = "SELECT * FROM prruSignal WHERE userId = ? and timestamp = "
                + "(SELECT timestamp from prruSignal WHERE userId = ? order by timestamp desc limit 1)";
        
        return jdbcTemplate.query(sql, new PrruMapper(), userId, userId);
    }
    
    /** 
     * @Title: getCurrentSignalByUserId 
     * @Description: 获取当前指定用户的2次信号数据
     * @param userId
     * @return 
     */
    public List<PrruSignalModel> getTwoSignalByUserId(String userId){
        String sql = "SELECT prruSignal.* FROM prruSignal join "
                + "(SELECT distinct(timestamp) from prruSignal WHERE userId = ? order by timestamp desc limit 2) a "
                + "WHERE prruSignal.userId = ? and prruSignal.timestamp = a.timestamp;";
        
        return jdbcTemplate.query(sql, new PrruMapper(), userId, userId);
    }
    
    /** 
     * @Title: getRelativeFeature 
     * @Description: 获取相关的特征库 
     * @param gpps
     * @return 
     */
    public List<PrruFeatureModel> getRelativeFeature(List<String> gpps, String floorNo){
        StringBuilder temp = new StringBuilder();
        for(int i=0; i<gpps.size(); i++){
            temp.append("?,");
        }
        temp.deleteCharAt(temp.length()-1);
        String sql = "select a.*,b.gpp,b.featureValue from prruFeature a join "
                + "(select * from prruFeatureDetail where gpp in ("+temp+")) b on a.id = b.featureId "
                + " where a.floorNo = "+floorNo;
        Object[] param = gpps.toArray();
        return jdbcTemplate.query(sql, param, new FeatureMapperAll());
    }
    
    /** 
     * @Title: deleteSignal 
     * @Description: 清空信号表
     * @return 
     */
    public int deleteSignal(){
        String sql = "DELETE FROM prrusignal";
        return jdbcTemplate.update(sql);
    }
    
    /** 
     * @Title: savePrruFeature 
     * @Description: 特征库入库 
     * @param pm
     * @return 
     */
    public int savePrruFeature(final PrruFeatureModel pm){
        final String sql = "INSERT INTO prrufeature(x,y,floorNo,checkValue,featureRadius,userId,timestamp,eNodeBid) VALUES (?,?,?,?,?,?,?,?)";
        KeyHolder key = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator(){

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException{

                PreparedStatement preState = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);

                preState.setString(1,pm.getX());
                preState.setString(2,pm.getY());
                preState.setString(3,pm.getFloorNo());
                preState.setBigDecimal(4,pm.getCheckValue());
                preState.setBigDecimal(5,pm.getFeatureRadius());
                preState.setString(6,pm.getUserId());
                preState.setLong(7,pm.getTimestamp());
                preState.setString(8,pm.geteNodeBid());

                return preState;

            }

        },key);

        //从主键持有者中获得主键值
        return key.getKey().intValue();
    }
    
    /** 
     * @Title: savePrruFeatureDetail 
     * @Description: 详细特征值数据入库
     * @param pdm 
     */
    public void savePrruFeatureDetail(List<PrruFeatureDetailModel> pdm){
        final List<PrruFeatureDetailModel> temp = pdm;
        String sql = "INSERT INTO prruFeatureDetail(featureId,gpp,featureValue) VALUES (?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps,int i)throws SQLException{
                String gpp = temp.get(i).getGpp();
                BigDecimal featureValue = temp.get(i).getFeatureValue();
                int id = temp.get(i).getFeatureid();
                ps.setInt(1, id);
                ps.setString(2, gpp);
                ps.setBigDecimal(3, featureValue);
            }
            @Override
            public int getBatchSize(){
                return temp.size();
            }
        });
    }
    
    /** 
     * @Title: getFeatureByPosition 
     * @Description: 查询指定位置是否已存在特征库信息
     * @param x
     * @param y
     * @param floorNo
     * @return 
     */
    public PrruFeatureModel getFeatureByPosition(BigDecimal x, BigDecimal y, BigDecimal floorNo){
        PrruFeatureModel result;
        String sql = "SELECT * FROM prruFeature WHERE x=? and y=? and floorNo=? limit 1";
        try{
            result = jdbcTemplate.queryForObject(sql, new FeatureMapper(), x,y,floorNo);
        }catch(DataAccessException e){
            result = new PrruFeatureModel();
        }
        return result;
    }
    
    /** 
     * @Title: getFeatureByPosition 
     * @Description: TODO 临时使用，后期删除
     * @param x
     * @param y
     * @param floorNo
     * @return 
     */
    public PrruFeatureModel getFeatureByPositionTemp(BigDecimal x, BigDecimal y){
        PrruFeatureModel result;
        String sql = "SELECT * FROM prruFeature WHERE x=? and y=? limit 1";
        try{
            result = jdbcTemplate.queryForObject(sql, new FeatureMapper(), x,y);
        }catch(DataAccessException e){
            result = new PrruFeatureModel();
        }
        return result;
    }
    
    /** 
     * @Title: getFeatureBaseData 
     * @Description: 获取导出用特征库数据
     * @return 
     */
    public List<FeatureBaseExportModel> getFeatureBaseData(String placeId){
        String sql = "SELECT a.*,b.gpp,b.featureValue,c.floorid,c.mapId FROM prruFeature a "
                + " join prruFeatureDetail b on a.id = b.featureId "
                + " left join maps c on a.floorNo = c.floorNo "
                + " left join store d on c.placeId = d.id "
                + " WHERE  d.id = ? order by a.id;";
        
        return jdbcTemplate.query(sql, new FeatureBaseMapper(),placeId);
    }
    
    /** 
     * @Title: deletFeatureById 
     * @Description: 删除指定id的特征库信息 
     * @param id
     * @return 
     */
    public int deletFeatureById(int id){
        String sql = "DELETE FROM prruFeature WHERE id = ?";
        return jdbcTemplate.update(sql,id);
    }
    
    /** 
     * @Title: deleteFeatureDetailByFeatureId 
     * @Description: 删除指定特征库对应的特征值信息 
     * @param featureId
     * @return 
     */
    public int deleteFeatureDetailByFeatureId(int featureId){
        String sql = "DELETE FROM prruFeatureDetail WHERE featureId = ?";
        return jdbcTemplate.update(sql,featureId);
    }
    
    /** 
     * @Title: getAllFeaturePostion 
     * @Description: 获取指定楼层的特征点
     * @param floorNo
     * @return 
     */
    public List<PrruFeatureModel> getAllFeaturePostion(String floorNo){
        String sql = "SELECT * FROM prruFeature WHERE floorNo = ?";
        return jdbcTemplate.query(sql, new FeatureMapper(), floorNo);
    }
    
    /** 
     * @Title: queryLocationByUseId 
     * @Description: 获取用户所在的楼层
     * @param userId
     * @return 
     */
    public String queryFloorNoByUseId(String userId)
    {
        String sql = "select z from locationPhone where userID='"
                + userId
                + "' order by timestamp desc limit 1";

        return this.jdbcTemplate.queryForObject(sql, String.class);
    }
    
    private class PrruMapper implements RowMapper<PrruSignalModel>
    {
        public PrruSignalModel mapRow(ResultSet rs, int num) throws SQLException
        {
            PrruSignalModel prru = new PrruSignalModel();
            prru.setId(rs.getInt("ID"));
            prru.setEnbid(rs.getString("ENBID"));
            prru.setGpp(rs.getString("GPP"));
            prru.setRsrp(rs.getBigDecimal("RSRP"));
            prru.setTimestamp(rs.getLong("TIMESTAMP"));
            prru.setUserId(rs.getString("USERID"));

            return prru;
        }
    }
    
    private class FeatureMapperAll implements RowMapper<PrruFeatureModel>
    {
        public PrruFeatureModel mapRow(ResultSet rs, int num) throws SQLException
        {
            PrruFeatureModel feature = new PrruFeatureModel();
            feature.setId(rs.getInt("ID"));
            feature.setX(rs.getString("X"));
            feature.setY(rs.getString("Y"));
            feature.setUserId(rs.getString("USERID"));
            feature.setFloorNo(rs.getString("FLOORNO"));
            feature.setGpp(rs.getString("GPP"));
            feature.setFeatureValue(rs.getBigDecimal("FEATUREVALUE"));
            feature.setCheckValue(rs.getBigDecimal("CHECKVALUE"));
            feature.setFeatureRadius(rs.getBigDecimal("FEATURERADIUS"));
            feature.setTimestamp(rs.getLong("TIMESTAMP"));

            return feature;
        }
    }
    
    private class FeatureMapper implements RowMapper<PrruFeatureModel>
    {
        public PrruFeatureModel mapRow(ResultSet rs, int num) throws SQLException
        {
            PrruFeatureModel feature = new PrruFeatureModel();
            feature.setId(rs.getInt("ID"));
            feature.setX(rs.getString("X"));
            feature.setY(rs.getString("Y"));
            feature.setUserId(rs.getString("USERID"));
            feature.setFloorNo(rs.getString("FLOORNO"));
            feature.setCheckValue(rs.getBigDecimal("CHECKVALUE"));
            feature.setFeatureRadius(rs.getBigDecimal("FEATURERADIUS"));
            feature.setTimestamp(rs.getLong("TIMESTAMP"));

            return feature;
        }
    }
    
    private class FeatureBaseMapper implements RowMapper<FeatureBaseExportModel>
    {
        public FeatureBaseExportModel mapRow(ResultSet rs, int num) throws SQLException
        {
            FeatureBaseExportModel featureBase = new FeatureBaseExportModel();
            featureBase.setId(rs.getInt("ID"));
            featureBase.setX(rs.getString("X"));
            featureBase.setY(rs.getString("Y"));
            featureBase.setUserId(rs.getString("USERID"));
            featureBase.setFloorid(rs.getString("FLOORID"));
            featureBase.setCheckValue(rs.getBigDecimal("CHECKVALUE"));
            featureBase.setFeatureRadius(rs.getBigDecimal("FEATURERADIUS"));
            featureBase.setTimestamp(rs.getLong("TIMESTAMP"));
            featureBase.setGpp(rs.getString("GPP"));
            featureBase.setFeatureValue(rs.getBigDecimal("FEATUREVALUE"));
            featureBase.setMapId(rs.getInt("MAPID"));

            return featureBase;
        }
    }
    
    /**
     * 根据楼层关联删除模拟点（userid=‘simulate’）数据
     * 
     * @param floorNo
     * @return
     */
    public int delSimulateDataByFloorNo(String floorNo, String eNodeBid) {
        String sql = "DELETE prrufeature,prrufeaturedetail from prrufeature LEFT JOIN prrufeaturedetail on prrufeature.id = prrufeaturedetail.featureId where prrufeature.floorNo = ? and prrufeature.eNodeBid = ? and prrufeature.userId = 'simulate'";
        if(eNodeBid.isEmpty()){
        	sql = "DELETE prrufeature,prrufeaturedetail from prrufeature LEFT JOIN prrufeaturedetail on prrufeature.id = prrufeaturedetail.featureId where prrufeature.floorNo = ? and prrufeature.userId = 'simulate'";
        	return jdbcTemplate.update(sql, floorNo);
        }
        return jdbcTemplate.update(sql, new Object[]{floorNo, eNodeBid});
    }
}
