package com.spatineo.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomSSLSocketFactory extends SSLSocketFactory {
    final SSLSocketFactory wrappedSSLSocketFactory;
    String[] ciphers;

    CustomSSLSocketFactory(SSLSocketFactory wrappedSSLSocketFactory) {
        this.wrappedSSLSocketFactory = wrappedSSLSocketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return getMyCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return getMyCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        final SSLSocket sslSocket = (SSLSocket) wrappedSSLSocketFactory.createSocket(socket, host, port, autoClose);
        sslSocket.setEnabledCipherSuites(getMyCipherSuites());

        return sslSocket;
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return null;
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return null;
    }

    public void setCiphers(String[] ciphers) {
        this.ciphers = ciphers;
    }

    private String[] getMyCipherSuites() {
        return ciphers;
    }
}
