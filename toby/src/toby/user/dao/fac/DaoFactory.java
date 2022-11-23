package toby.user.dao.fac;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import toby.user.dao.UserDaoJdbc;
import toby.user.dao.cm.ConnectionMaker;
import toby.user.dao.cm.impl.NConnectionMaker;

@Configuration
public class DaoFactory {
	@Bean
	public UserDaoJdbc userDao() {
		UserDaoJdbc userDao = new UserDaoJdbc();
		
		userDao.setDataSource(dataSource());
		
		return userDao;
	}
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(oracle.jdbc.driver.OracleDriver.class);
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
		dataSource.setUsername("bing");
		dataSource.setPassword("bing");
		
		return dataSource;
	}
	
}
