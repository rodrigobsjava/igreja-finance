package com.rodrigobs.igreja.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rodrigobs.igreja.dto.request.UsuarioRequestDTO;
import com.rodrigobs.igreja.dto.response.UsuarioResponseDTO;
import com.rodrigobs.igreja.exception.EmailJaCadastradoException;
import com.rodrigobs.igreja.mapper.UsuarioMapper;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

	private final PasswordEncoder passwordEncoder;
	private final UsuarioRepository usuarioRepository;

	@Transactional
	public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
		// Verifica se o e-mail ja existe
		if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new EmailJaCadastradoException("E-mail já cadastrado: " + dto.getEmail());
		}

		Usuario usuario = UsuarioMapper.toEntity(dto);
		usuario.setSenha(passwordEncoder.encode(dto.getSenha())); // hash da senha antes de salvar
		Usuario salvo = usuarioRepository.save(usuario);
		return UsuarioMapper.toDTO(salvo);
	}

	public UsuarioResponseDTO buscarPorId(UUID id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
		return UsuarioMapper.toDTO(usuario);
	}

	public List<UsuarioResponseDTO> listarTodos() {
		return usuarioRepository.findAll().stream().map(UsuarioMapper::toDTO).collect(Collectors.toList());
	}

	@Transactional
	public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

		// Evita alterar para um e-mail ja usado por outro usuário
		usuarioRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
			if (!existing.getId().equals(id)) {
				throw new EmailJaCadastradoException("E-mail já está em uso por outro usuário");
			}
		});

		usuario.setNome(dto.getNome());
		usuario.setEmail(dto.getEmail());
		usuario.setTipo(dto.getTipo());

		// Se veio senha nova, aplica hash
		if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
			usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
		}
		Usuario atualizado = usuarioRepository.save(usuario);
		return UsuarioMapper.toDTO(atualizado);
	}

	@Transactional
	public void deletar(UUID id) {
		if (!usuarioRepository.existsById(id)) {
			throw new EntityNotFoundException("Usuário não encontrado");
		}
		usuarioRepository.deleteById(id);
	}
}
