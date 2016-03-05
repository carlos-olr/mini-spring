package br.com.spektro.minispring.core.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

import br.com.spektro.minispring.core.dbmapper.ConfigDBMapper;

/**
 * @author Carlos
 *
 * @version 1.0.1
 */
public class GeradorIdService {

	private static GeradorIdService instance;

	private GeradorIdService() {
	}

	/**
	 * @return inst�ncia de GeradorIdService
	 */
	private static GeradorIdService getInstance() {
		if (instance == null) {
			instance = new GeradorIdService();
		}
		return instance;
	}

	/**
	 * @param tableName
	 * @return novo ID ainda n�o utilizado
	 */
	// Agora iremos passar como argumento o nome da tabela alvo
	public static synchronized Long getNextId(String tableName) {
		Connection connection = null;
		PreparedStatement query = null;
		PreparedStatement updateID = null;
		try {
			// Iremos considerar essa tabela no SQL que ir� retornar o NEXT_ID
			connection = ConfigDBMapper.getDefaultConnection();
			query = connection.prepareStatement(
					"SELECT NEXT_ID FROM GERADOR_IDS WHERE TABLE_NAME = ?;");
			query.setString(1, tableName); // N�o se esque�a de fazer o set da
											// 'tableName'
			ResultSet resultNextId = query.executeQuery();
			resultNextId.next();
			Long idSequence = resultNextId.getLong("NEXT_ID");
			// Agora iremos atualizar o valor do NEXT_ID na Tabela, tamb�m
			// utilizando o tableName
			updateID = connection.prepareStatement(
					"UPDATE GERADOR_IDS SET NEXT_ID = ? WHERE TABLE_NAME = ?;");
			updateID.setLong(1, idSequence + 1); // o '+ 1' � para incrementar o
													// nextId
			updateID.setString(2, tableName); // N�o se esque�a de fazer o set
												// da 'tableName'
			updateID.execute();
			return idSequence;
		} catch (SQLException e) {
			throw new RuntimeException(
					"Erro ao pegar ID para a tabela '" + tableName + "'", e);
		} finally {
			DbUtils.closeQuietly(updateID);
			DbUtils.closeQuietly(query);
			DbUtils.closeQuietly(connection);
		}
	}

}
