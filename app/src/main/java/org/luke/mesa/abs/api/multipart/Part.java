package org.luke.mesa.abs.api.multipart;

import org.apache.hc.client5.http.entity.mime.ContentBody;

public abstract class Part {
	private String key;
	
	protected Part(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public abstract ContentBody getValue();
}
