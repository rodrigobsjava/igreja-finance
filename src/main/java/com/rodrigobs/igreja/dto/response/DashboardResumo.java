package com.rodrigobs.igreja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumo {

    private ResumoFinanceiroDTO semana;
    private ResumoFinanceiroDTO mes;
    private ResumoFinanceiroDTO trimestre;
    private ResumoFinanceiroDTO ano;
}
