package org.luke.mesa.abs.api;

import java.io.IOException;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.json.JSONException;
import org.luke.mesa.abs.utils.functional.JsonConsumer;

public abstract class ApiCall {
	protected static CloseableHttpClient client = HttpClients.createDefault();
	protected String path;

	protected ApiCall(String path) {
		this.path = path;
	}

	public abstract void execute(JsonConsumer onResult, String token) throws IOException, ParseException, JSONException;
}
