package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rodrigobs.igreja.dto.response.DashboardResumo;
import com.rodrigobs.igreja.dto.response.ResumoFinanceiroDTO;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    FinanceService financeService;

    @InjectMocks
    DashboardService dashboardService;

    @Test
    @DisplayName("Deve retornar resumo completo com dados financeiros da semana, mês, trimestre e ano")
    void getResumoFinanceiroCompleto_ok() {
        ResumoFinanceiroDTO semana = new ResumoFinanceiroDTO(new BigDecimal("100"), new BigDecimal("40"), new BigDecimal("60"));
        ResumoFinanceiroDTO mes = new ResumoFinanceiroDTO(new BigDecimal("300"), new BigDecimal("100"), new BigDecimal("200"));
        ResumoFinanceiroDTO trimestre = new ResumoFinanceiroDTO(new BigDecimal("900"), new BigDecimal("500"), new BigDecimal("400"));
        ResumoFinanceiroDTO ano = new ResumoFinanceiroDTO(new BigDecimal("5000"), new BigDecimal("2000"), new BigDecimal("3000"));

        when(financeService.resumoSemana()).thenReturn(semana);
        when(financeService.resumoMes()).thenReturn(mes);
        when(financeService.resumoTrimestre()).thenReturn(trimestre);
        when(financeService.resumoAno()).thenReturn(ano);

        DashboardResumo resumo = dashboardService.getResumoFinanceiroCompleto();

        assertThat(resumo).isNotNull();
        assertThat(resumo.getSemana()).isEqualTo(semana);
        assertThat(resumo.getMes()).isEqualTo(mes);
        assertThat(resumo.getTrimestre()).isEqualTo(trimestre);
        assertThat(resumo.getAno()).isEqualTo(ano);

        verify(financeService).resumoSemana();
        verify(financeService).resumoMes();
        verify(financeService).resumoTrimestre();
        verify(financeService).resumoAno();
    }
}
