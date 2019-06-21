package com.bing.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import com.bing.chat.api.IResourceLoader;

public class FileSystemResourceLoader implements IResourceLoader {
	@Override
	public File getResource(String path) {
		File f = new File(path);
		return f.exists() ? f : null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		File f = new File(path);
		if (f.exists()) {
			try {
				return new FileInputStream(f);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}
}
