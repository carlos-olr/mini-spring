package br.com.spektro.minispring.test.testcenario.core;

import br.com.spektro.minispring.dto.DTOConverter;
import br.com.spektro.minispring.test.testcenario.api.DTOClass1;
import br.com.spektro.minispring.test.testcenario.api.EntityClass1;

public class ConverterImpl1 implements DTOConverter<EntityClass1, DTOClass1> {

	@Override
	public DTOClass1 toDTO(EntityClass1 entidade) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityClass1 toEntity(DTOClass1 DTO) {
		// TODO Auto-generated method stub
		return null;
	}

}
