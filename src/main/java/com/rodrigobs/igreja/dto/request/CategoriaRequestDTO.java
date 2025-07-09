package com.rodrigobs.igreja.dto.request;

import com.rodrigobs.igreja.model.TipoLancamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaRequestDTO {
	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@NotNull(message = "Tipo de lançamento é obrigatório")
	private TipoLancamento tipo;
}
