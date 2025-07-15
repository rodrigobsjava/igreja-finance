package com.rodrigobs.igreja.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigobs.igreja.dto.request.CategoriaRequestDTO;
import com.rodrigobs.igreja.dto.response.CategoriaResponseDTO;
import com.rodrigobs.igreja.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /* ========== LISTAGEM ========== */

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/list";          // → templates/categorias/list.html
    }

    /* ========== FORMULÁRIO DE NOVO ========== */

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("categoria", new CategoriaRequestDTO());
        return "categorias/form-categoria";
    }

    /* ========== SALVAR (CREATE) ========== */

    @PostMapping
    public String salvar(@Valid @ModelAttribute("categoria") CategoriaRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "categorias/form-categoria";
        }
        categoriaService.criar(dto);
        redirect.addFlashAttribute("sucesso", "Categoria cadastrada!");
        return "redirect:/categorias";
    }

    /* ========== FORMULÁRIO DE EDIÇÃO ========== */

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable UUID id, Model model) {
        CategoriaResponseDTO dto = categoriaService.buscarPorId(id);
        CategoriaRequestDTO form = new CategoriaRequestDTO(dto.getNome(), dto.getTipo());
        model.addAttribute("categoria", form);
        model.addAttribute("categoriaId", id);
        return "categorias/form-categoria";
    }

    /* ========== ATUALIZAR (UPDATE) ========== */

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid @ModelAttribute("categoria") CategoriaRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "categorias/form-categoria";
        }
        categoriaService.atualizar(id, dto);
        redirect.addFlashAttribute("sucesso", "Categoria atualizada!");
        return "redirect:/categorias";
    }

    /* ========== EXCLUIR (DELETE) ========== */

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable UUID id,
                          RedirectAttributes redirect) {
        categoriaService.deletar(id);
        redirect.addFlashAttribute("sucesso", "Categoria excluída!");
        return "redirect:/categorias";
    }
}
