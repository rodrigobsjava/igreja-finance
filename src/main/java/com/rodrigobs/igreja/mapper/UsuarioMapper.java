package com.rodrigobs.igreja.mapper;

import com.rodrigobs.igreja.dto.request.UsuarioRequestDTO;
import com.rodrigobs.igreja.dto.response.UsuarioResponseDTO;
import com.rodrigobs.igreja.model.Usuario;

public class UsuarioMapper {
	public static Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(dto.getTipo());
        return usuario;
    }
	
	public static UsuarioResponseDTO toDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTipo()
        );
    }
}
