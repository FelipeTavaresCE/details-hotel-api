package br.com.cvc.consulta.dados.hotel.exception.handler;

import br.com.cvc.consulta.dados.hotel.exception.CampoObrigatorioException;
import br.com.cvc.consulta.dados.hotel.exception.IntegracaoParceiroException;
import br.com.cvc.consulta.dados.hotel.exception.apierror.ApiError;
import br.com.cvc.consulta.dados.hotel.exception.apierror.ApiValidationError;
import br.com.cvc.consulta.dados.hotel.logger.LoggerStepEnum;
import br.com.cvc.consulta.dados.hotel.logger.LoggerUtil;
import br.com.cvc.consulta.dados.hotel.model.constantes.API;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " Parâmetro não informado";
        ApiError apiError = new ApiError(BAD_REQUEST, error, ex);
        apiError.setCode(LoggerStepEnum.HTE0006.getStep());
        return buildResponseEntity(logErro(apiError));
    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" tipo de midia não suportado. Os tipos suportados são ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        return buildResponseEntity(logErro(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex)));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage("Erro de Validação");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        apiError.setCode(LoggerStepEnum.HTE0007.getStep());
        return buildResponseEntity(logErro(apiError));
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        apiError.setCode(LoggerStepEnum.HTE0007.getStep());
        return buildResponseEntity(logErro(apiError));
    }

    /**
     * Handles EntityNotFoundException. Created to encapsulate errors with more detail than javax.persistence.EntityNotFoundException.
     *
     * @param ex the EntityNotFoundException
     * @return the ApiError object
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(logErro(apiError));
    }

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "requisição com JSON Malformado";
        return buildResponseEntity(logErro(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, error, ex)));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the ApiError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Erro ao escrever saíde do JSON";
        return buildResponseEntity(logErro(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex)));
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
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(logErro(apiError));
    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(logErro(new ApiError(HttpStatus.CONFLICT, "Erro na Base de Dados", ex.getCause())));
        }
        return buildResponseEntity(logErro(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                        WebRequest request) {
        ApiError apiError = new ApiError(INTERNAL_SERVER_ERROR, ex);
        apiError.setCode(LoggerStepEnum.HTE0008.getStep());
        return buildResponseEntity(logErro(apiError));
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("O parâmetro '%s' com o valor '%s' não pode ser convertido para o tipo '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiError.setDebugMessage(ex.getMessage());
        apiError.setCode(LoggerStepEnum.HTE0009.getStep());
        return buildResponseEntity(logErro(apiError));
    }

    @ExceptionHandler(IntegracaoParceiroException.class)
    protected ResponseEntity<Object> handleIntegracaoParceiroException(IntegracaoParceiroException ex,
                                                               WebRequest request) {
        ApiError apiError = new ApiError(BAD_GATEWAY);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(LoggerStepEnum.HTE0011.getMessage());
        apiError.setCode(LoggerStepEnum.HTE0011.getStep());
        return buildResponseEntity(logErro(apiError));
    }


    @ExceptionHandler(CampoObrigatorioException.class)
    protected ResponseEntity<Object> handleCampoObrigatorioException(CampoObrigatorioException ex) {
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(logErro(apiError));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity handleFeignStatusException(FeignException e, HttpServletResponse response) throws IOException {
        response.setStatus(e.status());
        return new ResponseEntity(new ObjectMapper().readValue(e.contentUTF8(), Object.class), HttpStatus.valueOf(e.status()));
    }


    @ExceptionHandler(RollbackException.class)
    protected ResponseEntity<Object> handleRollbackException(RollbackException ex,
                                                             WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(LoggerStepEnum.HTE0003.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        apiError.setSubErrors(preencherException(ex, LoggerStepEnum.HTE0003.getMessage()));
        return buildResponseEntity(logErro(apiError));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(LoggerStepEnum.HTE0004.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        apiError.setSubErrors(preencherException(ex, LoggerStepEnum.HTE0004.getMessage()));
        return buildResponseEntity(logErro(apiError));
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ApiError logErro(ApiError apiError) {
        LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HTE0004.getStep(), LoggerStepEnum.HTE0004.getMessage(), "",
                apiError, "");
        return apiError;
    }

    public static List<ApiValidationError> preencherException(Exception e, String titulo) {
        List<ApiValidationError> erros = new ArrayList<>();

        if (e instanceof RollbackException &&
                e.getCause() instanceof javax.validation.ConstraintViolationException) {

            ((javax.validation.ConstraintViolationException) e.getCause())
                    .getConstraintViolations().forEach(ex -> {
                        String mensagem = "O Campo: ".concat(String.valueOf(ex.getPropertyPath())).concat(" " + ex.getMessage());
                        erros.add(ApiValidationError
                                .builder()
                                .message(mensagem)
                                .build());
                    }
            );
        } else {
            erros.add(ApiValidationError
                    .builder()
                    .message(titulo)
                    .object(e.getMessage())
                    .build());
        }

        return erros;
    }
}
