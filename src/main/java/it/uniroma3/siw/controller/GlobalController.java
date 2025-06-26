package it.uniroma3.siw.controller;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalController {
  @ModelAttribute("userDetails")
  public UserDetails getUser() {
    UserDetails user = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    return user;
  }
  

  
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
      model.addAttribute("errorMessage", "L'id fornito non è valido.");
      return "/error/500.html";
  }
  
  @ExceptionHandler(SpelEvaluationException.class)
  public String handleSpelEvaluation (SpelEvaluationException ex, Model model) {
      model.addAttribute("errorMessage", ex.getMessage());
      return "/error/500.html";
  }
  
  @ExceptionHandler(NoSuchElementException.class)
  public String handleNoSuchElement (NoSuchElementException ex, Model model) {
      model.addAttribute("errorMessage", ex.getMessage());
      return "/error/500.html";
  }
  
  @ExceptionHandler(RuntimeException.class)
  public String handleRunTime(RuntimeException ex, Model model) {
      model.addAttribute("errorMessage", ex.getMessage());
      return "/error/500.html";
  }
  
  @ExceptionHandler(DataIntegrityViolationException.class)
  public String handleDataIntegrityViolation (DataIntegrityViolationException ex, Model model) {
      model.addAttribute("errorMessage", ex.getMostSpecificCause().getMessage());
      return "/error/500.html";
  }
  
}
