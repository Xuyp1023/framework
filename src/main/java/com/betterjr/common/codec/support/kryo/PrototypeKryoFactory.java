package com.betterjr.common.codec.support.kryo;

import com.esotericsoftware.kryo.Kryo;
 
public class PrototypeKryoFactory extends KryoFactory {

    public Kryo getKryo() {
        return createKryo();
    }
}
