package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import com.rodrigobs.igreja.dto.request.LancamentoRequestDTO;
import com.rodrigobs.igreja.dto.response.LancamentoResponseDTO;
import com.rodrigobs.igreja.mapper.LancamentoMapper;
import com.rodrigobs.igreja.model.Categoria;
import com.rodrigobs.igreja.model.Lancamento;
import com.rodrigobs.igreja.model.TipoLancamento;
import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.CategoriaRepository;
import com.rodrigobs.igreja.repository.LancamentoRepository;
import com.rodrigobs.igreja.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class LancamentoServiceTest {

    @Mock LancamentoRepository lancamentoRepository;
    @Mock CategoriaRepository categoriaRepository;
    @Mock UsuarioRepository usuarioRepository;

    @InjectMocks LancamentoService lancamentoService;

    private UUID catId;
    private UUID userId;
    private Categoria categoria;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        catId = UUID.randomUUID();
        userId = UUID.randomUUID();
        categoria = new Categoria(catId, "Dízimos", TipoLancamento.ENTRADA);
        usuario  = new Usuario(userId, "Ana", "ana@ig.com", "123", TipoUsuario.TESOUREIRA);
    }

    /* ---------- CREATE ---------- */

    @Test
    @DisplayName("Deve criar lançamento quando usuário e categoria existem")
    void criarLancamento_success() {
        // arrange
        when(categoriaRepository.findById(catId)).thenReturn(Optional.of(categoria));
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        LancamentoRequestDTO req = new LancamentoRequestDTO(
                "Oferta especial",
                new BigDecimal("150.00"),
                LocalDate.now(),
                TipoLancamento.ENTRADA,
                catId,
                userId);

        when(lancamentoRepository.save(any())).thenAnswer(inv -> {
            Lancamento l = inv.getArgument(0);
            l.setId(UUID.randomUUID());
            return l;
        });

        // act
        LancamentoResponseDTO resp = lancamentoService.criar(req);

        // assert
        assertThat(resp.getDescricao()).isEqualTo("Oferta especial");
        assertThat(resp.getValor()).isEqualByComparingTo("150.00");
        assertThat(resp.getCategoriaNome()).isEqualTo("Dízimos");
        verify(lancamentoRepository).save(any(Lancamento.class));
    }

    @Test
    @DisplayName("Deve lançar 404 se categoria não existir no create")
    void criarLancamento_categoria404() {
        when(categoriaRepository.findById(catId)).thenReturn(Optional.empty());
        LancamentoRequestDTO req = new LancamentoRequestDTO("x", BigDecimal.ONE,
                LocalDate.now(), TipoLancamento.SAIDA, catId, userId);

        assertThatThrownBy(() -> lancamentoService.criar(req))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Categoria não encontrada");
    }

    @Test
    @DisplayName("Deve lançar 404 se usuário não existir no create")
    void criarLancamento_usuario404() {
        when(categoriaRepository.findById(catId)).thenReturn(Optional.of(categoria));
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        LancamentoRequestDTO req = new LancamentoRequestDTO("x", BigDecimal.ONE,
                LocalDate.now(), TipoLancamento.SAIDA, catId, userId);

        assertThatThrownBy(() -> lancamentoService.criar(req))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Usuário não encontrado");
    }

    /* ---------- LIST & GET ---------- */

    @Test
    @DisplayName("Deve listar todos os lançamentos")
    void listarTodos_success() {
        Lancamento l1 = LancamentoMapper.toEntity(
                new LancamentoRequestDTO("A", BigDecimal.TEN, LocalDate.now(),
                                         TipoLancamento.ENTRADA, catId, userId),
                categoria, usuario);
        l1.setId(UUID.randomUUID());

        when(lancamentoRepository.findAll()).thenReturn(List.of(l1));

        List<LancamentoResponseDTO> lista = lancamentoService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getDescricao()).isEqualTo("A");
    }

    @Nested
    class BuscarPorId {

        @Test
        void buscarPorId_success() {
            Lancamento lanc = LancamentoMapper.toEntity(
                    new LancamentoRequestDTO("B", BigDecimal.ONE, LocalDate.now(),
                                             TipoLancamento.SAIDA, catId, userId),
                    categoria, usuario);
            UUID id = UUID.randomUUID();
            lanc.setId(id);

            when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lanc));

            LancamentoResponseDTO dto = lancamentoService.buscarPorId(id);

            assertThat(dto.getId()).isEqualTo(id);
            assertThat(dto.getTipo()).isEqualTo(TipoLancamento.SAIDA);
        }

        @Test
        void buscarPorId_404() {
            UUID id = UUID.randomUUID();
            when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> lancamentoService.buscarPorId(id))
                .isInstanceOf(EntityNotFoundException.class);
        }
    }

    /* ---------- UPDATE ---------- */

    @Test
    @DisplayName("Deve atualizar lançamento existente")
    void atualizar_success() {
        UUID lancId = UUID.randomUUID();
        Lancamento existente = LancamentoMapper.toEntity(
                new LancamentoRequestDTO("Old", BigDecimal.ONE, LocalDate.now(),
                                         TipoLancamento.ENTRADA, catId, userId),
                categoria, usuario);
        existente.setId(lancId);

        when(lancamentoRepository.findById(lancId)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findById(catId)).thenReturn(Optional.of(categoria));
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(lancamentoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LancamentoRequestDTO updateReq = new LancamentoRequestDTO(
                "Novo nome", new BigDecimal("55"), LocalDate.now(),
                TipoLancamento.SAIDA, catId, userId);

        LancamentoResponseDTO dto = lancamentoService.atualizar(lancId, updateReq);

        assertThat(dto.getDescricao()).isEqualTo("Novo nome");
        assertThat(dto.getTipo()).isEqualTo(TipoLancamento.SAIDA);
    }

    @Test
    @DisplayName("Update deve lançar 404 se lançamento não existe")
    void atualizar_notFound() {
        UUID lancId = UUID.randomUUID();
        when(lancamentoRepository.findById(lancId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lancamentoService.atualizar(lancId,
                new LancamentoRequestDTO("x", BigDecimal.ONE, LocalDate.now(),
                                         TipoLancamento.ENTRADA, catId, userId)))
            .isInstanceOf(EntityNotFoundException.class);
    }

    /* ---------- DELETE ---------- */

    @Nested
    class Deletar {

        @Test
        void deletar_success() {
            UUID id = UUID.randomUUID();
            when(lancamentoRepository.existsById(id)).thenReturn(true);
            doNothing().when(lancamentoRepository).deleteById(id);

            lancamentoService.deletar(id);

            verify(lancamentoRepository).deleteById(id);
        }

        @Test
        void deletar_notFound() {
            UUID id = UUID.randomUUID();
            when(lancamentoRepository.existsById(id)).thenReturn(false);

            assertThatThrownBy(() -> lancamentoService.deletar(id))
                .isInstanceOf(EntityNotFoundException.class);
        }
    }
}
