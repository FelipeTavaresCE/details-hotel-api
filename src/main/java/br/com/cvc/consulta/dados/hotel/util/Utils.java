package br.com.cvc.consulta.dados.hotel.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.cvc.consulta.dados.hotel.logger.LoggerStepEnum;
import br.com.cvc.consulta.dados.hotel.logger.LoggerUtil;
import br.com.cvc.consulta.dados.hotel.model.constantes.API;

public class Utils {
	
	public static int diferencaEmDias(String dataInicio, String dataFim) {
		int dias = -1;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar data1 = Calendar.getInstance();
			Calendar data2 = Calendar.getInstance();
			try {
				data1.setTime(sdf.parse(dataInicio));
				data2.setTime(sdf.parse(dataFim));
			} catch (java.text.ParseException e ) {}
			dias = data2.get(Calendar.DAY_OF_YEAR) -
					data1.get(Calendar.DAY_OF_YEAR);
		}catch (Exception e) {
			LoggerUtil.logger(API.DEATAILS_HOTEL, LoggerStepEnum.HTE0010.getStep(), LoggerStepEnum.HTE0010.getMessage(), "",
					"", null);
			e.printStackTrace();
		}
		return dias;
	}
	

}
