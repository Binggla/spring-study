package toby.user.service.impl;

import toby.user.dao.UserDao;
import toby.user.domain.Level;
import toby.user.domain.User;
import toby.user.service.UserLevelUpgradePolicy;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy {
	UserDao userDao;
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public boolean canUpgradeLevel(User user) {
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

	@Override
	public void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
