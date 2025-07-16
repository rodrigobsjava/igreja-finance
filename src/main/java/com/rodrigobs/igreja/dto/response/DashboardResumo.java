package com.rodrigobs.igreja.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResumo {

    private ResumoFinanceiroDTO semana;
    private ResumoFinanceiroDTO mes;
    private ResumoFinanceiroDTO trimestre;
    private ResumoFinanceiroDTO ano;
}
