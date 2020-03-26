package com.tech.mkblogs.exception;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tech.mkblogs.response.AccountErrorResponse;
import com.tech.mkblogs.response.ErrorObject;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	public RestExceptionHandler() {
		
	}
	
    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the AccountErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
    	log.info("| Request Time - Start - handleMissingServletRequestParameter() " + LocalTime.now());
        String error = ex.getParameterName() + " parameter is missing";
        AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("100", ex.getParameterName(), error);
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the AccountErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
    	log.info("| Request Time - Start - handleHttpMediaTypeNotSupported() " + LocalTime.now());
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("101", "media type is not supported", builder.toString());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the AccountErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
    	log.info("| Request Time - Start - handleMethodArgumentNotValid() " + LocalTime.now());
    	AccountErrorResponse response = new AccountErrorResponse();
    	for(FieldError error : ex.getBindingResult().getFieldErrors()) {
    		ErrorObject errorObject = constructErrorObject(error.getCode(), error.getField(), error.getDefaultMessage());
    	    response.addErrorObject(errorObject);
    	}
    	for(ObjectError error : ex.getBindingResult().getGlobalErrors()) {
    		ErrorObject errorObject = constructErrorObject(error.getCode(), error.getObjectName(), error.getDefaultMessage());
    	    response.addErrorObject(errorObject);
    	}
        return buildResponseEntity(response);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the AccountErrorResponse object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
    	log.info("| Request Time - Start - handleConstraintViolation() " + LocalTime.now());
    	AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("103", "GeneralError", ex.getConstraintViolations().toString());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the AccountErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("| Request Time - Start - handleHttpMessageNotReadable() " + LocalTime.now());
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        
        AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("104", "MessageNotReadable", ex.toString());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the AccountErrorResponse object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	log.info("| Request Time - Start - handleHttpMessageNotWritable() " + LocalTime.now());
    	AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("105", "MessageNotWritable", ex.toString());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle NoHandlerFoundException.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	log.info("| Request Time - Start - handleNoHandlerFoundException() " + LocalTime.now());
        String msg = String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL());
        AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("106", "NoHandlerFound", msg);
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle EmptyResultDataAccessException
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EmptyResultDataAccessException ex) {
    	log.info("| Request Time - Start - handleEntityNotFound() " + LocalTime.now());
    	AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("107", "EmptyResultData", ex.getMessage());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the AccountErrorResponse object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
    	
    	log.info("| Request Time - Start - handleDataIntegrityViolation() " + LocalTime.now());
    	AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = new ErrorObject();
        errorObject.setErrorCode("108");
        if (ex.getCause() instanceof ConstraintViolationException) {
        	errorObject.setErrorDescription("Database error" + ex.getMessage());
            response.addErrorObject(errorObject);
        }
        errorObject.setErrorDescription(ex.getMessage());
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the AccountErrorResponse object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
    	log.info("| Request Time - Start - handleMethodArgumentTypeMismatch() " + LocalTime.now());
        String msg = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        AccountErrorResponse response = new AccountErrorResponse();
        ErrorObject errorObject = constructErrorObject("109", "MethodArgumentTypeMismatch", msg);
        response.addErrorObject(errorObject);
        return buildResponseEntity(response);
    }
    
   /**
    * 
    * @param errorCode
    * @param errorField
    * @param errorDesc
    * @return
    */
    protected ErrorObject constructErrorObject(String errorCode,String errorField,String errorDesc) {
    	ErrorObject errorObject = new ErrorObject();
    	errorObject.setErrorCode(errorCode);
    	errorObject.setFieldName(errorField);
    	errorObject.setErrorDescription(errorDesc);
    	errorObject.setErrorCreatedTs(Timestamp.from(Instant.now()));
    	return errorObject;
    }
    
    private ResponseEntity<Object> buildResponseEntity(AccountErrorResponse response) {
        return ResponseEntity.ok().body(response);
    }

}
