package com.example.demo.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String dataIntegrityViolationException(DataIntegrityViolationException e, Model model)
	{
		model.addAttribute("msg", e.getMessage());
		return "error";
		
	}
	
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String illegalArgumentException(IllegalArgumentException e, Model model)
	{
		model.addAttribute("msg", e.getMessage());
		return "error";
		
	}
	
	
	
	
	
	
}
