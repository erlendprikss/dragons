package org.dragon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    private String adId;
    private String message;
    private int reward;
    private int expiresIn;
    private String probability;
}

