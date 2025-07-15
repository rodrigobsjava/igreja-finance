package com.rodrigobs.igreja.controller;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rodrigobs.igreja.dto.request.LancamentoRequestDTO;
import com.rodrigobs.igreja.service.CategoriaService;
import com.rodrigobs.igreja.service.LancamentoService;
import com.rodrigobs.igreja.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    /* ========== LISTAGEM ========== */

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lancamentos", lancamentoService.listarTodos());
        return "lancamentos/list";           // → templates/lancamentos/list.html
    }

    /* ========== FORMULÁRIO DE NOVO ========== */

    @GetMapping("/novo")
    public String novo(Model model) {
        popularCombos(model);
        model.addAttribute("lancamento", new LancamentoRequestDTO());
        return "lancamentos/form-lancamento";
    }

    /* ========== SALVAR (CREATE) ========== */

    @PostMapping
    public String salvar(@Valid @ModelAttribute("lancamento") LancamentoRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model) {
        if (result.hasErrors()) {
            popularCombos(model);
            return "lancamentos/form-lancamento";
        }
        lancamentoService.criar(dto);
        redirect.addFlashAttribute("sucesso", "Lançamento cadastrado!");
        return "redirect:/lancamentos";
    }

    /* ========== FORMULÁRIO DE EDIÇÃO ========== */

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable UUID id, Model model) {
        var dto = lancamentoService.buscarPorId(id);
        var form = new LancamentoRequestDTO(
                dto.getDescricao(),
                dto.getValor(),
                dto.getData(),
                dto.getTipo(),
                dto.getCategoriaId(),
                dto.getUsuarioId()
        );
        popularCombos(model);
        model.addAttribute("lancamento", form);
        model.addAttribute("lancamentoId", id);
        return "lancamentos/form-lancamento";
    }

    /* ========== ATUALIZAR (UPDATE) ========== */

    @PostMapping("/{id}")
    public String atualizar(@PathVariable UUID id,
                            @Valid @ModelAttribute("lancamento") LancamentoRequestDTO dto,
                            BindingResult result,
                            RedirectAttributes redirect,
                            Model model) {
        if (result.hasErrors()) {
            popularCombos(model);
            return "lancamentos/form-lancamento";
        }
        lancamentoService.atualizar(id, dto);
        redirect.addFlashAttribute("sucesso", "Lançamento atualizado!");
        return "redirect:/lancamentos";
    }

    /* ========== EXCLUIR (DELETE) ========== */

    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable UUID id,
                          RedirectAttributes redirect) {
        lancamentoService.deletar(id);
        redirect.addFlashAttribute("sucesso", "Lançamento excluído!");
        return "redirect:/lancamentos";
    }

    /* ========== HELPERS ========== */

    /** Popula combos de Categoria e Usuario para o formulário */
    private void popularCombos(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());   // List<CategoriaResponseDTO>
        model.addAttribute("usuarios", usuarioService.listarTodos());       // List<UsuarioResponseDTO>
    }
}
