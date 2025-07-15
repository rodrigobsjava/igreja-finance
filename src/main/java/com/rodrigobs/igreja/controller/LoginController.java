package com.rodrigobs.igreja.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigobs.igreja.dto.request.CredenciaisDTO;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final PasswordEncoder passwordEncoder;
	private final UsuarioRepository usuarioRepo;

	@GetMapping("/login")
	public String loginForm(Model m) {
		m.addAttribute("credenciais", new CredenciaisDTO());
		return "login";
	}

	@PostMapping("/login")
	public String autenticar(@ModelAttribute CredenciaisDTO cred, HttpSession session, RedirectAttributes redirect) {

		Usuario usuario = usuarioRepo.findByEmail(cred.getEmail())
	            .filter(u -> passwordEncoder.matches(cred.getSenha(), u.getSenha())) // compara hash
	            .orElse(null);

		if (usuario == null) {
			redirect.addFlashAttribute("erro", "E‑mail ou senha inválidos");
			return "redirect:/login";
		}
		session.setAttribute("usuarioLogado", usuario);
		return "redirect:/dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login?logout";
	}

}
