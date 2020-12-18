package br.com.cvc.consulta.dados.hotel.client;

import br.com.cvc.consulta.dados.hotel.model.dto.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@FeignClient(name = "parceirosIntegracaoClient", url = "${parceirosUrl.api.url}")
public interface ParceirosIntegracaoClient {

    @GetMapping("{idHotel}")
    ResponseEntity<List<Hotel>> buscarHotelPorId(@PathVariable Long idHotel);

    @GetMapping("${parceirosUrl.api.endpoint.city}"+"{idCidade}")
    ResponseEntity<List<Hotel>> buscarHotelPorCidade(@PathVariable Long idCidade);

}
