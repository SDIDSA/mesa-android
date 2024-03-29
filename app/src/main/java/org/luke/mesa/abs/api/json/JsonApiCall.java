package org.luke.mesa.abs.api.json;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.ApiCall;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.functional.JsonConsumer;
import org.luke.mesa.abs.utils.Platform;


public class JsonApiCall extends ApiCall {
	private final Param[] params;

	public JsonApiCall(String path, Param... params) {
		super(path);
		this.params = params;
	}

	public void execute(JsonConsumer onResult, String token) throws IOException, ParseException, JSONException {
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader("Accept", "application/json");

		if (token != null) {
			httpPost.addHeader("token", token);
		}

		JSONObject paramsToSend = new JSONObject();
		for (Param param : this.params) {
			try {
				paramsToSend.put(param.getKey(), param.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		StringEntity requestEntity = new StringEntity(paramsToSend.toString(), ContentType.APPLICATION_JSON);

		httpPost.setEntity(requestEntity);

		CloseableHttpResponse response = client.execute(httpPost);

		JSONObject res = new JSONObject(EntityUtils.toString(response.getEntity()));

		Platform.runLater(() -> {
			try {
				onResult.accept(res);
			} catch (Exception x) {
				ErrorHandler.handle(x, "handle response for API call to " + path);
			}
		});
	}

	@Override
	public String toString() {
		return "JsonApiCall{" +
				"path='" + path + '\'' +
				", params=" + Arrays.toString(params) +
				'}';
	}
}
