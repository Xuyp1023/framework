package com.betterjr.common.codec.support.kryo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
 
import com.esotericsoftware.kryo.Kryo;

public class PooledKryoFactory extends KryoFactory {

    private final Queue<Kryo> pool = new ConcurrentLinkedQueue<Kryo>();

    @Override
    public void returnKryo(Kryo kryo) {
        pool.offer(kryo);
    }

    @Override
    public void close() {
        pool.clear();
    }

    public Kryo getKryo() {
        Kryo kryo = pool.poll();
        if (kryo == null) {
            kryo = createKryo();
        }
        return kryo;
    }
}
