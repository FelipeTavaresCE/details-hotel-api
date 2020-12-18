package br.com.cvc.consulta.dados.hotel.service.impl;

import br.com.cvc.consulta.dados.hotel.client.ParceirosIntegracaoClient;
import br.com.cvc.consulta.dados.hotel.exception.CampoObrigatorioException;
import br.com.cvc.consulta.dados.hotel.exception.IntegracaoParceiroException;
import br.com.cvc.consulta.dados.hotel.exception.PeriodoInvalidoException;
import br.com.cvc.consulta.dados.hotel.logger.LoggerStepEnum;
import br.com.cvc.consulta.dados.hotel.logger.LoggerUtil;
import br.com.cvc.consulta.dados.hotel.model.constantes.API;
import br.com.cvc.consulta.dados.hotel.model.dto.*;
import br.com.cvc.consulta.dados.hotel.service.HotelService;
import br.com.cvc.consulta.dados.hotel.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Value("${dados.api.comissao}")
    private BigDecimal comissao;

    private final ParceirosIntegracaoClient parceirosIntegracaoClient;

    public HotelServiceImpl(ParceirosIntegracaoClient parceirosIntegracaoClient) {
        this.parceirosIntegracaoClient = parceirosIntegracaoClient;
    }

    @Override
    public List<HotelDetail> obterHotel(TotalReservaRequest totalReservaRequest) {
        ResponseEntity<List<Hotel>> response =
                parceirosIntegracaoClient.buscarHotelPorId(totalReservaRequest.getId());

        tratarError(null, response, null);

        if (response != null && response.getBody() != null && !response.getBody().isEmpty()) {
            List<HotelDetail> hotelDetails = new ArrayList<>();
            Integer qtdDiarias = verificaQtdDias(totalReservaRequest);

            calculaValorHotel(totalReservaRequest, hotelDetails, qtdDiarias, response.getBody());

            return hotelDetails;
        }

        return new ArrayList<>();

    }

    @Override
    public TotalReservaRequest populaDTO(Long id, String checkin, String checkout, Integer qtdAdulto, Integer qtdCrianca) {
        TotalReservaRequest totalReservaRequest = TotalReservaRequest.builder().id(id).checkin(checkin).checkout(checkout).qtdAdulto(qtdAdulto).qtdCrianca(qtdCrianca).build();
        validaCamposObrigatorios(totalReservaRequest);
        return totalReservaRequest;
    }

    private List<Hotel> buscarHotelPorCidade(Long idCidade) {
        ResponseEntity<List<Hotel>> response =
                parceirosIntegracaoClient.buscarHotelPorCidade(idCidade);

        tratarError(null, response, null);

        if (response != null && response.getBody() != null && !response.getBody().isEmpty()) {
            return response.getBody();
        }

        return new ArrayList<>();

    }

    @Override
    public List<HotelDetail> obterHoteisPorCidade(TotalReservaRequest totalReservaRequest) {

        List<HotelDetail> hotelDetails = new ArrayList<>();
        Integer qtdDiarias = verificaQtdDias(totalReservaRequest);

        List<Hotel> hoteis = buscarHotelPorCidade(totalReservaRequest.getId());

        calculaValorHotel(totalReservaRequest, hotelDetails, qtdDiarias, hoteis);


        return hotelDetails;
    }

    private Integer verificaQtdDias(TotalReservaRequest totalReservaRequest) {
        Integer qtdDiarias = Utils.diferencaEmDias(totalReservaRequest.getCheckin(), totalReservaRequest.getCheckout());

        if (qtdDiarias < 1) {
            throw new PeriodoInvalidoException("Período checkin e checkout inválido");
        }
        return qtdDiarias;
    }

    private void calculaValorHotel(TotalReservaRequest totalReservaRequest, List<HotelDetail> hotelDetails, Integer qtdDiarias, List<Hotel> hoteis) {
        hoteis.stream().forEach((Hotel h) -> {
            HotelDetail hd = new HotelDetail();
            hd.setId(h.getId());
            hd.setCityName(h.getCityName());
            hd.setRooms(new ArrayList<>());

            h.getRooms().stream().forEach((Room r) -> {
                RoomDetail rd = new RoomDetail();
                rd.setRoomID(r.getRoomID());
                rd.setCategoryName(r.getCategoryName());

                calculaPreco(totalReservaRequest, qtdDiarias, r, rd);

                hd.getRooms().add(rd);
            });

            hotelDetails.add(hd);
        });
    }

    private void calculaPreco(TotalReservaRequest totalReservaRequest, Integer qtdDiarias, Room r, RoomDetail rd) {

        PriceDetail pd = new PriceDetail();
        pd.setPricePerDayAdult(r.getPrice().getAdult().add(r.getPrice().getAdult().divide(comissao, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN)));
        pd.setPricePerDayChild(r.getPrice().getChild().add(r.getPrice().getChild().divide(comissao, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN)));
        rd.setPriceDetail(pd);

        BigDecimal totalAdultos = pd.getPricePerDayAdult().multiply(BigDecimal.valueOf(totalReservaRequest.getQtdAdulto())).multiply(BigDecimal.valueOf(qtdDiarias));
        BigDecimal totalCriancas = pd.getPricePerDayChild().multiply(BigDecimal.valueOf(totalReservaRequest.getQtdCrianca())).multiply(BigDecimal.valueOf(qtdDiarias));

        rd.setTotalPrice(totalAdultos.add(totalCriancas));
    }

    private void validaCamposObrigatorios(TotalReservaRequest totalReservaRequest) {
        if (StringUtils.isEmpty(totalReservaRequest.getId())) {
            throw new CampoObrigatorioException("Campo Id é orbigatorio");
        }
        if (StringUtils.isEmpty(totalReservaRequest.getCheckin())) {
            throw new CampoObrigatorioException("Campo Checkin é obrigatorio");
        }
        if (StringUtils.isEmpty(totalReservaRequest.getCheckout())) {
            throw new CampoObrigatorioException("Campo Checkout é obrigatorio");
        }
        if (StringUtils.isEmpty(totalReservaRequest.getQtdAdulto())) {
            throw new CampoObrigatorioException("Campo Quantidade Adulto é orbigatorio");
        }
        if (StringUtils.isEmpty(totalReservaRequest.getQtdCrianca())) {
            throw new CampoObrigatorioException("Campo Quantidade Criança é orbigatorio");
        }
    }

    public void tratarError(HttpEntity<String> request, ResponseEntity<List<Hotel>> responseEntity, String url) {
        if (!responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getStatusCode() != HttpStatus.NOT_FOUND) {
            if (responseEntity.getStatusCode().is4xxClientError()) {
                logByStatus(responseEntity);

                throw new IntegracaoParceiroException();

            } else if (responseEntity.getStatusCode().is5xxServerError()) {
                LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HTE0005.getStep(), LoggerStepEnum.HTE0005.getMessage(), url,
                        request, responseEntity);
                throw new HttpServerErrorException(HttpStatus.BAD_GATEWAY, HttpStatus.BAD_GATEWAY.getReasonPhrase());
            } else {
                throw new IntegracaoParceiroException();
            }
        }
    }

    private void logByStatus(ResponseEntity<List<Hotel>> responseEntity) {
        LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HTE0005.getStep(), LoggerStepEnum.HTE0005.getMessage(), "",
                "", responseEntity);
    }

}
