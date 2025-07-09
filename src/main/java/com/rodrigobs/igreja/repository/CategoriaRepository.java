package com.rodrigobs.igreja.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.igreja.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {

}
