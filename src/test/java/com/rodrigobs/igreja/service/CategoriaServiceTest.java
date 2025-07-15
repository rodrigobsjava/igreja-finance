package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rodrigobs.igreja.dto.request.CategoriaRequestDTO;
import com.rodrigobs.igreja.dto.response.CategoriaResponseDTO;
import com.rodrigobs.igreja.mapper.CategoriaMapper;
import com.rodrigobs.igreja.model.Categoria;
import com.rodrigobs.igreja.model.TipoLancamento;
import com.rodrigobs.igreja.repository.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private CategoriaRequestDTO requestEntrada;
    private Categoria categoriaEntrada;

    @BeforeEach
    void setup() {
        requestEntrada = new CategoriaRequestDTO("Ofertas", TipoLancamento.ENTRADA);
        categoriaEntrada = CategoriaMapper.toEntity(requestEntrada);
        categoriaEntrada.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("Deve criar categoria com sucesso")
    void criarCategoria_success() {
        when(categoriaRepository.save(any())).thenReturn(categoriaEntrada);

        CategoriaResponseDTO dto = categoriaService.criar(requestEntrada);

        assertThat(dto.getId()).isEqualTo(categoriaEntrada.getId());
        assertThat(dto.getNome()).isEqualTo("Ofertas");
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    @DisplayName("Deve listar todas as categorias")
    void listarTodas_success() {
        var cat2 = new Categoria(UUID.randomUUID(), "Despesas", TipoLancamento.SAIDA);
        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaEntrada, cat2));

        List<CategoriaResponseDTO> lista = categoriaService.listarTodas();

        assertThat(lista).hasSize(2);
        assertThat(lista).extracting(CategoriaResponseDTO::getNome)
                         .containsExactlyInAnyOrder("Ofertas", "Despesas");
    }

    @Nested
    class BuscarPorId {

        @Test
        @DisplayName("Deve retornar categoria existente")
        void buscarPorId_success() {
            when(categoriaRepository.findById(categoriaEntrada.getId()))
                    .thenReturn(Optional.of(categoriaEntrada));

            CategoriaResponseDTO dto = categoriaService.buscarPorId(categoriaEntrada.getId());

            assertThat(dto.getNome()).isEqualTo("Ofertas");
        }

        @Test
        @DisplayName("Deve lançar 404 se categoria não encontrada")
        void buscarPorId_notFound() {
            UUID idInexistente = UUID.randomUUID();
            when(categoriaRepository.findById(idInexistente)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoriaService.buscarPorId(idInexistente))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Categoria não encontrada");
        }
    }

    @Nested
    class AtualizarCategoria {

        @Test
        @DisplayName("Deve atualizar categoria existente")
        void atualizar_success() {
            UUID id = categoriaEntrada.getId();
            when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaEntrada));
            when(categoriaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var reqUpdate = new CategoriaRequestDTO("Dízimos", TipoLancamento.ENTRADA);

            CategoriaResponseDTO atualizado = categoriaService.atualizar(id, reqUpdate);

            assertThat(atualizado.getNome()).isEqualTo("Dízimos");
            verify(categoriaRepository).save(any(Categoria.class));
        }

        @Test
        @DisplayName("Deve lançar 404 se categoria para atualizar não existir")
        void atualizar_notFound() {
            UUID id = UUID.randomUUID();
            when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> categoriaService.atualizar(id, requestEntrada))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class DeletarCategoria {

        @Test
        @DisplayName("Deve deletar categoria existente")
        void deletar_success() {
            UUID id = categoriaEntrada.getId();
            when(categoriaRepository.existsById(id)).thenReturn(true);
            doNothing().when(categoriaRepository).deleteById(id);

            categoriaService.deletar(id);

            verify(categoriaRepository).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar 404 se categoria a deletar não existir")
        void deletar_notFound() {
            UUID idInexistente = UUID.randomUUID();
            when(categoriaRepository.existsById(idInexistente)).thenReturn(false);

            assertThatThrownBy(() -> categoriaService.deletar(idInexistente))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }
}
