package br.com.spektro.minispring.core.implfinder;

import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import br.com.spektro.minispring.api.implfinder.ImplFinderException;

public class ImplFinder {

	private static ImplFinder instance;

	private Map<Class, Object> cache = Maps.newHashMap();

	private ImplFinder() {
	}

	private static ImplFinder getinstance() {
		if (instance == null) {
			instance = new ImplFinder();
		}
		return instance;
	}

	public static <T extends Object> T getImpl(Class<T> interfaceToFind) {
		return getinstance().getImplementation(interfaceToFind);
	}

	private <T extends Object> T getImplementation(Class<T> interfaceToFind) {
		if (this.cache.containsKey(interfaceToFind)) {
			return interfaceToFind.cast(this.cache.get(interfaceToFind));
		}

		Reflections ref = new Reflections(ContextSpecifier.getContext() + ".core");
		Set subTypesOf = ref.getSubTypesOf(interfaceToFind);
		if (subTypesOf.size() == 0) {
			throw new ImplFinderException(
					"Não foi encontrada implementação para a classe "
							+ interfaceToFind.getCanonicalName());
		}
		if (subTypesOf.size() > 1) {
			throw new ImplFinderException("Mais de uma implementação da classe "
					+ interfaceToFind.getCanonicalName() + " foram encontradas.");
		}

		Class klass = (Class) subTypesOf.toArray()[0];
		Object instanceClass = null;
		try {
			instanceClass = klass.newInstance();
			this.cache.put(interfaceToFind, instanceClass);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return interfaceToFind.cast(instanceClass);
	}

	public static <I extends Object> I getFinalImpl(Class<I> implClass) {
		return getinstance().getFinalImplementation(implClass);
	}

	private <I extends Object> I getFinalImplementation(Class<I> implClass) {
		if (this.cache.containsKey(implClass)) {
			return implClass.cast(this.cache.get(implClass));
		}
		try {
			I newInstance = implClass.newInstance();
			this.cache.put(implClass, newInstance);
			return newInstance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
