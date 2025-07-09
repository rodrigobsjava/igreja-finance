package com.rodrigobs.igreja.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.igreja.model.Lancamento;
import com.rodrigobs.igreja.model.Usuario;

public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
	List<Lancamento> findByUsuario(Usuario usuario);
}