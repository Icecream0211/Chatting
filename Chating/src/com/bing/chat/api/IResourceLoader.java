package com.bing.chat.api;

import java.io.InputStream;

public interface IResourceLoader {
	public Object getResource(String path);
	public InputStream getResourceAsStream(String path);
}
