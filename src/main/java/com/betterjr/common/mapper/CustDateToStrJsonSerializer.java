package com.betterjr.common.mapper;

import java.io.IOException;
import java.util.Date;

import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustDateToStrJsonSerializer extends JsonSerializer<Date>{

    @Override
    public void serialize(Date anValue, JsonGenerator anGen, SerializerProvider anSerializers) throws IOException, JsonProcessingException {
        if (anValue == null) {
            anGen.writeString("");
        }
        else {
            anGen.writeString(BetterDateUtils.formatDate(anValue));
        }
        
    }


}
