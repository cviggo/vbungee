package org.cviggo.vbungee.shared.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by viggo on 25-07-2014.
 */
public final class Client {
    public static boolean request(String host, String apiKey, String command, Map<String, String> parameters) {
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            String requestStr = String.format(
                    "http://%s/VBungee?api-key=%s"
                            + "&command=%s",
                    host,
                    apiKey,
                    command
            );

            if (parameters != null) {
                final Set<String> keys = parameters.keySet();
                for (String key : keys) {
                    requestStr += "&" + key + "=" + parameters.get(key);
                }
            }

            HttpGet httpget = new HttpGet(requestStr);
            response = httpClient.execute(httpget);

            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            }

        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}
