package br.com.spektro.minispring.core.query;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.dbutils.DbUtils;

import br.com.spektro.minispring.core.dbmapper.ConfigDBMapper;

public class QueryExecutorService {

	private static QueryExecutorService instance;

	private QueryExecutorService() {
	}

	private static QueryExecutorService getinstance() {
		if (instance == null) {
			instance = new QueryExecutorService();
		}
		return instance;
	}

	public static void executeQuery(String query) {
		Connection connection = ConfigDBMapper.getDefaultConnection();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(query);
			statement.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			DbUtils.closeQuietly(connection);
			DbUtils.closeQuietly(statement);
		}
	}
}
