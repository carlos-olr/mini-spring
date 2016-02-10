package br.com.spektro.minispring.dto;

/**
 * @author Carlos Oliveira
 * @param <E>
 *            Classe que representa a Entidade
 * @param <D>
 *            Classe que representa o DTO
 */
public interface DTOConverter<E, D> {

	D toDTO(E entidade);

	E toEntity(D DTO);

}
