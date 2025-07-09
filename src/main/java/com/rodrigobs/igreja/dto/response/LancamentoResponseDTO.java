package com.rodrigobs.igreja.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.rodrigobs.igreja.model.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LancamentoResponseDTO {
	private UUID id;
	private String descricao;
	private BigDecimal valor;
	private LocalDate data;
	private TipoLancamento tipo;

	private UUID categoriaId;
	private String categoriaNome;

	private UUID usuarioId;
	private String usuarioNome;
}
