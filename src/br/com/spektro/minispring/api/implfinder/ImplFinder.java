package br.com.spektro.minispring.api.implfinder;

public interface ImplFinder {

	<T extends Object> T getImpl(Class<T> interfaceToFind);

}
