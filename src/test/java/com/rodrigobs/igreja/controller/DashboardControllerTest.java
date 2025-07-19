package com.rodrigobs.igreja.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rodrigobs.igreja.dto.response.DashboardResumo;
import com.rodrigobs.igreja.dto.response.ResumoFinanceiroDTO;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.service.DashboardService;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    @Test
    @DisplayName("Deve retornar resumo financeiro completo com sucesso")
    void getResumoFinanceiroCompleto_sucesso() throws Exception {
        DashboardResumo mockResumo = new DashboardResumo(
                new ResumoFinanceiroDTO(), new ResumoFinanceiroDTO(),
                new ResumoFinanceiroDTO(), new ResumoFinanceiroDTO()
        );

        Mockito.when(dashboardService.getResumoFinanceiroCompleto())
               .thenReturn(mockResumo);

        mockMvc.perform(get("/dashboard/resumo")
                .sessionAttr("usuarioLogado", new Usuario()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.semana").exists())
            .andExpect(jsonPath("$.mes").exists())
            .andExpect(jsonPath("$.trimestre").exists())
            .andExpect(jsonPath("$.ano").exists());
    }
}
