package com.bing.common;

import java.io.InputStream;
import java.net.URL;

import com.bing.chat.api.IResourceLoader;
import com.xiyou.view.MainView;

public class ClassPathResourceLoader implements IResourceLoader {
	@Override
	public URL getResource(String path) {
		return ClassPathResourceLoader.class.getClassLoader().getResource(path);
	}

	public InputStream getResourceAsStream(String path) {
		return ClassPathResourceLoader.class.getClassLoader()
				.getResourceAsStream(path);
	}
}
