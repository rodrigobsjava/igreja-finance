package com.rodrigobs.igreja.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigobs.igreja.dto.request.LancamentoRequestDTO;
import com.rodrigobs.igreja.dto.response.LancamentoResponseDTO;
import com.rodrigobs.igreja.mapper.LancamentoMapper;
import com.rodrigobs.igreja.model.Categoria;
import com.rodrigobs.igreja.model.Lancamento;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.CategoriaRepository;
import com.rodrigobs.igreja.repository.LancamentoRepository;
import com.rodrigobs.igreja.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public LancamentoResponseDTO criar(LancamentoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Lancamento lancamento = LancamentoMapper.toEntity(dto, categoria, usuario);
        Lancamento salvo = lancamentoRepository.save(lancamento);
        return LancamentoMapper.toDTO(salvo);
    }

    public List<LancamentoResponseDTO> listarTodos() {
        return lancamentoRepository.findAll()
                .stream()
                .map(LancamentoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public LancamentoResponseDTO buscarPorId(UUID id) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));
        return LancamentoMapper.toDTO(lancamento);
    }

    @Transactional
    public LancamentoResponseDTO atualizar(UUID id, LancamentoRequestDTO dto) {
        Lancamento lancamento = lancamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        lancamento.setDescricao(dto.getDescricao());
        lancamento.setValor(dto.getValor());
        lancamento.setData(dto.getData());
        lancamento.setTipo(dto.getTipo());
        lancamento.setCategoria(categoria);
        lancamento.setUsuario(usuario);

        Lancamento atualizado = lancamentoRepository.save(lancamento);
        return LancamentoMapper.toDTO(atualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        if (!lancamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Lançamento não encontrado");
        }
        lancamentoRepository.deleteById(id);
    }
}
