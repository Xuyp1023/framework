package com.betterjr.modules.sys.jwt;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class JWTAudienceException extends JWTVerifyException {

    private static final long serialVersionUID = 9023354240755174813L;
    private JsonNode audienceNode;

    public JWTAudienceException(JsonNode audienceNode) {
        this.audienceNode = audienceNode;
    }

    public JWTAudienceException(String message, JsonNode audienceNode) {
        super(message);
        this.audienceNode = audienceNode;
    }

    public List<String> getAudience() {
        ArrayList<String> audience = new ArrayList<String>();
        if (audienceNode.isArray()) {
            for (JsonNode jsonNode : audienceNode) {
                audience.add(jsonNode.textValue());
            }
        } else if (audienceNode.isTextual()) {
            audience.add(audienceNode.textValue());
        }
        return audience;
    }
}
