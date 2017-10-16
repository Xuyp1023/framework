package com.betterjr.common.security;

import java.net.Socket;
import java.util.Map;

public interface PrivateKeyStrategy {

    String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket);

}
