package com.rodrigobs.igreja.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

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
        BigDecimal saídas   = lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA,   inicio, fim);
        return new ResumoFinanceiroDTO(entradas, saídas, entradas.subtract(saídas));
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
}
