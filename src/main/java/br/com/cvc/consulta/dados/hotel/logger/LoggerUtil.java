package br.com.cvc.consulta.dados.hotel.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class LoggerUtil {
    private LoggerUtil() {

    }

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void logger(String app, String step, String message, String description, Object request,
                              Object response) {
        LoggerModelProperties loggerModelProperties = new LoggerModelProperties(app, step, message, description,
                request, response);
        getLogger(loggerModelProperties);
    }

    private static void getLogger(LoggerModelProperties loggerModelProperties) {
        LoggerModel loggerModel = new LoggerModel();
        loggerModel.setHotel(loggerModelProperties);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.convertValue(loggerModel, JsonNode.class);

            String jsonFinal = mapper.writeValueAsString(jsonNode);

            logger.info(jsonFinal);

        } catch (JsonProcessingException e) {
            logger.error("ERROR AO GERAR LOG", e.getCause());
        }
    }

    public static void logger(LoggerModelProperties loggerModelProperties) {
        getLogger(loggerModelProperties);
    }
}
