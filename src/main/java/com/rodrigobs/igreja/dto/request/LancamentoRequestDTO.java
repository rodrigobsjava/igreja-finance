package com.rodrigobs.igreja.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.rodrigobs.igreja.model.TipoLancamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LancamentoRequestDTO {
	@NotBlank(message = "Descrição é obrigatória")
	private String descricao;

	@NotNull(message = "Valor é obrigatório")
	@PositiveOrZero(message = "Valor deve ser zero ou positivo")
	private BigDecimal valor;

	@NotNull(message = "Data é obrigatória")
	private LocalDate data;

	@NotNull(message = "Tipo de lançamento é obrigatório")
	private TipoLancamento tipo;

	@NotNull(message = "Categoria é obrigatória")
	private UUID categoriaId;

	@NotNull(message = "Usuário é obrigatório")
	private UUID usuarioId;

}
