package com.nfthub.core.logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class AbstractLog {
    static ObjectMapper mapper = new ObjectMapper();
    private String date;
    private Integer resStatus;
    private String method;
    private String requestUrl;
    private String userIp;
    private String userAgent;

    public String serialize() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}