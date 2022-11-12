package toby.user.dao.fac;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import toby.user.dao.UserDao;
import toby.user.dao.cm.ConnectionMaker;
import toby.user.dao.cm.impl.CountingConnectionMaker;
import toby.user.dao.cm.impl.NConnectionMaker;

@Configuration
public class CountingDaoFactory {
	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker());
		
		return userDao;
	}
	
	@Bean
	public ConnectionMaker connectionMaker() {
		return new CountingConnectionMaker(realConnectionMaker());
	}
	
	@Bean
	public ConnectionMaker realConnectionMaker() {
		return new NConnectionMaker();
	}
}
