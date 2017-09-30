package com.betterjr.common.mapper;

import java.io.IOException;
import java.text.ParseException;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustDateTimeJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(final String anValue, final JsonGenerator anGen, final SerializerProvider anSerializers) throws IOException {
        if (BetterStringUtils.isBlank(anValue)) {
            anGen.writeString(" ");
        }
        else {
            try {
                anGen.writeString(BetterDateUtils.formatDate(BetterDateUtils.parseDate(anValue, "yyyyMMdd HHmmss"), "yyyy-MM-dd HH:mm:ss"));
            }
            catch (final ParseException e) {
                anGen.writeString("");
            }
        }
    }
}
