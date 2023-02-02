package org.luke.mesa.abs.api.multipart;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.luke.mesa.abs.api.ApiCall;
import org.luke.mesa.abs.utils.ErrorHandler;
import org.luke.mesa.abs.utils.functional.JsonConsumer;
import org.luke.mesa.abs.utils.Platform;


public class MultiPartApiCall extends ApiCall {
	private final Part[] parts;

	public MultiPartApiCall(String path, Part... parts) {
		super(path);
		this.parts = parts;
	}
	
	public void execute(JsonConsumer onResult, String token) throws IOException, ParseException, JSONException {
		HttpPost httpPost = new HttpPost(path);
		httpPost.addHeader("Accept", "application/json");
		
		if(token != null) {
			httpPost.addHeader("token", token);
		}

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.EXTENDED);
		for (Part part : this.parts) {
			builder.addPart(part.getKey(), part.getValue());
		}

		httpPost.setEntity(builder.build());

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
}
