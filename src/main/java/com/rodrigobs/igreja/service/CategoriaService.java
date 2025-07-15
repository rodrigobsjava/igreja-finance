package com.rodrigobs.igreja.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rodrigobs.igreja.dto.request.CategoriaRequestDTO;
import com.rodrigobs.igreja.dto.response.CategoriaResponseDTO;
import com.rodrigobs.igreja.mapper.CategoriaMapper;
import com.rodrigobs.igreja.model.Categoria;
import com.rodrigobs.igreja.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;

	@Transactional
	public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
		verificarDuplicidade(dto.getNome());
		Categoria categoria = CategoriaMapper.toEntity(dto);
		Categoria salvo = categoriaRepository.save(categoria);
		return CategoriaMapper.toDTO(salvo);
	}

	public CategoriaResponseDTO buscarPorId(UUID id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
		return CategoriaMapper.toDTO(categoria);
	}

	public List<CategoriaResponseDTO> listarTodas() {
		return categoriaRepository.findAll().stream().map(CategoriaMapper::toDTO).collect(Collectors.toList());
	}

	@Transactional
	public CategoriaResponseDTO atualizar(UUID id, CategoriaRequestDTO dto) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

		if (!categoria.getNome().equalsIgnoreCase(dto.getNome())) {
			verificarDuplicidade(dto.getNome());
		}

		categoria.setNome(dto.getNome());
		categoria.setTipo(dto.getTipo());

		Categoria atualizada = categoriaRepository.save(categoria);
		return CategoriaMapper.toDTO(atualizada);
	}

	@Transactional
	public void deletar(UUID id) {
		if (!categoriaRepository.existsById(id)) {
			throw new EntityNotFoundException("Categoria não encontrada");
		}

		categoriaRepository.deleteById(id);
	}

	private void verificarDuplicidade(String nome) {
		boolean existe = categoriaRepository.existsByNomeIgnoreCase(nome);
		if (existe) {
			throw new IllegalArgumentException("Categoria já existe: " + nome);
		}
	}

}
