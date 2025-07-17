package com.rodrigobs.igreja.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigobs.igreja.dto.response.ResumoFinanceiroDTO;
import com.rodrigobs.igreja.model.TipoLancamento;
import com.rodrigobs.igreja.repository.LancamentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinanceService {

	private final LancamentoRepository lancRepo;

	public ResumoFinanceiroDTO resumoPeriodo(LocalDate inicio, LocalDate fim) {
		BigDecimal entradas = lancRepo.somaPorTipoEPeriodo(TipoLancamento.ENTRADA, inicio, fim);
		BigDecimal saídas = lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA, inicio, fim);
		return gerarResumo(entradas, saídas);
	}

	public ResumoFinanceiroDTO resumoSemana() {
		LocalDate hoje = LocalDate.now();
		LocalDate segunda = hoje.with(DayOfWeek.MONDAY);
		return resumoPeriodo(segunda, hoje);
	}

	public ResumoFinanceiroDTO resumoMes() {
		LocalDate hoje = LocalDate.now();
		return resumoPeriodo(hoje.withDayOfMonth(1), hoje);
	}

	public ResumoFinanceiroDTO resumoTrimestre() {
		LocalDate hoje = LocalDate.now();
		LocalDate inicio = hoje.minusMonths(2).withDayOfMonth(1);
		return resumoPeriodo(inicio, hoje);
	}

	public ResumoFinanceiroDTO resumoAno() {
		LocalDate hoje = LocalDate.now();
		return resumoPeriodo(hoje.withDayOfYear(1), hoje);
	}

	public ResumoFinanceiroDTO resumoPorCategoria(UUID categoriaId, LocalDate inicio, LocalDate fim) {
		BigDecimal entradas = lancRepo.somaPorCategoriaETipo(categoriaId, TipoLancamento.ENTRADA, inicio, fim);
		BigDecimal saidas = lancRepo.somaPorCategoriaETipo(categoriaId, TipoLancamento.SAIDA, inicio, fim);
		return gerarResumo(entradas, saidas);
	}

	public ResumoFinanceiroDTO resumoPorUsuario(UUID usuarioId, LocalDate inicio, LocalDate fim) {
		BigDecimal entradas = lancRepo.somaPorUsuarioETipo(usuarioId, TipoLancamento.ENTRADA, inicio, fim);
		BigDecimal saidas = lancRepo.somaPorUsuarioETipo(usuarioId, TipoLancamento.SAIDA, inicio, fim);
		return gerarResumo(entradas, saidas);
	}

	public BigDecimal totalEntradas(LocalDate inicio, LocalDate fim) {
		return lancRepo.somaPorTipoEPeriodo(TipoLancamento.ENTRADA, inicio, fim);
	}

	public BigDecimal totalSaidas(LocalDate inicio, LocalDate fim) {
		return lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA, inicio, fim);
	}

	private ResumoFinanceiroDTO gerarResumo(BigDecimal entradas, BigDecimal saidas) {
		return new ResumoFinanceiroDTO(entradas, saidas, entradas.subtract(saidas));
	}
}
