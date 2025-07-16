package com.rodrigobs.igreja.service;

import org.springframework.stereotype.Service;

import com.rodrigobs.igreja.dto.response.DashboardResumo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinanceService financeService;

    public DashboardResumo getResumoFinanceiroCompleto() {
        DashboardResumo resumo = new DashboardResumo();
        resumo.setSemana(financeService.resumoSemana());
        resumo.setMes(financeService.resumoMes());
        resumo.setTrimestre(financeService.resumoTrimestre());
        resumo.setAno(financeService.resumoAno());
        return resumo;
    }

}
