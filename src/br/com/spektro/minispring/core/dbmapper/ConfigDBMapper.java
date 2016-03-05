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

	// Instância única para garantir a reutilização das connections
	private static ConfigDBMapper instance;
	// um class loader para encontrar facilmente o arquivo de configs
	private static final ClassLoader loader = ConfigDBMapper.class.getClassLoader();
	// Mapa de connections já existentes, permitindo que toda connection, depois
	// de devidametne criada, possa ser reutilizada sempre que necessário.
	private static Map<String, ConfigConnection> configConnections = new HashMap<String, ConfigConnection>();
	// Propriedade utilizada para definir qual será o nome da conexão padrão,
	// podendo assim variar para testes ou produção
	private String defaultConnectionName;
	// Lista de configs disponíveis para utilização
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
	 * @return {@link Connection} gerada a partir do arquivo de configuração que
	 *         possua a 'configName' passada ou exception caso essa configuração
	 *         não existisse no momento de load das configs
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
						"Não foi possível geral uma conexão pra o Banco de Dados '"
								+ configName + "'.",
						e);
			}
		}
		throw new DBMapperException(
				"Não existe configuração com nome '" + configName + "'.");
	}

	/**
	 * Carrega as configurações do databases.json e cria uma conexão para cada
	 * configuração existente, além de as colocar no mapa de conexões
	 */
	public void loadConnections() {
		Properties prop = new Properties();

		try {
			// O primeiro passo para carregar as configurações é abrir o
			// arquivo de configurações. Para isso vamos utilizar nosso 'loader'
			// para pegar o 'path' do mesmo.
			String context = ContextSpecifier.getContext().replace(".", "/");
			String path = loader.getResource(context + "/core/databases.properties")
					.getPath();
			// Com 'path em mãos iremos converter, fazer 'parse', de seu
			// conteúdo para um objeto JSONArray, já que o arquivo começa
			// com '[..]', ou seja, o arquivo é um array.
			prop.load(new FileReader(path));

			if (!getInstance().isValid(prop)) {
				throw new DBMapperException(
						"É necessário ao menos uma configuração de Banco de Dados");
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
					"Erro ao carregar configurações de banco de dados", e);
		}
	}

	private boolean isValid(Properties prop) {
		return prop.size() % 5 == 0;
	}

	/**
	 * @return {@link Connection} gerada a partir da propriedade
	 *         defaultConnectionName, ou null caso não esteja configurada
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
	 * Esse método possui uma iteligência que pode parecer uma pegadinha. Ele
	 * faz com que a propriedade 'defaultConnectionName' receba valor uma única
	 * vez, com isso se torna impossível que por acidente a configuração padrão
	 * de banco seja alterada. Além disso esse método também verifica se a
	 * 'config' passada existe na lista de possíveis conexões, caso não um
	 * exceção é lançada.
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
						"Não existe configuração com nome '" + config + "'.");
			}
		}
	}

	public static String getDefaultConnectionName() {
		return getInstance().defaultConnectionName;
	}

	/**
	 * @return lista com todos os nomes de configurações disponíveis, essa lista
	 *         é criada a patir do método 'loadConnections'
	 */
	public static List<String> getPossibleConfigs() {
		return getInstance().possibleConfigs;
	}

}
