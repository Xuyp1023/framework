package com.betterjr.common.mapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustDecimalJsonSerializer extends JsonSerializer<BigDecimal> {
    private static DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void serialize(BigDecimal anValue, JsonGenerator anGen, SerializerProvider anSerializers)
            throws IOException, JsonProcessingException {
        if (anValue == null) {
            anGen.writeString("0.00");
        } else {
            anGen.writeString(df.format(anValue));
        }
    }

    public static String format(Number anNum) {
        if (anNum == null) {
            return "0.00";
        } else {
            return df.format(anNum);
        }
    }
}
