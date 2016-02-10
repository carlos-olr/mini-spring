package br.com.spektro.minispring.core.implfinder;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.google.common.collect.Maps;

import br.com.spektro.minispring.api.implfinder.ImplFinderException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public class ImplementationFinder {

	private static ImplementationFinder instance;

	private Map<Class, Object> cache = Maps.newHashMap();

	private ImplementationFinder() {
	}

	private static ImplementationFinder getinstance() {
		if (instance == null) {
			instance = new ImplementationFinder();
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

	public static <I extends Object> I getImpl(Class<I> interfaceToFind,
			Class entityToFind) {
		return getinstance().getImplementation(interfaceToFind, entityToFind);
	}

	private <I extends Object> I getImplementation(Class<I> interfaceToFind,
			Class entityToFind) {
		Reflections ref = new Reflections(ContextSpecifier.getContext() + ".core");
		Set subTypesOf = ref.getSubTypesOf(interfaceToFind);

		Object[] subTypes = subTypesOf.toArray();

		for (int i = 0; i < subTypes.length; i++) {
			Class klass = (Class) subTypes[i];
			Type[] types = klass.getGenericInterfaces();
			if (types.length != 0) {
				ParameterizedTypeImpl type = (ParameterizedTypeImpl) types[0];
				Class entityKlassType = (Class) type.getActualTypeArguments()[0];
				if (entityKlassType.equals(entityToFind)) {
					try {
						return interfaceToFind.cast(klass.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
