package com.rodrigobs.igreja.dto.response;

import java.util.UUID;

import com.rodrigobs.igreja.model.TipoUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioResponseDTO {
	private UUID id;
	private String nome;
	private String email;
	private TipoUsuario tipo;
}
