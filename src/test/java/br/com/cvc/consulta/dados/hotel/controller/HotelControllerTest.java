package br.com.cvc.consulta.dados.hotel.controller;


import br.com.cvc.consulta.dados.hotel.model.constantes.API;
import br.com.cvc.consulta.dados.hotel.model.dto.HotelDetail;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HotelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("unchecked")
	@Test
	public void buscarHotelPorCidade() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API.VERSAO + "/hotels")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("cityCode", "1032")
				.param("checkin", "20/05/2021")
				.param("checkout", "21/05/2021")
				.param("qtdAdulto", "2")
				.param("qtdCrianca", "1"));


		List<HotelDetail> list = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), List.class);

		for (Object obj : list) {
			HotelDetail c = new Gson().fromJson(new Gson().toJson(obj), HotelDetail.class);
			assertTrue(c != null && c.getCityName().equals("Porto Seguro"));
			break;
		}
	}

	@Test
	public void buscarHotel() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(API.VERSAO + "/hotels/{idHotel}","1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("checkin", "20/05/2021")
				.param("checkout", "21/05/2021")
				.param("qtdAdulto", "2")
				.param("qtdCrianca", "1"));


		List<HotelDetail> list = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), List.class);

		for (Object obj : list) {
			HotelDetail c = new Gson().fromJson(new Gson().toJson(obj), HotelDetail.class);
			assertTrue(c != null && c.getId().equals(1l));
			break;
		}

	}

	
}
