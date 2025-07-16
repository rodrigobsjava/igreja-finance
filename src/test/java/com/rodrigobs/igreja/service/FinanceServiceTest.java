package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rodrigobs.igreja.dto.response.ResumoFinanceiroDTO;
import com.rodrigobs.igreja.model.TipoLancamento;
import com.rodrigobs.igreja.repository.LancamentoRepository;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    LancamentoRepository lancRepo;

    @InjectMocks
    FinanceService financeService;

    @Test
    @DisplayName("Deve calcular entradas, saídas e saldo em período genérico")
    void resumoPeriodo_ok() {
        LocalDate ini = LocalDate.of(2025, 1, 1);
        LocalDate fim = LocalDate.of(2025, 1, 31);

        when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.ENTRADA, ini, fim))
                .thenReturn(new BigDecimal("1000.00"));
        when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA, ini, fim))
                .thenReturn(new BigDecimal("400.00"));

        ResumoFinanceiroDTO resumo = financeService.resumoPeriodo(ini, fim);

        assertThat(resumo.getEntradas()).isEqualByComparingTo("1000.00");
        assertThat(resumo.getSaídas()).isEqualByComparingTo("400.00");
        assertThat(resumo.getSaldo()).isEqualByComparingTo("600.00");
    }

    @Test
    @DisplayName("Deve retornar zeros quando não há lançamentos")
    void resumoPeriodo_semLancamentos() {
        LocalDate hoje = LocalDate.now();

        when(lancRepo.somaPorTipoEPeriodo(any(), any(), any()))
                .thenReturn(BigDecimal.ZERO);

        ResumoFinanceiroDTO resumo = financeService.resumoPeriodo(hoje, hoje);

        assertThat(resumo.getEntradas()).isZero();
        assertThat(resumo.getSaídas()).isZero();
        assertThat(resumo.getSaldo()).isZero();
    }
}
