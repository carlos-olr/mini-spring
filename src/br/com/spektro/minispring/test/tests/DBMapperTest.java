package br.com.spektro.minispring.test.tests;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.com.spektro.minispring.api.dbmapper.DBMapperException;
import br.com.spektro.minispring.core.dbmapper.ConfigDBMapper;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DBMapperTest extends MiniSpringBaseTest {

	@Test
	public void getConnectionTest$1() {
		Assert.assertNull(ConfigDBMapper.getDefaultConnection());
	}

	@Test
	public void getConnectionTest$2() {
		ConfigDBMapper.setDefaultConnectionName("test1");
		Assert.assertNotNull(ConfigDBMapper.getDefaultConnection());
	}

	@Test
	public void getConnectionTest$3() {
		Assert.assertNotNull(ConfigDBMapper.getConnectionByConfig("test2"));
	}

	@Test(expected = DBMapperException.class)
	public void getConnectionTest$4() {
		Assert.assertNotNull(ConfigDBMapper.getConnectionByConfig(null));
		Assert.fail();
	}

	@Test(expected = DBMapperException.class)
	public void getConnectionTest$5() {
		Assert.assertNotNull(ConfigDBMapper.getConnectionByConfig("test3"));
		Assert.fail();
	}
}
