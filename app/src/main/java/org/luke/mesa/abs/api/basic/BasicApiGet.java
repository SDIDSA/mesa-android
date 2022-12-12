package org.luke.mesa.abs.api.basic;

import android.util.Log;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.ApiCall;
import org.luke.mesa.abs.api.json.Param;
import org.luke.mesa.abs.utils.Platform;
import org.luke.mesa.abs.utils.functional.JsonConsumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class BasicApiGet extends ApiCall {
    private final Param[] params;

    public BasicApiGet(String path, Param... params) {
        super(path);
        this.params = params;
    }

    public void execute(JsonConsumer onResult, String token) throws IOException, ParseException, JSONException {
        try {
            String uri = new URIBuilder().setPath(path).addParameters(Arrays.stream(params).map(param -> new BasicNameValuePair(param.getKey(), param.getValue())).collect(Collectors.toList())).build().toString().substring(1);
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader("Accept", "application/json");

            Log.i("uri", uri);
            CloseableHttpResponse response = client.execute(httpGet);

            JSONObject res = new JSONObject();
            res.put("body", EntityUtils.toString(response.getEntity()));

            Platform.runLater(() -> onResult.accept(res));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
