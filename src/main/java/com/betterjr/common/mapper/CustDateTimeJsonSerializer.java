package com.betterjr.common.mapper;

import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustDateTimeJsonSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(final String anValue, final JsonGenerator anGen, final SerializerProvider anSerializers)
            throws IOException {
        if (StringUtils.isBlank(anValue)) {
            anGen.writeString(" ");
        } else {
            try {
                anGen.writeString(BetterDateUtils.formatDate(DateUtils.parseDate(anValue, "yyyyMMdd HHmmss"),
                        "yyyy-MM-dd HH:mm:ss"));
            }
            catch (final ParseException e) {
                anGen.writeString("");
            }
        }
    }
}
