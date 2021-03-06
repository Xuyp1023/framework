package com.betterjr.common.codec.support.fst;

import java.io.InputStream;
import java.io.OutputStream;

import com.betterjr.common.codec.support.SerializableClassRegistry;

import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class FstFactory {
    private static final FstFactory factory = new FstFactory();
    private final FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    public static FstFactory getDefaultFactory() {
        return factory;
    }

    public FstFactory() {
        for (final Class clazz : SerializableClassRegistry.getRegisteredClasses()) {
            conf.registerClass(clazz);
        }
    }

    public FSTObjectOutput getObjectOutput(final OutputStream outputStream) {
        return conf.getObjectOutput(outputStream);
    }

    public FSTObjectInput getObjectInput(final InputStream inputStream) {
        return conf.getObjectInput(inputStream);
    }
}
