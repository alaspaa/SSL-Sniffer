package com.spatineo.ssl.test;

import com.spatineo.ssl.QueryManager;
import com.spatineo.ssl.ServerCipherAndProtocol;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest(QueryManager.class)
@PowerMockIgnore("javax.net.ssl.*")
public class QueryManagerTest {
    private static final String CIPHER_1 = "TLS_RSA_WITH_AES_256_CBC_SHA";
    private static final String TEST_URL = "https://127.0.0.1";
    private static final String TEST_PROTOCOL = "TLSv1.2";
    private static final String MOCK_METHOD_SEND = "send";

    private QueryManager MANAGER;
    private ArrayList<String> SINGLE_CIPHER;

    @Before
    public void setup() {
        MANAGER = PowerMockito.spy(new QueryManager());
        SINGLE_CIPHER = new ArrayList<>(Arrays.asList(CIPHER_1));
    }

    @Test
    public void queryTest() throws Exception {
        String expectedResult = TEST_URL + "," + TEST_PROTOCOL + "," + CIPHER_1 + "\n";
        PowerMockito.doReturn(CIPHER_1).when(MANAGER, MOCK_METHOD_SEND, any(HttpsURLConnection.class));

        List<ServerCipherAndProtocol> returnList = MANAGER.queryWithProtocol(TEST_URL, TEST_PROTOCOL, SINGLE_CIPHER);
        Assert.assertEquals(1, returnList.size());
        Assert.assertEquals(expectedResult, returnList.get(0).toString());
    }

    @Test
    public void queryReturnNullCipherTest() throws Exception {
        PowerMockito.doReturn(null).when(MANAGER, MOCK_METHOD_SEND, any(HttpsURLConnection.class));

        List<ServerCipherAndProtocol> returnList = MANAGER.queryWithProtocol(TEST_URL, TEST_PROTOCOL, SINGLE_CIPHER);
        Assert.assertTrue(returnList.isEmpty());
    }

}
