package com.rodrigobs.igreja.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

class AuthInterceptorTest {

    private AuthInterceptor interceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        interceptor = new AuthInterceptor();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Nested
    class RotasPublicas {

        @Test
        @DisplayName("Deve permitir acesso a /login mesmo sem sessão")
        void rotaLogin_liberada() throws Exception {
            when(request.getRequestURI()).thenReturn("/login");

            boolean resultado = interceptor.preHandle(request, response, new Object());

            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Deve permitir acesso a /css mesmo sem sessão")
        void rotaCss_liberada() throws Exception {
            when(request.getRequestURI()).thenReturn("/css/style.css");

            boolean resultado = interceptor.preHandle(request, response, new Object());

            assertThat(resultado).isTrue();
        }
    }

    @Nested
    class ProtegidoSemLogin {

        @Test
        @DisplayName("Deve bloquear rota protegida se não estiver logado")
        void protegido_semLogin() throws Exception {
            when(request.getRequestURI()).thenReturn("/categorias");
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute("usuarioLogado")).thenReturn(null);

            doNothing().when(response).sendRedirect("/login");

            boolean resultado = interceptor.preHandle(request, response, new Object());

            assertThat(resultado).isFalse();
            verify(response).sendRedirect("/login");
        }
    }

    @Nested
    class ProtegidoComLogin {

        @Test
        @DisplayName("Deve permitir acesso a /categorias por tesoureira logada")
        void acessoTesoureiraPermitido() throws Exception {
            Usuario tesoureira = new Usuario(UUID.randomUUID(), "Maria", "m@x.com", "123", TipoUsuario.TESOUREIRA);

            when(request.getRequestURI()).thenReturn("/categorias");
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuarioLogado")).thenReturn(tesoureira);

            boolean resultado = interceptor.preHandle(request, response, new Object());

            assertThat(resultado).isTrue();
        }

        @Test
        @DisplayName("Deve bloquear acesso a /usuarios por tesoureira")
        void acessoTesoureiraNegadoEmUsuarios() throws Exception {
            Usuario tesoureira = new Usuario(UUID.randomUUID(), "Maria", "m@x.com", "123", TipoUsuario.TESOUREIRA);

            when(request.getRequestURI()).thenReturn("/usuarios");
            when(request.getSession(false)).thenReturn(session);
            when(session.getAttribute("usuarioLogado")).thenReturn(tesoureira);

            doNothing().when(response).sendRedirect("/403");

            boolean resultado = interceptor.preHandle(request, response, new Object());

            assertThat(resultado).isFalse();
            verify(response).sendRedirect("/403");
        }
    }
}
