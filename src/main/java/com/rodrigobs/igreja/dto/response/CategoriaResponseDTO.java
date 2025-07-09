package com.rodrigobs.igreja.dto.response;

import java.util.UUID;

import com.rodrigobs.igreja.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaResponseDTO {
	private UUID id;
	private String nome;
	private TipoLancamento tipo;
}
