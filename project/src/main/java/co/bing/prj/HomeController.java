package co.bing.prj;

import java.sql.SQLException;
import java.util.Locale;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.bing.prj.user.dao.UserDao;
import co.bing.prj.user.dao.cm.impl.CountingConnectionMaker;
import co.bing.prj.user.dao.fac.CountingDaoFactory;
import co.bing.prj.user.domain.User;

@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws ClassNotFoundException, SQLException {
		
		ApplicationContext context =
				new AnnotationConfigApplicationContext(CountingDaoFactory.class);
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		User user = new User();
		user.setId("whiteship");
		user.setName("지수빈");
		user.setPassword("married");
		
		dao.add(user);
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getId() + " 조회 성공");
		
		CountingConnectionMaker ccm = context.getBean("connectionMaker", CountingConnectionMaker.class);
		System.out.println("Connection counter : " + ccm.getCounter());
		
		return "home";
	}
	
}
