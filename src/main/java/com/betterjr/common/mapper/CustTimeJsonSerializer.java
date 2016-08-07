package com.betterjr.common.mapper;

import java.io.IOException;

import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustTimeJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String anValue, JsonGenerator anGen, SerializerProvider anSerializers) throws IOException, JsonProcessingException {
        if (anValue == null) {
            anGen.writeString(" ");
        }
        else {
            anGen.writeString(BetterDateUtils.formatDispTime(anValue));
        }
    }

}
