package toby.user.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import toby.user.dao.UserDao;
import toby.user.domain.Level;
import toby.user.domain.User;

public class UserService {
	UserDao userDao;
	private PlatformTransactionManager transactionManager;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public void upgradeLevels() { 
		//DI받은 트랜잭션 매니저를 공유해서 사용한다. 멀티스레드 환경에서도 안전하다.
		TransactionStatus status =
				this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		
		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if (canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			
			this.transactionManager.commit(status);
			
		} catch (Exception e) {
			this.transactionManager.rollback(status);
			throw e;
		} 
	}
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		
		switch(currentLevel) {
			//레벨별로 구분해서 조건을 판단한다.
			case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
			case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
			case GOLD: return false;
			
			//현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다. 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 발생한다.
			default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	public void add(User user) {
		if (user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
}
