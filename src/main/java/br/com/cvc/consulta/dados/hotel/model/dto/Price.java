package br.com.cvc.consulta.dados.hotel.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Price implements Serializable {
	
	private BigDecimal adult;
	private BigDecimal child;

}
