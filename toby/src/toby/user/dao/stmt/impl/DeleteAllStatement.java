package toby.user.dao.stmt.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import toby.user.dao.stmt.StatementStrategy;

public class DeleteAllStatement implements StatementStrategy {

	@Override
	public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
		PreparedStatement ps = c.prepareStatement("DELETE FROM USERS");
		
		return ps;
	}

}
