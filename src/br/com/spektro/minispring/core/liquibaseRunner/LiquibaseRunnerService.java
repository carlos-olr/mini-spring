package br.com.spektro.minispring.core.liquibaseRunner;

import java.sql.Connection;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import br.com.spektro.minispring.core.dbmapper.ConfigDBMapper;
import br.com.spektro.minispring.core.implfinder.ContextSpecifier;

/**
 * @author carlosolr
 * 
 * @version 1.0.0
 */
public class LiquibaseRunnerService {

	public static void run() {
		Connection conn = ConfigDBMapper.getDefaultConnection();
		try {
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
					new JdbcConnection(conn));
			Liquibase liquibase = new Liquibase(ContextSpecifier.getContextAsFile() + "/liquibase/changelog-master.xml",
					new ClassLoaderResourceAccessor(), database);
			liquibase.forceReleaseLocks();
			liquibase.update(ConfigDBMapper.getDefaultConnectionName());
			conn.close();
		} catch (Exception e) {
			throw new RuntimeException("Erro na execução do Liquibase", e);
		}
	}
}
