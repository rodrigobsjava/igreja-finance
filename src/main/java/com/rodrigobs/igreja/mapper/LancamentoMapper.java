package com.rodrigobs.igreja.mapper;

import com.rodrigobs.igreja.dto.request.LancamentoRequestDTO;
import com.rodrigobs.igreja.dto.response.LancamentoResponseDTO;
import com.rodrigobs.igreja.model.Categoria;
import com.rodrigobs.igreja.model.Lancamento;
import com.rodrigobs.igreja.model.Usuario;

public class LancamentoMapper {
	public static Lancamento toEntity(LancamentoRequestDTO dto, Categoria categoria, Usuario usuario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setValor(dto.getValor());
        lancamento.setData(dto.getData());
        lancamento.setTipo(dto.getTipo());
        lancamento.setCategoria(categoria);
        lancamento.setUsuario(usuario);
        return lancamento;
    }

    public static LancamentoResponseDTO toDTO(Lancamento l) {
        return new LancamentoResponseDTO(
            l.getId(),
            l.getDescricao(),
            l.getValor(),
            l.getData(),
            l.getTipo(),
            l.getCategoria().getId(),
            l.getCategoria().getNome(),
            l.getUsuario().getId(),
            l.getUsuario().getNome()
        );
    }
}
