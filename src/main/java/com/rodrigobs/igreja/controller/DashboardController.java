package com.rodrigobs.igreja.controller;

import com.rodrigobs.igreja.dto.response.DashboardResumo;
import com.rodrigobs.igreja.service.DashboardService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResumo> getResumoFinanceiro() {
        DashboardResumo resumo = dashboardService.getResumoFinanceiroCompleto();
        return ResponseEntity.ok(resumo);
    }
}
