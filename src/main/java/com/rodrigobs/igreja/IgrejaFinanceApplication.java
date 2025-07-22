package com.rodrigobs.igreja;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.UsuarioRepository;

@SpringBootApplication
public class IgrejaFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgrejaFinanceApplication.class, args);
	}

	@Bean
	CommandLineRunner initUsuarios(UsuarioRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("wfrankms2@gmail.com").isEmpty()) {
				Usuario u = new Usuario();
				u.setNome("Wyston Frank Menezes De Souza");
				u.setEmail("wfrankms2@gmail.com");
				u.setSenha(encoder.encode("$wfrankms2$"));
				u.setTipo(TipoUsuario.ADMINISTRADOR);
				repo.save(u);
			}
		};
	}

}
