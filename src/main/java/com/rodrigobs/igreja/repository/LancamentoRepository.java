package com.rodrigobs.igreja.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rodrigobs.igreja.model.Lancamento;
import com.rodrigobs.igreja.model.TipoLancamento;
import com.rodrigobs.igreja.model.Usuario;

public interface LancamentoRepository extends JpaRepository<Lancamento, UUID> {
	List<Lancamento> findByUsuario(Usuario usuario);

	@Query("""
			SELECT COALESCE(SUM(l.valor), 0)
			FROM Lancamento l
			WHERE l.tipo = :tipo
			  AND l.data BETWEEN :inicio AND :fim
			""")
	BigDecimal somaPorTipoEPeriodo(@Param("tipo") TipoLancamento tipo, @Param("inicio") LocalDate inicio,
			@Param("fim") LocalDate fim);
}