package com.rodrigobs.igreja.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.UsuarioRepository;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UsuarioRepository usuarioRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

//    @Test
//    @DisplayName("GET /login deve conter modelo com credenciais")
//    void getLoginForm() throws Exception {
//        mvc.perform(get("/login"))
//            .andExpect(status().isOk())
//            .andExpect(model().attributeExists("credenciais"));
//    }


    @Test
    @DisplayName("POST /login com credenciais corretas deve redirecionar para /dashboard")
    void login_sucesso() throws Exception {
        Usuario usuario = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", TipoUsuario.TESOUREIRA);

        Mockito.when(usuarioRepo.findByEmail("ana@x.com")).thenReturn(Optional.of(usuario));
        Mockito.when(passwordEncoder.matches("123", "hash")).thenReturn(true);

        mvc.perform(post("/login")
                .param("email", "ana@x.com")
                .param("senha", "123"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @DisplayName("POST /login com email não encontrado deve voltar ao login com erro")
    void login_emailInvalido() throws Exception {
        Mockito.when(usuarioRepo.findByEmail("nao@existe.com")).thenReturn(Optional.empty());

        mvc.perform(post("/login")
                .param("email", "nao@existe.com")
                .param("senha", "abc"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"))
            .andExpect(flash().attribute("erro", "E‑mail ou senha inválidos"));
    }

    @Test
    @DisplayName("POST /login com senha incorreta deve voltar ao login com erro")
    void login_senhaIncorreta() throws Exception {
        Usuario usuario = new Usuario(UUID.randomUUID(), "Ana", "ana@x.com", "hash", TipoUsuario.TESOUREIRA);

        Mockito.when(usuarioRepo.findByEmail("ana@x.com")).thenReturn(Optional.of(usuario));
        Mockito.when(passwordEncoder.matches("senhaerrada", "hash")).thenReturn(false);

        mvc.perform(post("/login")
                .param("email", "ana@x.com")
                .param("senha", "senhaerrada"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"))
            .andExpect(flash().attribute("erro", "E‑mail ou senha inválidos"));
    }

    @Test
    @DisplayName("GET /logout deve invalidar a sessão e redirecionar")
    void logout() throws Exception {
        mvc.perform(get("/logout"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/login*"));
    }

}
