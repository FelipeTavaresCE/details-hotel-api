package br.com.cvc.consulta.dados.hotel.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Room implements Serializable {

	private Integer roomID;
	private String categoryName;
	private Price price;
}
