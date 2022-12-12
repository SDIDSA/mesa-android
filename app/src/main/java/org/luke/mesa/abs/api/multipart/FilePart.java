package org.luke.mesa.abs.api.multipart;

import java.io.File;

import org.apache.hc.client5.http.entity.mime.ContentBody;
import org.apache.hc.client5.http.entity.mime.FileBody;

public class FilePart extends Part {

	private File value;
	
	public FilePart(String key, File value) {
		super(key);
		this.value = value;
	}

	@Override
	public ContentBody getValue() {
		return new FileBody(value);
	}

}
