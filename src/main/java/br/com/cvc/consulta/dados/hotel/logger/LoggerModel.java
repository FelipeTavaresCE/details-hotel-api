package br.com.cvc.consulta.dados.hotel.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggerModel implements Serializable {
    private static final long serialVersionUID = 1523265191056550369L;

    private LoggerModelProperties hotel;

}
