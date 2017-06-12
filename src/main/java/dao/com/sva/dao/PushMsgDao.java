/**   
 * @Title: PushMsgDao.java 
 * @Package com.sva.dao 
 * @Description: 服务器推送消息dao 
 * @Company:ICS
 * @author label  
 * @date 2016年7月28日 上午11:15:47 
 * @version V1.0 
 */
package com.sva.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>Title:PushMsgDao</p>
 * <p>Description:服务器推送消息dao </p>
 * <p>Company: ICS</p>
 * @author label
 * @date 2016年7月28日 上午11:15:47
 */
public class PushMsgDao {
	/**
	 * @Fields jdbcTemplate
	 */
	private JdbcTemplate jdbcTemplate;

    /** 
     * @Title: setDataSource 
     * @Description: 注入DataSource
     * @param dataSource void   
     * @throws 
     */
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** 
     * @Title: getMessageByUserId 
     * @Description: 通过userid查询出对应的消息
     * @param userId
     * @return
     * @throws SQLException List<String>   
     * @throws 
     */
    public List<Map<String,Object>> getMessageByUserId(String userId)
    {
        String sql = "select * from pushMsg where userId = ?";
        Object[] params = {userId};
        return this.jdbcTemplate.queryForList(sql, params);
    }
    
    /** 
     * @Title: saveMessage 
     * @Description: 保存消息
     * @param userId
     * @param content
     * @throws SQLException void   
     * @throws 
     */
    public void saveMessage(String userId, String content) throws SQLException
    {
        String sql = "INSERT INTO pushMsg(userId,content) VALUES(?,?)";
        this.jdbcTemplate.update(sql, userId, content);
    }
    
    /** 
     * @Title: deleteMessageByUserId 
     * @Description: 根据id删除消息
     * @param userId
     * @throws SQLException void   
     * @throws 
     */
    public void deleteMessageById(String id) throws SQLException
    {
        String sql = "delete from pushMsg where id = ?";
        this.jdbcTemplate.update(sql, id);
    }
}
