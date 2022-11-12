package toby.test;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import toby.user.dao.UserDao;
import toby.user.dao.cm.impl.CountingConnectionMaker;
import toby.user.dao.fac.DaoFactory;
import toby.user.domain.User;

public class userDaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context =
				new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("jeeesubb");
		user.setName("지수빈");
		user.setPassword("married");
	
		dao.add(user);
		System.out.println(user.getId() + " 등록 성공");
	
		User user2 = dao.get(user.getId());
		System.out.println(user2.getId() + " 조회 성공");
	
		//CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		//System.out.println("Connection counter : " + ccm.getCounter());
	}
}
