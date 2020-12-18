package br.com.cvc.consulta.dados.hotel.exception;

public class IntegracaoParceiroException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2660515783248707536L;

	public IntegracaoParceiroException() {
		super("Erro na integração com a API!");
	}
	
	public IntegracaoParceiroException(String message) {
		super(message);
	}

}
