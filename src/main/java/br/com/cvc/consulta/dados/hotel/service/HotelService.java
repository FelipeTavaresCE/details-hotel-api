package br.com.cvc.consulta.dados.hotel.service;

import br.com.cvc.consulta.dados.hotel.model.dto.HotelDetail;
import br.com.cvc.consulta.dados.hotel.model.dto.TotalReservaRequest;

import java.util.List;

public interface HotelService {

    public List<HotelDetail> obterHotel(TotalReservaRequest idHotel);
    public TotalReservaRequest populaDTO(Long cityCode, String checkin, String checkout, Integer qtdAdulto, Integer qtdCrianca);
    public List<HotelDetail> obterHoteisPorCidade(TotalReservaRequest totalReservaRequest);

}
