package com.sva.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.sva.model.TicketModel;

@SuppressWarnings("all")
public class TicketDao
{
    private JdbcTemplate jdbcTemplate;

    // 注入DataSource
    public void setDataSource(DataSource dataSource)
    {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public Collection<TicketModel> getAllTicket(String msgId)
    {
        String sql = "select a.*,b.shopName name from ticket a left join message b on b.id = a.msgId where a.msgId ="+msgId;
        return this.jdbcTemplate.query(sql, new ticketModel1());
    }
    
    public Collection<TicketModel> getAllTicketById(int placeId,String msgId)
    {
        String sql = "select a.* from ticket a left join message b on b.id = a.msgId left join store c on c.id = b.placeId where b.placeId = "+placeId+" and a.msgId = "+msgId;
        return this.jdbcTemplate.query(sql, new ticketModel());
    }

    public void saveTicket(TicketModel model)
    {
        String sql = "insert into ticket(msgId,chances,ticketPath) values(?,?,?)";
        this.jdbcTemplate.update(sql,model.getMsgId(),model.getChances(),model.getTicketPath());
    }
    public void updataTicket(TicketModel model)
    {
        String sql = "update ticket set msgId=?,chances=?,ticketPath=? where id=?";
        this.jdbcTemplate.update(sql,model.getMsgId(),model.getChances(),model.getTicketPath(),model.getTicketId());
    }
    private class ticketModel implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            TicketModel tic = new TicketModel();
            tic.setChances(rs.getString("CHANCES"));
            tic.setTicketPath(rs.getString("TICKETPATH"));
            tic.setMsgId(rs.getString("MSGID"));
            tic.setTicketId(rs.getString("ID"));

            return tic;
        }
    }
    private class ticketModel1 implements RowMapper
    {
        public Object mapRow(ResultSet rs, int num) throws SQLException
        {
            TicketModel tic = new TicketModel();
            tic.setChances(rs.getString("CHANCES"));
            tic.setTicketPath(rs.getString("TICKETPATH"));
            tic.setMsgId(rs.getString("MSGID"));
            tic.setTicketId(rs.getString("ID"));
            tic.setName(rs.getString("NAME"));
            
            return tic;
        }
    }

   
}
