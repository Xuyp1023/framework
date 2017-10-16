package com.betterjr.common.mapper;

import java.io.IOException;
import java.util.Map;

import com.betterjr.common.utils.BTObjectUtils;
import com.betterjr.common.utils.BetterDateUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustUnzipJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String anValue, JsonGenerator anGen, SerializerProvider anSerializers)
            throws IOException, JsonProcessingException {
        if (anValue == null) {
            anGen.writeString("");
        } else {
            Map<String, Object> map = (Map) BTObjectUtils.fastDeserializeStr(anValue);
            for (Map.Entry<String, Object> ent : map.entrySet()) {
                if (ent.getKey().contains("Date")) {
                    ent.setValue(BetterDateUtils.formatDispDate((String) ent.getValue()));
                }
            }
            anGen.writeObject(map);
        }
    }

}
