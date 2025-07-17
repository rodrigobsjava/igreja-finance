package com.rodrigobs.igreja.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigobs.igreja.dto.response.DashboardResumo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private final FinanceService financeService;

	@Transactional(readOnly = true)
	public DashboardResumo getResumoFinanceiroCompleto() {
		return new DashboardResumo(financeService.resumoSemana(), financeService.resumoMes(),
				financeService.resumoTrimestre(), financeService.resumoAno());
	}

}
