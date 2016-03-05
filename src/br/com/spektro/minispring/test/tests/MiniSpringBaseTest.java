package br.com.spektro.minispring.test.tests;

import org.junit.Before;

import br.com.spektro.minispring.core.implfinder.ContextSpecifier;

public class MiniSpringBaseTest {

	@Before
	public void setUp() {
		ContextSpecifier.setContext("br.com.spektro.minispring.test.testcenario");
	}

}
