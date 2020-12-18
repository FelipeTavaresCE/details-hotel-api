package br.com.cvc.consulta.dados.hotel.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class RoomDetail implements Serializable {
	
	private Integer roomID;
	private String categoryName;
	private BigDecimal totalPrice;
	private PriceDetail priceDetail;

}
