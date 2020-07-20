package com.spatineo.ssl;

public class ServerCipherAndProtocol {
    private String host;
    private String protocol;
    private String cipher;

    public ServerCipherAndProtocol(String host, String protocol, String cipher) {
        this.host = host;
        this.protocol = protocol;
        this.cipher = cipher;
    }

    @Override
    public String toString() {
        return host + "," + protocol + "," + cipher + "\n";
    }
}
