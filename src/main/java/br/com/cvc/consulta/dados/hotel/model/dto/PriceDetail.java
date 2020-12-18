package br.com.cvc.consulta.dados.hotel.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class PriceDetail implements Serializable {

	private BigDecimal pricePerDayAdult;
	private BigDecimal pricePerDayChild;
}
