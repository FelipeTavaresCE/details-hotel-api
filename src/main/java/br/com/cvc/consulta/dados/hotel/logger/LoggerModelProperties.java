package br.com.cvc.consulta.dados.hotel.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoggerModelProperties implements Serializable {
    private static final long serialVersionUID = -6036958326718162471L;

    private String app;
    private String step;
    private String message;
    private String description;
    private Object request;
    private Object response;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        //Sonar
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        //Sonar
    }

}