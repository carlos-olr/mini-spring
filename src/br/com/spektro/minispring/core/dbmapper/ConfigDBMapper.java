package br.com.spektro.minispring.core.dbmapper;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Strings;

import br.com.spektro.minispring.api.dbmapper.ConfigConnection;
import br.com.spektro.minispring.api.dbmapper.DBMapperException;
import br.com.spektro.minispring.core.implfinder.ContextSpecifier;

/**
 * @author Carlos
 * 
 * @version
 */
public class ConfigDBMapper {

	// Inst�ncia �nica para garantir a reutiliza��o das connections
	private static ConfigDBMapper instance;
	// um class loader para encontrar facilmente o arquivo de configs
	private static final ClassLoader loader = ConfigDBMapper.class.getClassLoader();
	// Mapa de connections j� existentes, permitindo que toda connection, depois
	// de devidametne criada, possa ser reutilizada sempre que necess�rio.
	private static Map<String, ConfigConnection> configConnections = new HashMap<String, ConfigConnection>();
	// Propriedade utilizada para definir qual ser� o nome da conex�o padr�o,
	// podendo assim variar para testes ou produ��o
	private String defaultConnectionName;
	// Lista de configs dispon�veis para utiliza��o
	private List<String> possibleConfigs;

	private ConfigDBMapper() {
	}

	/**
	 * @return instancia de ConfigDBMapper
	 */
	private static ConfigDBMapper getInstance() {
		if (instance == null) {
			instance = new ConfigDBMapper();
			instance.loadConnections();
		}
		return instance;
	}

	/**
	 * @param configName
	 * @return {@link Connection} gerada a partir do arquivo de configura��o que
	 *         possua a 'configName' passada ou exception caso essa configura��o
	 *         n�o existisse no momento de load das configs
	 */
	public static Connection getConnectionByConfig(String configName) {
		if (!Strings.isNullOrEmpty(configName)
				&& configConnections.containsKey(configName)) {
			ConfigConnection configConnection = configConnections.get(configName);
			try {
				return DriverManager.getConnection(configConnection.getUrl(),
						configConnection.getLogin(), configConnection.getPassword());
			} catch (SQLException e) {
				throw new DBMapperException(
						"N�o foi poss�vel geral uma conex�o pra o Banco de Dados '"
								+ configName + "'.",
						e);
			}
		}
		throw new DBMapperException(
				"N�o existe configura��o com nome '" + configName + "'.");
	}

	/**
	 * Carrega as configura��es do databases.json e cria uma conex�o para cada
	 * configura��o existente, al�m de as colocar no mapa de conex�es
	 */
	public void loadConnections() {
		Properties prop = new Properties();

		try {
			// O primeiro passo para carregar as configura��es � abrir o
			// arquivo de configura��es. Para isso vamos utilizar nosso 'loader'
			// para pegar o 'path' do mesmo.
			String context = ContextSpecifier.getContext().replace(".", "/");
			String path = loader.getResource(context + "/core/databases.properties")
					.getPath();
			// Com 'path em m�os iremos converter, fazer 'parse', de seu
			// conte�do para um objeto JSONArray, j� que o arquivo come�a
			// com '[..]', ou seja, o arquivo � um array.
			prop.load(new FileReader(path));

			if (!getInstance().isValid(prop)) {
				throw new DBMapperException(
						"� necess�rio ao menos uma configura��o de Banco de Dados");
			}

			int length = prop.size() / 5;

			for (int i = 1; i <= length; i++) {
				String keyBase = "database" + i + ".";
				Class dbClass = Class
						.forName(prop.getProperty(keyBase + "driverClassName"));
				configConnections.put(prop.getProperty(keyBase + "name"),
						new ConfigConnection(prop.getProperty(keyBase + "name"),
								prop.getProperty(keyBase + "url"),
								prop.getProperty(keyBase + "login"),
								prop.getProperty(keyBase + "password"), dbClass));
			}
			getInstance().possibleConfigs = new ArrayList<String>(
					configConnections.keySet());
		} catch (Exception e) {
			throw new DBMapperException(
					"Erro ao carregar configura��es de banco de dados", e);
		}
	}

	private boolean isValid(Properties prop) {
		return prop.size() % 5 == 0;
	}

	/**
	 * @return {@link Connection} gerada a partir da propriedade
	 *         defaultConnectionName, ou null caso n�o esteja configurada
	 */
	public static Connection getDefaultConnection() {
		if (getInstance().defaultConnectionName == null) {
			return null;
		}
		return getInstance().getConnectionByConfig(getInstance().defaultConnectionName);
	}

	public static Class getDefaultConnectionType() {
		if (getInstance().defaultConnectionName == null) {
			return null;
		}
		return getInstance().configConnections.get(getInstance().defaultConnectionName)
				.getDbClass();
	}

	/**
	 * Esse m�todo possui uma itelig�ncia que pode parecer uma pegadinha. Ele
	 * faz com que a propriedade 'defaultConnectionName' receba valor uma �nica
	 * vez, com isso se torna imposs�vel que por acidente a configura��o padr�o
	 * de banco seja alterada. Al�m disso esse m�todo tamb�m verifica se a
	 * 'config' passada existe na lista de poss�veis conex�es, caso n�o um
	 * exce��o � lan�ada.
	 * 
	 * @param config
	 */
	public static void setDefaultConnectionName(String config) {
		if (getInstance().defaultConnectionName == null
				&& (!Strings.isNullOrEmpty(config))) {
			if (getInstance().possibleConfigs.contains(config)) {
				getInstance().defaultConnectionName = config;
			} else {
				throw new DBMapperException(
						"N�o existe configura��o com nome '" + config + "'.");
			}
		}
	}

	public static String getDefaultConnectionName() {
		return getInstance().defaultConnectionName;
	}

	/**
	 * @return lista com todos os nomes de configura��es dispon�veis, essa lista
	 *         � criada a patir do m�todo 'loadConnections'
	 */
	public static List<String> getPossibleConfigs() {
		return getInstance().possibleConfigs;
	}

}
