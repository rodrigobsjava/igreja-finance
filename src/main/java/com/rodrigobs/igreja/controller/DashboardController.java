package com.rodrigobs.igreja.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigobs.igreja.dto.response.DashboardResumo;
import com.rodrigobs.igreja.service.DashboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/resumo")
    public ResponseEntity<DashboardResumo> getResumoFinanceiro() {
        DashboardResumo resumo = dashboardService.getResumoFinanceiroCompleto();
        return ResponseEntity.ok(resumo);
    }
}
