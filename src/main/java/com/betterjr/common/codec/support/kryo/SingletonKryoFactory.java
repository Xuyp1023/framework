package com.betterjr.common.codec.support.kryo;

import com.esotericsoftware.kryo.Kryo;

public class SingletonKryoFactory extends KryoFactory {

    private Kryo instance;

    @Override
    public Kryo getKryo() {
        if (instance == null) {
            instance = createKryo();
        }
        return instance;
    }
}
