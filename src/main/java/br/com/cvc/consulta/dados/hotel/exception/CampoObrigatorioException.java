package br.com.cvc.consulta.dados.hotel.exception;

public class CampoObrigatorioException extends RuntimeException {

    public CampoObrigatorioException(String message) {
        super(message);
    }
}
