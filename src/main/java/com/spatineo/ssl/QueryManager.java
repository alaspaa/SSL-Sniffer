package com.spatineo.ssl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {
    public List<ServerCipherAndProtocol> queryWithProtocol(String uri, String protocol, ArrayList<String> ciphers) throws NoSuchAlgorithmException, IOException, KeyManagementException, ConnectException {
        List<ServerCipherAndProtocol> SCPList = new ArrayList<>();
        URL url = new URL(uri);
        System.out.println("\nTrying protocol " + protocol);

        SSLContext sc = SSLContext.getInstance(protocol);
        sc.init(null, null, new java.security.SecureRandom());

        while (ciphers.size() > 0) {
            HttpsURLConnection client = (HttpsURLConnection) url.openConnection();
            CustomSSLSocketFactory socketFactory = new CustomSSLSocketFactory(sc.getSocketFactory());
            socketFactory.setCiphers(ciphers.toArray(new String[0]));

            client.setSSLSocketFactory(socketFactory);

            try {
                String cipher = send(client);
                System.out.println(cipher);
                if (cipher == null || cipher.length() == 0) {
                    System.out.println("No more accepted ciphers.");
                    break;
                }
                ciphers.remove(ciphers.indexOf(cipher));
                SCPList.add(new ServerCipherAndProtocol(uri, protocol, cipher));
            } catch (SSLHandshakeException e) {
                break;
            } catch (ConnectException e) {
                throw e;
            }
        }
        return SCPList;
    }

    private String send(HttpsURLConnection client) throws IOException, ConnectException {
        String cipher = "";
        try {
            if (client != null && client.getResponseCode() == HttpURLConnection.HTTP_OK) {
                cipher = client.getCipherSuite();
            }
        } catch (SSLException e) {
            System.out.println("Protocol or cipher suite not accepted at url: " + client.getURL().toString());
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
        return cipher;
    }
}
