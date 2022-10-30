package co.bing.prj.user.dao.cm.impl;

import java.sql.Connection;
import java.sql.SQLException;

import co.bing.prj.user.dao.cm.ConnectionMaker;

public class CountingConnectionMaker implements ConnectionMaker {

	int counter = 0;
	private ConnectionMaker realConnectionMaker;
	
	public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
		this.realConnectionMaker = realConnectionMaker;
	}

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		this.counter++;
		return realConnectionMaker.makeConnection();
	}
	
	public int getCounter() {
		return this.counter;
	}

}