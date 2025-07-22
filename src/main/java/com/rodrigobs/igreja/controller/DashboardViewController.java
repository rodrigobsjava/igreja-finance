package com.rodrigobs.igreja.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.rodrigobs.igreja.dto.response.DashboardResumo;
import com.rodrigobs.igreja.service.DashboardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardViewController {
	
	private final DashboardService dashboardService;
//	@GetMapping("/dashboard")
//    public String mostrarDashboard() {
//        return "dashboard"; // templates/dashboard.html
//    }
	@GetMapping("/dashboard")
	public String dashboard(Model model, HttpSession session) {
		// Proteção contra acesso não autorizado
		if (session.getAttribute("usuarioLogado") == null) {
			return "redirect:/login";
		}

		DashboardResumo resumo = dashboardService.getResumoFinanceiroCompleto();
		model.addAttribute("resumo", resumo);
		return "dashboard"; // → templates/dashboard.html
	}
}
