package org.cviggo.vbungee.shared.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.cviggo.vbungee.shared.server.ServerInfo;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.simpleframework.http.Query;

import java.io.IOException;
import java.util.*;

/**
 * Created by viggo on 25-07-2014.
 */
public final class Client {

    public static boolean request(ServerInfo serverInfo, String command, Map<String, String> parameters, String callback, Map<String, String> callbackParameters) {
        return request(serverInfo.host + ":" + serverInfo.port, serverInfo.apiKey, command, parameters, callback, callbackParameters);
    }


    public static boolean request(ServerInfo serverInfo, String command, Map<String, String> parameters) {
        return request(serverInfo.host + ":" + serverInfo.port, serverInfo.apiKey, command, parameters, null, null);
    }

    public static boolean request(String hostAndPort, String apiKey, String command, Map<String, String> parameters, String callback, Map<String, String> callbackParameters) {
        CloseableHttpResponse response = null;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            String requestStr = String.format(
                    "http://%s/VBungee?api-key=%s"
                            + "&command=%s",
                    hostAndPort,
                    apiKey,
                    command
            );

            HttpPost httpPost = new HttpPost(requestStr);

            List<NameValuePair> nameValuePairs = new ArrayList<>();

            if (parameters != null) {
                final Set<String> keys = parameters.keySet();
                for (String key : keys) {
                    nameValuePairs.add(new BasicNameValuePair(key, parameters.get(key)));
                }
            }

            if (callback != null){

                nameValuePairs.add(new BasicNameValuePair("callback", callback));

                if (callbackParameters != null) {
                    String callbackParametersData = "{\"callbackParameters:\": " + JSONValue.toJSONString(callbackParameters) + "}";
                    nameValuePairs.add(new BasicNameValuePair("callbackParametersData", callbackParametersData));
                }
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpClient.execute(httpPost);

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

    public static String encodeMap(Map<String, String> map){
        String mapData = "{\"map:\": " + JSONValue.toJSONString(map) + "}";
        return mapData;
    }

    public static Map<String, String> decodeMap(String mapData){
        final JSONObject parsedObject = (JSONObject)JSONValue.parse(mapData);

        if (parsedObject == null){
            return null;
        }

        if (parsedObject.size() < 1){
            return null;
        }

        final HashMap map = (HashMap) parsedObject.values().toArray()[0];

        final Set keySet = map.keySet();

        HashMap<String, String> requestMap = new HashMap<>();

        for (Object keyObj : keySet) {
            requestMap.put(keyObj.toString(), map.get(keyObj).toString());
        }

        return requestMap;
    }

    public static void parseCallbackParameters(Query query, HashMap<String, String> requestMap) {
        final String callbackParametersData = query.get("callbackParametersData");

        if (callbackParametersData == null){
            return;
        }

        final JSONObject parsedObject = (JSONObject)JSONValue.parse(callbackParametersData);

        if (parsedObject == null){
            return;
        }

        if (parsedObject.size() < 1){
            return;
        }

        final HashMap map = (HashMap) parsedObject.values().toArray()[0];

        final Set keySet = map.keySet();

        for (Object keyObj : keySet) {
            requestMap.put(keyObj.toString(), map.get(keyObj).toString());
        }
    }
}
