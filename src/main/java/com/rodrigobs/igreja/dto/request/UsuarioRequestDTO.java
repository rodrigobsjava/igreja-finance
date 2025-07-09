package com.rodrigobs.igreja.dto.request;

import com.rodrigobs.igreja.model.TipoUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioRequestDTO {
	@NotBlank(message = "Nome é obrigatório")
	private String nome;

	@Email(message = "Email inválido")
	@NotBlank(message = "Email é obrigatório")
	private String email;

	@NotBlank(message = "Senha é obrigatória")
	private String senha;

	@NotNull(message = "Tipo de usuário é obrigatório")
	private TipoUsuario tipo;
}
