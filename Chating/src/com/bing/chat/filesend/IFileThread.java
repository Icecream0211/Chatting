package com.bing.chat.filesend;

import java.io.File;

public interface IFileThread {
	public long getSendedSize();
	public File getFile();
}
