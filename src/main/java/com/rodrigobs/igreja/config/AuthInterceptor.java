package com.rodrigobs.igreja.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse res,
                             Object handler) throws Exception {

        String uri = req.getRequestURI();

        // rotas públicas
        if (uri.startsWith("/login")
            || uri.startsWith("/css")
            || uri.startsWith("/js")
            || uri.startsWith("/images")) {
            return true;
        }

        HttpSession session = req.getSession(false);        // não cria se inexistente
        Usuario logado = (session != null)
                       ? (Usuario) session.getAttribute("usuarioLogado")
                       : null;

        if (logado == null) {
            res.sendRedirect("/login");
            return false;
        }

        // regra por perfil
        if (uri.startsWith("/usuarios")
            && logado.getTipo() != TipoUsuario.ADMINISTRADOR) {
            res.sendRedirect("/403");
            return false;
        }

        return true;  // acesso liberado
    }
}

