package com.rodrigobs.igreja.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	/* ===== 400 – Validação de formulário ===== */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request, // ⬅️ injeta o
																									// request
			RedirectAttributes redirect) {

		List<String> erros = ex.getBindingResult().getFieldErrors().stream()
				.map(fe -> String.format("%s: %s", fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());

		redirect.addFlashAttribute("erros", erros);

		// tenta voltar para a página anterior; se nulo, vai para raiz
		String referrer = request.getHeader("Referer");
		return "redirect:" + (referrer != null ? referrer : "/");
	}

	/* ===== 404 – Entidade não encontrada ===== */
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(EntityNotFoundException ex, Model model) {
		model.addAttribute("mensagem", ex.getMessage());
		return "error/404"; // templates/error/404.html
	}

	/* ===== 409 – Regra de negócio (duplicidade, etc.) ===== */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public String handleIllegalArg(IllegalArgumentException ex, RedirectAttributes redirect) {
		redirect.addFlashAttribute("erro", ex.getMessage());
		return "redirect:/"; // volta à home ou ajuste conforme necessidade
	}

	/* ===== 500 – Qualquer erro não tratado ===== */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGeneric(Exception ex, Model model) {
		model.addAttribute("mensagem", "Ocorreu um erro inesperado.");
		return "error/500";
	}

	/* ---------- helpers ---------- */
	private String formatarErro(FieldError fe) {
		return String.format("%s: %s", fe.getField(), fe.getDefaultMessage());
	}

}
