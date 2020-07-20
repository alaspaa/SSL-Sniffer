package com.spatineo.ssl.test;

import com.spatineo.ssl.FileManager;
import com.spatineo.ssl.ServerCipherAndProtocol;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManagerTest {

    private static final String HOST = "TEST";
    private static final String SEPARATOR_COMMA = ",";
    private static final String FILE_NAME_NOT_CREATED = "shoulNotExist.csv";
    private static final String FILE_NAME_CREATED = "TestWriteFile.csv";
    private static final String FILE_NAME_TEST_SERVICE_LIST = "TestServiceList.txt";
    private static final String RESOURCE_PATH = System.getProperty("user.dir") + "/src/test/resources/";

    private static final String FILE_PATH_CREATED = RESOURCE_PATH + FILE_NAME_CREATED;
    private static final String FILE_PATH_NOT_CREATED = RESOURCE_PATH + FILE_NAME_NOT_CREATED;
    private static final String EXPECTED_GOOGLE = "https://www.google.com";
    private static final String EXPECTED_YAHOO = "https://www.yahoo.com";
    private static final String EXPECTED_EXCEPTION_MESSAGE = "Service list resource name should not be empty!";
    private static final String HEADER_ROW_URL = "URL";
    private static final String HEADER_ROW_PROTOCOL = "Protocol";
    private static final String HEADER_ROW_CIPHER = "Cipher_suites";

    private static String DATE;
    private static String TIME;
    private static List<ServerCipherAndProtocol> CIPHER_LIST;

    @BeforeClass
    public static void setup() {
        Timestamp ts = new Timestamp(new Date().getTime());
        String[] splitDateTime = ts.toString().split(" ");
        DATE = splitDateTime[0];
        TIME = splitDateTime[1];

        ServerCipherAndProtocol item = new ServerCipherAndProtocol(HOST, DATE, TIME);
        CIPHER_LIST = new ArrayList<>();
        CIPHER_LIST.add(item);
    }

    @AfterClass
    public static void cleanup() {
        File file1 = new File(FILE_PATH_CREATED);
        File file2 = new File(FILE_NAME_NOT_CREATED);

        if (file1.exists()) {
            Assert.assertTrue(file1.delete());
        }
        if (file2.exists()) {
            Assert.assertTrue(file2.delete());
        }
    }

    @Test
    public void readSeviceList() throws IOException {
        List<String> serviceList = FileManager.getServiceURLs(FILE_NAME_TEST_SERVICE_LIST);
        Assert.assertTrue(serviceList.size() > 0);
        Assert.assertEquals(2, serviceList.size());
        Assert.assertTrue(serviceList.contains(EXPECTED_GOOGLE));
        Assert.assertTrue(serviceList.contains(EXPECTED_YAHOO));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullResourceName() throws IOException {
        try {
            FileManager.getServiceURLs(null);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(EXPECTED_EXCEPTION_MESSAGE, e.getMessage());
            throw e;
        }
    }

    @Test
    public void writeFileFalseTest() throws IOException {
        FileManager.writeToFile(CIPHER_LIST, FILE_PATH_NOT_CREATED, false);
        File fileShouldNotExist = new File(FILE_PATH_NOT_CREATED);
        Assert.assertFalse(fileShouldNotExist.exists());
    }

    @Test
    public void writeFileTrueTest() throws IOException {
        FileManager.writeToFile(CIPHER_LIST, FILE_PATH_CREATED, true);
        File createdFile = new File(FILE_PATH_CREATED);
        Assert.assertTrue(createdFile.exists());

        List<String[]> lines = new ArrayList<>();
        String line;

        BufferedReader br = new BufferedReader(new FileReader(createdFile));
        while ((line = br.readLine()) != null) {
            lines.add(line.trim().split(SEPARATOR_COMMA));
        }
        Assert.assertEquals(2, lines.size());
        Assert.assertEquals(HEADER_ROW_URL, lines.get(0)[0]);
        Assert.assertEquals(HEADER_ROW_PROTOCOL, lines.get(0)[1]);
        Assert.assertEquals(HEADER_ROW_CIPHER, lines.get(0)[2]);
        Assert.assertEquals(HOST, lines.get(1)[0]);
        Assert.assertEquals(DATE, lines.get(1)[1]);
        Assert.assertEquals(TIME, lines.get(1)[2]);
    }

    @Test(expected = IOException.class)
    public void nullFilePath() throws IOException {
        FileManager.writeToFile(CIPHER_LIST, null, true);
    }
}
