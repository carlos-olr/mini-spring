package br.com.spektro.minispring.api.dbmapper;

/**
 * @author carloso
 * 
 * @version 1.0.0
 */

public class ConfigConnection {

	private String name;
	private String url;
	private String login;
	private String password;
	private Class dbClass;

	/**
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param name
	 * @param url
	 * @param login
	 * @param password
	 */
	public ConfigConnection(String name, String url, String login, String password,
			Class dbClass) {
		this.name = name;
		this.url = url;
		this.login = login;
		this.password = password;
		this.setDbClass(dbClass);
	}

	public Class getDbClass() {
		return dbClass;
	}

	public void setDbClass(Class dbClass) {
		this.dbClass = dbClass;
	}

}
