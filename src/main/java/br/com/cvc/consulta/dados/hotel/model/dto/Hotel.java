package br.com.cvc.consulta.dados.hotel.model.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel implements Serializable {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String name;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long cityCode;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cityName;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<Room> rooms;
}
