package br.com.spektro.minispring.test.tests;

import org.junit.Assert;
import org.junit.Test;

import br.com.spektro.minispring.api.implfinder.ImplFinderException;
import br.com.spektro.minispring.core.implfinder.ImplFinder;
import br.com.spektro.minispring.test.testcenario.api.InterfaceBasica_1;
import br.com.spektro.minispring.test.testcenario.api.InterfaceBasica_Erro;
import br.com.spektro.minispring.test.testcenario.core.ConverterImpl1;
import br.com.spektro.minispring.test.testcenario.core.ConverterImpl2;

public class ImplFinderTest extends MiniSpringBaseTest {

	@Test
	public void findImplBasica() {
		Assert.assertNotNull(ImplFinder.getImpl(InterfaceBasica_1.class));
	}

	@Test
	public void checkSingleton() {
		InterfaceBasica_1 impl1 = ImplFinder.getImpl(InterfaceBasica_1.class);
		InterfaceBasica_1 impl2 = ImplFinder.getImpl(InterfaceBasica_1.class);
		Assert.assertEquals(impl1, impl2);
	}

	@Test(expected = ImplFinderException.class)
	public void checkConstraintUniqueImpl() {
		ImplFinder.getImpl(InterfaceBasica_Erro.class);
		Assert.fail();
	}

	@Test
	public void findImplGenerics() {
		ConverterImpl1 impl1 = ImplFinder.getFinalImpl(ConverterImpl1.class);
		Assert.assertNotNull(impl1);

		ConverterImpl2 impl2 = ImplFinder.getFinalImpl(ConverterImpl2.class);
		Assert.assertNotNull(impl2);

		ConverterImpl1 impl11 = ImplFinder.getFinalImpl(ConverterImpl1.class);
		Assert.assertEquals(impl1, impl11);

		ConverterImpl2 impl21 = ImplFinder.getFinalImpl(ConverterImpl2.class);
		Assert.assertEquals(impl2, impl21);

	}

}
