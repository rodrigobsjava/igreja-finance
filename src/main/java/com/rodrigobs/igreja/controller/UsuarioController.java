package com.rodrigobs.igreja.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigobs.igreja.dto.request.UsuarioRequestDTO;
import com.rodrigobs.igreja.dto.response.UsuarioResponseDTO;
import com.rodrigobs.igreja.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /* ========== LISTAGEM ========== */

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/list";      // → templates/usuarios/list.html
    }

    /* ========== FORMULÁRIO DE NOVO ========== */

    @GetMapping("/novo")
    public String mostrarFormNovo(Model model) {
        model.addAttribute("usuario", new UsuarioRequestDTO());
        return "usuarios/form-usuario";  // → templates/usuarios/form-usuario.html
    }

    /* ========== SALVAR (CREATE) ========== */

    @PostMapping
    public String salvar(@Valid @ModelAttribute("usuario") UsuarioRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
        	redirect.addFlashAttribute("erros", result.getAllErrors());
            return "usuarios/form-usuario";
        }
        usuarioService.criar(dto);
        redirect.addFlashAttribute("sucesso", "Usuário cadastrado!");
        return "redirect:/usuarios";
    }

    /* ========== FORMULÁRIO DE EDIÇÃO ========== */

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable UUID id, Model model) {
        UsuarioResponseDTO dto = usuarioService.buscarPorId(id);
        // Preenche o form com dados existentes
        UsuarioRequestDTO form = new UsuarioRequestDTO(
                dto.getNome(), dto.getEmail(), null, dto.getTipo());
        model.addAttribute("usuario", form);
        model.addAttribute("usuarioId", id);
        return "usuarios/form-usuario";
    }

    /* ========== ATUALIZAR (UPDATE) ========== */

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid @ModelAttribute("usuario") UsuarioRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "usuarios/form-usuario";
        }
        usuarioService.atualizar(id, dto);
        redirect.addFlashAttribute("sucesso", "Usuário atualizado!");
        return "redirect:/usuarios";
    }

    /* ========== EXCLUIR (DELETE) ========== */

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable UUID id,
                          RedirectAttributes redirect) {
        usuarioService.deletar(id);
        redirect.addFlashAttribute("sucesso", "Usuário excluído!");
        return "redirect:/usuarios";
    }
}
