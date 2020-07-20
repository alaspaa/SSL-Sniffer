package com.spatineo.ssl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static List<String> getServiceURLs(String resourceName) throws IOException {
        if (resourceName == null || resourceName.trim().equals("")) {
            throw new IllegalArgumentException("Service list resource name should not be empty!");
        }
        List<String> services = new ArrayList<>();
        File serviceList = new File(SSLSniffer.class.getClassLoader().getResource(resourceName).getFile());
        BufferedReader br = new BufferedReader(new FileReader(serviceList));
        String line = br.readLine().trim();
        while (line != null) {
            if (line.trim().startsWith("https://")) {
                services.add(line.trim());
            }
            line = br.readLine();
        }
        return services;
    }

    public static void writeToFile(List<ServerCipherAndProtocol> SCPList, String csvPath, boolean createFile) throws IOException {
        final String headerRow = "URL,Protocol,Cipher_suites\n";
        FileWriter writer = null;
        try {
            if (csvPath == null || csvPath.trim() == "") {
                throw new IOException();
            }
            File file = new File(csvPath);
            if (createFile) {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } else if (!createFile && !file.exists()) {
                System.out.println(SSLSniffer.PROP_CREATE_FILE + " flag set to false or not defined\nprotocols.csv was not created!");
                return;
            }
            writer = new FileWriter(file);
            writer.append(headerRow);

            for (ServerCipherAndProtocol item : SCPList) {

                writer.append(item.toString());
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }
}
