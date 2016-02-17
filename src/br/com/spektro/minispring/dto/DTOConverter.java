package br.com.spektro.minispring.dto;

import java.util.List;

/**
 * @author Carlos Oliveira
 * @param <E>
 *            Classe que representa a Entidade
 * @param <D>
 *            Classe que representa o DTO
 */
public interface DTOConverter<E, D> {

	D toDTO(E entidade);

	E toEntity(D dto);

	List<D> toDTO(List<E> entidades);

	List<E> toEntity(List<D> dtos);

}
