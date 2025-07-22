package com.rodrigobs.igreja.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PeriodoResumoDTO {
	private BigDecimal entradas;
	private BigDecimal saidas;
	private BigDecimal saldo;
}
