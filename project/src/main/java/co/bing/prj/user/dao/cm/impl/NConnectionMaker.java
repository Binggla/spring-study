package co.bing.prj.user.dao.cm.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.bing.prj.user.dao.cm.ConnectionMaker;

public class NConnectionMaker implements ConnectionMaker {

	@Override
	public Connection makeConnection() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection c = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521:xe", "bing", "bing");
		
		return c;
	}

}
