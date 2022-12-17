package toby.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import toby.user.domain.Level;
import toby.user.domain.User;

public class UserDaoJdbc implements UserDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private RowMapper<User> userMapper =
		new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getString("id")); 
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setLevel(Level.valueOf(rs.getInt("level")));
				user.setLogin(rs.getInt("login"));
				user.setRecommend(rs.getInt("recommend"));
				
				return user;
			}
		};
	
	public void add(final User user) {
		this.jdbcTemplate.update("INSERT INTO USERS(ID, NAME, PASSWORD, \"LEVEL\", LOGIN, RECOMMEND) VALUES (?,?,?,?,?,?)",
				user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query(
			"SELECT * FROM USERS ORDER BY ID", 
			this.userMapper);
	}
	
	public User get(String id) {
		return this.jdbcTemplate.queryForObject(
			"SELECT * FROM USERS WHERE ID = ?", 
			new Object[] {id},
			this.userMapper);
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update("DELETE FROM USERS");
		
	}
	
	public int getCount() {
		return this.jdbcTemplate.queryForInt("SELECT COUNT(*) FROM USERS");
	}

	@Override
	public void update(User user) {
		this.jdbcTemplate.update(
			"UPDATE USERS "
			+ "SET  NAME = ? "
			+ "	    ,PASSWORD = ? "
			+ "     ,\"LEVEL\" = ? "
			+ "     ,LOGIN = ? "
			+ "     ,RECOMMEND = ? "
			+ "WHERE ID = ? ",
			user.getName(), user.getPassword(), user.getLevel().intValue(), 
			user.getLogin(), user.getRecommend(), user.getId());
	}
}