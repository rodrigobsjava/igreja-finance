package com.rodrigobs.igreja.mapper;

import com.rodrigobs.igreja.dto.request.CategoriaRequestDTO;
import com.rodrigobs.igreja.dto.response.CategoriaResponseDTO;
import com.rodrigobs.igreja.model.Categoria;

public class CategoriaMapper {
	 public static Categoria toEntity(CategoriaRequestDTO dto) {
	        Categoria categoria = new Categoria();
	        categoria.setNome(dto.getNome());
	        categoria.setTipo(dto.getTipo());
	        return categoria;
	    }

	    public static CategoriaResponseDTO toDTO(Categoria categoria) {
	        return new CategoriaResponseDTO(
	            categoria.getId(),
	            categoria.getNome(),
	            categoria.getTipo()
	        );
	    }
}
