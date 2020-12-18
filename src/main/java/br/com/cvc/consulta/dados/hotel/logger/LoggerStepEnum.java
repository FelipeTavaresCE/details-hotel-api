package br.com.cvc.consulta.dados.hotel.logger;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoggerStepEnum {
    HT00001("HT00001", "buscar hotel por id"),
    HT00002("HT00002", "hotel consultado"),
    HT00003("HT00003", "buscar hotel por id cidade"),
    HT00004("HT00004", "hoteis consultados"),

    //Erros
    HTE0003("HTE0003", "Erro na validação dos dados do objeto"),
    HTE0004("HTE0004", "Erro"),

    HTE0005("HTE0005","Erro na integração com o Parceiros API"),


    HTE0006("HTE0006", "Parâmetro não informado"),
    HTE0007("HTE0007", "Erro na validação"),
    HTE0008("HTE0008", "Constraint"),
    HTE0009("HTE0009", "Erro na conversão"),
    HTE0010("HTE0010", "Erro ao obter a diferenca entre as datas"),
    HTE0011("HTE0011", "Erro na integração com a APi de parceiros")

    ;
    private String step;
    private String message;

    public String getStep() {
        return step;
    }

    public String getMessage() {
        return message;
    }

}
