package toby.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;

import toby.user.dao.cm.ConnectionMaker;
import toby.user.domain.User;

public class UserDao {
	//ConnectionMaker 사용
	private ConnectionMaker connectionMaker;

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}
	
	//DataSource 사용
	private DataSource dataSource;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException {
		//Connection c = connectionMaker.makeConnection();
		
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"INSERT INTO USERS(ID, NAME, PASSWORD) "
				+ "VALUES (?,?,?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException {
		//Connection c = connectionMaker.makeConnection();
		
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"SELECT * FROM USERS WHERE ID = ?");
		ps.setString(1, id);
		
		User user = null;
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
		}
		
		rs.close();
		ps.close();
		c.close();
		
		if (user == null) throw new EmptyResultDataAccessException(1);
		
		return user;
	}
	
	public void deleteAll() throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"DELETE FROM USERS");
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public int getCount() throws SQLException {
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"SELECT COUNT(*) FROM USERS");
		
		ResultSet rs = ps.executeQuery();
		rs.next();

		int count = rs.getInt(1);
		
		rs.close();
		ps.close();
		c.close();
		
		return count;
	}
}