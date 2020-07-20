package com.spatineo.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SSLSniffer {
    static List<ServerCipherAndProtocol> SCPList = new ArrayList<>();
    static String[] PROTOCOLS = {"TLSv1", "TLSv1.1", "TLSv1.2", "TLSv1.3", "SSLv3"};
    static final String PROP_CREATE_FILE = "create.cipher.list";
    static final String uriResourcePath =  "service-list";
    static final String pathToResultsFile = System.getProperty("user.dir") + "/protocols.csv";

    public static void main(String[] args) {
        try {
            boolean createFile = false;
            String propCreate = System.getProperty(PROP_CREATE_FILE);
            if(propCreate != null) {
                createFile = propCreate.toLowerCase().trim().equals("true");
            }

            List<String> services = FileManager.getServiceURLs(uriResourcePath);
            QueryManager manager = new QueryManager();
            for(String service : services) {
                System.out.println("Querying: " + service);
                try {
                    for (String protocol : PROTOCOLS) {
                        SCPList.addAll(manager.queryWithProtocol(service, protocol, getCiphers()));
                    }
                } catch (ConnectException e) {
                    System.out.println("Connection refused at " + service + "\n");
                }
            }
            FileManager.writeToFile(SCPList, pathToResultsFile, createFile);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getCiphers() {
        try {
            SSLContext context = SSLContext.getDefault();
            SSLSocketFactory sf = context.getSocketFactory();
            return new ArrayList(Arrays.asList(sf.getSupportedCipherSuites()));
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}

