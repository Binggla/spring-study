package co.bing.prj.user.dao.fac;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.bing.prj.user.dao.UserDao;
import co.bing.prj.user.dao.cm.ConnectionMaker;
import co.bing.prj.user.dao.cm.impl.CountingConnectionMaker;
import co.bing.prj.user.dao.cm.impl.NConnectionMaker;

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
