package br.com.cvc.consulta.dados.hotel.exception.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ApiVendedorLoteError implements ApiSubError {

    private Integer linha;
    private List<String> erros = new ArrayList<>();
}
