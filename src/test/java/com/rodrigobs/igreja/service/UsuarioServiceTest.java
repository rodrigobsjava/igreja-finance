package com.rodrigobs.igreja.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rodrigobs.igreja.dto.request.UsuarioRequestDTO;
import com.rodrigobs.igreja.dto.response.UsuarioResponseDTO;
import com.rodrigobs.igreja.exception.EmailJaCadastradoException;
import com.rodrigobs.igreja.model.TipoUsuario;
import com.rodrigobs.igreja.model.Usuario;
import com.rodrigobs.igreja.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDTO request;

    @BeforeEach
    void setUp() {
        request = new UsuarioRequestDTO("Maria", "maria@ex.com", "123", TipoUsuario.ADMINISTRADOR);
    }

    @Test
    void deveCriarUsuarioQuandoEmailNaoExiste() {
        // given
        when(usuarioRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // mock save retornando entidade com ID
        Usuario entity = new Usuario(UUID.randomUUID(), request.getNome(), request.getEmail(),
                                     request.getSenha(), request.getTipo());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(entity);

        // when
        UsuarioResponseDTO resposta = usuarioService.criar(request);

        // then
        assertThat(resposta.getId()).isNotNull();
        assertThat(resposta.getEmail()).isEqualTo(request.getEmail());

        // capturar entidade salva
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getNome()).isEqualTo("Maria");
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // given
        Usuario jaExistente = new Usuario();
        when(usuarioRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(jaExistente));

        // when / then
        assertThatThrownBy(() -> usuarioService.criar(request))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining("E-mail já cadastrado");
    }
}
