package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    private LocalDate inicio;
    private LocalDate fim;

    @BeforeEach
    void setUp() {
        inicio = LocalDate.of(2025, 1, 1);
        fim = LocalDate.of(2025, 1, 31);
    }

    @Nested
    class ResumoPeriodo {

        @Test
        @DisplayName("Deve calcular entradas, saídas e saldo em período genérico")
        void resumoPeriodo_ok() {
            when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.ENTRADA, inicio, fim)).thenReturn(new BigDecimal("1000.00"));
            when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA, inicio, fim)).thenReturn(new BigDecimal("400.00"));

            ResumoFinanceiroDTO resumo = financeService.resumoPeriodo(inicio, fim);

            assertThat(resumo.getEntradas()).isEqualByComparingTo("1000.00");
            assertThat(resumo.getSaídas()).isEqualByComparingTo("400.00");
            assertThat(resumo.getSaldo()).isEqualByComparingTo("600.00");
        }

        @Test
        @DisplayName("Deve retornar zeros quando não há lançamentos")
        void resumoPeriodo_semLancamentos() {
            when(lancRepo.somaPorTipoEPeriodo(any(), any(), any())).thenReturn(BigDecimal.ZERO);

            ResumoFinanceiroDTO resumo = financeService.resumoPeriodo(inicio, fim);

            assertThat(resumo.getEntradas()).isZero();
            assertThat(resumo.getSaídas()).isZero();
            assertThat(resumo.getSaldo()).isZero();
        }
    }

    @Nested
    class ResumoAtalhos {

        @Test
        void resumoSemana_ok() {
            when(lancRepo.somaPorTipoEPeriodo(any(), any(), any())).thenReturn(BigDecimal.TEN);

            ResumoFinanceiroDTO resumo = financeService.resumoSemana();

            assertThat(resumo.getEntradas()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(resumo.getSaídas()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(resumo.getSaldo()).isZero(); // 10 - 10 = 0
        }

        @Test
        void resumoMes_ok() {
            when(lancRepo.somaPorTipoEPeriodo(any(), any(), any())).thenReturn(BigDecimal.ONE);

            ResumoFinanceiroDTO resumo = financeService.resumoMes();

            assertThat(resumo.getEntradas()).isEqualByComparingTo(BigDecimal.ONE);
            assertThat(resumo.getSaídas()).isEqualByComparingTo(BigDecimal.ONE);
            assertThat(resumo.getSaldo()).isZero();
        }

        @Test
        void resumoTrimestre_ok() {
            when(lancRepo.somaPorTipoEPeriodo(any(), any(), any())).thenReturn(new BigDecimal("200"));

            ResumoFinanceiroDTO resumo = financeService.resumoTrimestre();

            assertThat(resumo.getSaldo()).isZero(); // 200 - 200 = 0
        }

        @Test
        void resumoAno_ok() {
            when(lancRepo.somaPorTipoEPeriodo(any(), any(), any())).thenReturn(new BigDecimal("500"));

            ResumoFinanceiroDTO resumo = financeService.resumoAno();

            assertThat(resumo.getSaldo()).isZero(); // 500 - 500 = 0
        }
    }

    @Nested
    class ResumoPorCategoriaOuUsuario {

        @Test
        void resumoPorCategoria_ok() {
            UUID categoriaId = UUID.randomUUID();

            when(lancRepo.somaPorCategoriaETipo(categoriaId, TipoLancamento.ENTRADA, inicio, fim))
                .thenReturn(new BigDecimal("700"));
            when(lancRepo.somaPorCategoriaETipo(categoriaId, TipoLancamento.SAIDA, inicio, fim))
                .thenReturn(new BigDecimal("500"));

            ResumoFinanceiroDTO resumo = financeService.resumoPorCategoria(categoriaId, inicio, fim);

            assertThat(resumo.getEntradas()).isEqualByComparingTo("700");
            assertThat(resumo.getSaídas()).isEqualByComparingTo("500");
            assertThat(resumo.getSaldo()).isEqualByComparingTo("200");
        }

        @Test
        void resumoPorUsuario_ok() {
            UUID usuarioId = UUID.randomUUID();

            when(lancRepo.somaPorUsuarioETipo(usuarioId, TipoLancamento.ENTRADA, inicio, fim))
                .thenReturn(new BigDecimal("300"));
            when(lancRepo.somaPorUsuarioETipo(usuarioId, TipoLancamento.SAIDA, inicio, fim))
                .thenReturn(new BigDecimal("100"));

            ResumoFinanceiroDTO resumo = financeService.resumoPorUsuario(usuarioId, inicio, fim);

            assertThat(resumo.getEntradas()).isEqualByComparingTo("300");
            assertThat(resumo.getSaídas()).isEqualByComparingTo("100");
            assertThat(resumo.getSaldo()).isEqualByComparingTo("200");
        }
    }

    @Nested
    class Totais {

        @Test
        void totalEntradas_ok() {
            when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.ENTRADA, inicio, fim)).thenReturn(new BigDecimal("777.77"));

            BigDecimal total = financeService.totalEntradas(inicio, fim);

            assertThat(total).isEqualByComparingTo("777.77");
        }

        @Test
        void totalSaidas_ok() {
            when(lancRepo.somaPorTipoEPeriodo(TipoLancamento.SAIDA, inicio, fim)).thenReturn(new BigDecimal("123.45"));

            BigDecimal total = financeService.totalSaidas(inicio, fim);

            assertThat(total).isEqualByComparingTo("123.45");
        }
    }
}
