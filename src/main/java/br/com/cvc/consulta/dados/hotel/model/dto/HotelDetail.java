package br.com.cvc.consulta.dados.hotel.model.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelDetail implements Serializable {
	
	private Long id;
	private String cityName;
	private List<RoomDetail> rooms;

}
