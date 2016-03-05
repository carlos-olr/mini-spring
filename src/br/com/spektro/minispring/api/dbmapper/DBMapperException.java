package br.com.spektro.minispring.api.dbmapper;

public class DBMapperException extends RuntimeException {

	/** */
	private static final long serialVersionUID = 1L;

	public DBMapperException(String message) {
		super(message);
	}

	public DBMapperException(String message, Exception e) {
		super(message, e);
	}
}
