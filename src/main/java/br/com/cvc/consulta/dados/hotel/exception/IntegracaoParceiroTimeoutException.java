package br.com.cvc.consulta.dados.hotel.exception;

public class IntegracaoParceiroTimeoutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -503168157402132516L;

	public IntegracaoParceiroTimeoutException() {
		super("Erro na integração com o API (Serviço excedeu o tempo de resposta)");
	}
	
	public IntegracaoParceiroTimeoutException(String message) {
		super(message);
	}
	
	

}
