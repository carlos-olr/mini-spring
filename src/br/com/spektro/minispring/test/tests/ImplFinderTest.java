package br.com.spektro.minispring.test.tests;

import org.junit.Assert;
import org.junit.Test;

import br.com.spektro.minispring.api.implfinder.ImplFinderException;
import br.com.spektro.minispring.core.implfinder.ImplementationFinder;
import br.com.spektro.minispring.dto.DTOConverter;
import br.com.spektro.minispring.test.testcenario.api.DTOClass1;
import br.com.spektro.minispring.test.testcenario.api.EntityClass1;
import br.com.spektro.minispring.test.testcenario.api.EntityClass2;
import br.com.spektro.minispring.test.testcenario.api.InterfaceBasica_1;
import br.com.spektro.minispring.test.testcenario.api.InterfaceBasica_Erro;
import br.com.spektro.minispring.test.testcenario.core.ConverterImpl1;
import br.com.spektro.minispring.test.testcenario.core.ConverterImpl2;

public class ImplFinderTest extends MiniSpringBaseTest {

	@Test
	public void findImplBasica() {
		Assert.assertNotNull(ImplementationFinder.getImpl(InterfaceBasica_1.class));
	}

	@Test
	public void checkSingleton() {
		InterfaceBasica_1 impl1 = ImplementationFinder.getImpl(InterfaceBasica_1.class);
		InterfaceBasica_1 impl2 = ImplementationFinder.getImpl(InterfaceBasica_1.class);
		Assert.assertEquals(impl1, impl2);
	}

	@Test(expected = ImplFinderException.class)
	public void checkConstraintUniqueImpl() {
		ImplementationFinder.getImpl(InterfaceBasica_Erro.class);
		Assert.fail();
	}

	@Test
	public void findImplGenerics() {
		DTOConverter impl1 = ImplementationFinder.getImpl(DTOConverter.class,
				EntityClass1.class);
		Assert.assertNotNull(impl1);
		Assert.assertTrue(impl1 instanceof ConverterImpl1);

		DTOConverter impl2 = ImplementationFinder.getImpl(DTOConverter.class,
				EntityClass2.class);
		Assert.assertNotNull(impl2);
		Assert.assertTrue(impl2 instanceof ConverterImpl2);

		DTOConverter impl3 = ImplementationFinder.getImpl(DTOConverter.class,
				DTOClass1.class);
		Assert.assertNull(impl3);

	}

}
