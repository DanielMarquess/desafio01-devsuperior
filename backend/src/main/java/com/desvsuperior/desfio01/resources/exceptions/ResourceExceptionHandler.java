package com.desvsuperior.desfio01.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.desvsuperior.desfio01.services.exceptions.DatabaseException;
import com.desvsuperior.desfio01.services.exceptions.ResourceNotFoundException;


//Intercepta exceções que acontecerem no controlador REST

@ControllerAdvice
public class ResourceExceptionHandler {
	//Vai ser uma resposta de requisição onde o conteúdo da resposta vai ser um StandarError
	//HttpServletRequest vai ter as informações da requisição
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource Not Found");
		err.setMsg(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimeStamp(Instant.now());
		err.setStatus(status.value()); //BAD_REQUEST é utilizado quando dá um erro de requisição e você não quer especificar
		err.setError("Database exception");
		err.setMsg(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
}

