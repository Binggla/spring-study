package toby.user.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static toby.user.service.impl.UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER;
import static toby.user.service.impl.UserLevelUpgradePolicyImpl.MIN_RECCOMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import toby.user.dao.UserDao;
import toby.user.domain.Level;
import toby.user.domain.User;
import toby.user.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	
	@Autowired private UserDao dao;
	@Autowired UserService userService;
	@Autowired PlatformTransactionManager transactionManager;
	
	List<User> users;
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
				new User("erwins", "신승한", "p3", Level.SILVER, MIN_RECCOMEND_FOR_GOLD-1, 29),
				new User("madnite1", "이상호", "p4", Level.SILVER, MIN_RECCOMEND_FOR_GOLD, 30),
				new User("green", "오민규", "p5", Level.GOLD, Integer.MAX_VALUE, 100)
			);
	}
	
	static class TestUserService extends UserService {
		private String id;
		
		private TestUserService(String id) {
			this.id = id;
		}
		
		//UserService의 메소드를 오버라이드
		protected void upgradeLevel(User user) {
			//지정된 id의 User 오브젝트가 발견되면 예외를 던져서 작업을 강제로 중단시킨다.
			if (user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
	}

	static class TestUserServiceException extends RuntimeException {
		
	}
	
	@Test
	public void upgradeAllorNothing() throws Exception {
		//예외를 발생시킬 네 번째 사용자의 id를 넣어서 테스트용 UserService 대역 오브젝트를 생성한다.
		UserService testUserService = new TestUserService(users.get(3).getId());
		testUserService.setUserDao(this.dao); //수동 DI
		testUserService.setTransactionManager(this.transactionManager);
		
		dao.deleteAll();
		for (User user : users) dao.add(user);
		
		try {
			//TestUserService는 업그레이드 작업 중에 예외가 발생해야 한다. 정상 종료라면 문제가 있으니 실패이다.
			testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		
		//TestUserService가 던지는 예외를 잡아서 계속 진행되도록 한다. 그 외의 예외라면 테스트는 실패이다.
		} catch(TestUserServiceException e) {
			
		}
		
		//예외가 발생하기 전에 레벨 변경이 있었던 사용자의 레벨이 처음 상태로 바뀌었나 확인한다.
		checkLevelUpgraded(users.get(1), false);
	}
	
	@Test
	public void add() {
		dao.deleteAll();
		
		User userWithLevel = users.get(4);		//gold레벨. 레벨이 이미 지정된 유저라면 레벨을 초기화하지 않아야 한다.
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);		//레벨이 비어있는 사용자. 로직에 따라 등록 중에 BASIC 레벨로 설정돼야 한다.
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = dao.get(userWithLevel.getId());
		User userWithoutLevelRead = dao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));
	}
	
	@Test
	public void upgradeLevels() throws Exception {
		dao.deleteAll();
		for (User user : users) dao.add(user);
		
		userService.upgradeLevels();
		
		checkLevelUpgraded(users.get(0), false);
		checkLevelUpgraded(users.get(1), true);
		checkLevelUpgraded(users.get(2), false);
		checkLevelUpgraded(users.get(3), true);
		checkLevelUpgraded(users.get(4), false);
	}
	
	//어떤 레벨로 바뀔 것인가가 아니라, 다음 레벨로 업그레이드 될 것인가 아닌가를 지정한다.
	public void checkLevelUpgraded(User user, boolean upgraded) {
		User userUpdate = dao.get(user.getId());
		if (upgraded) {
			//업그레이드가 일어났는지 확인
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
			
		} else {
			//업그레이드가 일어나지 않았는지 확인
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	public void checkLevel(User user, Level expectedLevel) {
		User userUpdate = dao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
	@Test
	public void bean() {
		assertThat(this.userService, is(notNullValue()));
	}
}
