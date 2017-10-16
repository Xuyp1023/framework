package com.ql.util.express;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DefaultContext<K, V> extends HashMap<K, V> implements IExpressContext<K, V> {

    public DefaultContext() {

    }

    public DefaultContext(Map anMap) {

        super(anMap);
    }
}
