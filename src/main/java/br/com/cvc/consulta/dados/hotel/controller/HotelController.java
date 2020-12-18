package br.com.cvc.consulta.dados.hotel.controller;

import br.com.cvc.consulta.dados.hotel.logger.LoggerStepEnum;
import br.com.cvc.consulta.dados.hotel.logger.LoggerUtil;
import br.com.cvc.consulta.dados.hotel.model.constantes.API;
import br.com.cvc.consulta.dados.hotel.model.dto.HotelDetail;
import br.com.cvc.consulta.dados.hotel.model.dto.TotalReservaRequest;
import br.com.cvc.consulta.dados.hotel.service.HotelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(API.VERSAO+"/hotels")
@Api(value = "API REST Consulta Hoteis")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value="Detalhes do Hotel por código de hotel", response=ResponseEntity.class, responseContainer = "Set")
	@GetMapping("/{id}")
	public ResponseEntity<Object> buscarHotelporId(@PathVariable Long id,
												   @RequestParam(value="checkin", required = true) String checkin,
												   @RequestParam(value="checkout", required = true) String checkout,
												   @RequestParam(value="qtdAdulto", required = true) Integer qtdAdulto,
												   @RequestParam(value="qtdCrianca", required = true) Integer qtdCrianca) {
		LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HT00001.getStep(), LoggerStepEnum.HT00001.getMessage(), "",
				"buscarHotelporId", "");
		TotalReservaRequest totalReservaRequest = hotelService.populaDTO(id,checkin,checkout,qtdAdulto,qtdCrianca);
		List<HotelDetail> hotelDetails = hotelService.obterHotel(totalReservaRequest);

		LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HT00002.getStep(), LoggerStepEnum.HT00002.getMessage(), "",
				"buscarHotelporId", hotelDetails);

		return ResponseEntity.ok().body(hotelDetails);

	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value="Obter valores de Hospedagem", response=ResponseEntity.class, responseContainer = "Set")
	@GetMapping
	public ResponseEntity buscarHotelPorCidade(@RequestParam(value="cityCode", required = true) Long cityCode,
											   @RequestParam(value="checkin", required = true) String checkin,
											   @RequestParam(value="checkout", required = true) String checkout,
											   @RequestParam(value="qtdAdulto", required = true) Integer qtdAdulto,
											   @RequestParam(value="qtdCrianca", required = true) Integer qtdCrianca) {
		LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HT00003.getStep(), LoggerStepEnum.HT00003.getMessage(), "",
				"buscarHotelPorCidade", "");

		TotalReservaRequest totalReservaRequest = hotelService.populaDTO(cityCode,checkin,checkout,qtdAdulto,qtdCrianca);
		List<HotelDetail> hotelDetails = hotelService.obterHoteisPorCidade(totalReservaRequest);

		LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HT00004.getStep(), LoggerStepEnum.HT00004.getMessage(), "",
				"buscarHotelPorCidade", hotelDetails);

		return ResponseEntity.ok().body(hotelDetails);
	}
	
}
