/*
 * Copyright 2016 Quatico Solutions Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quatico.base.aem.test.api.values;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;


public class TestFile {
	private static final char ILLEGAL_FILE_SEPARATOR = File.separatorChar == '/' ? '\\' : '/';
	
	private static final String TEST_RESOURCES_DIRECTORY = path("src", "test", "resources");
	private final String testDirectory;
	private       String name;
	private boolean isTemporary = false;
	private Delay lastModified;
	private File  file;
	
	public TestFile() {
		this((String) null);
	}
	
	public TestFile(String... dirPathSegments) {
		this(path(dirPathSegments));
	}
	
	public TestFile(String testDirectory) {
		this.testDirectory = testDirectory;
		this.lastModified = Delay.NOW;
	}
	
	public static String path(String... segments) {
		StringBuilder buf = new StringBuilder();
		for (int count = 0, idx = 0; idx < segments.length; idx++) {
			String cur = segments[idx];
			if (StringUtils.isBlank(cur) && count == 0 || StringUtils.isNotBlank(cur) && buf.length() > 1) {
				buf.append(File.separator);
			}
			if (StringUtils.isNotBlank(cur)) {
				buf.append(cur);
			}
			count++;
		}
		String result = buf.toString();
		if (result.matches("^\\\\[\\w]{1}:")) { // java.io.File cannot handle leading slashes before drive letters on Windows
			result = result.substring(1);
		}
		return result.replace(ILLEGAL_FILE_SEPARATOR, File.separatorChar);
	}
	
	public File build() {
		if (this.isTemporary) {
			try {
				this.file = File.createTempFile(tempFilePrefix(), ".tmp", new File(getPathForTestFiles()));
				this.file.deleteOnExit();
			} catch (IOException ex) {
				return null;
			}
		} else if (this.name != null) {
			this.file = new File(getFileName());
		} else {
			this.file = new File(getPathForTestFiles());
		}
		this.file.setLastModified(this.lastModified.getStamp());
		return this.file;
	}
	
	public String getPath() {
		return build().getAbsolutePath();
	}
	
	public TestFile name(String fileName) {
		this.name = fileName;
		return this;
	}
	
	public String name() {
		return this.name;
	}
	
	public boolean temporary() {
		return this.isTemporary;
	}
	
	public TestFile temporary(boolean isTemporary) {
		this.isTemporary = isTemporary;
		return this;
	}
	
	public Delay lastModified() {
		return this.lastModified;
	}
	
	public TestFile lastModified(Delay time) {
		this.lastModified = time;
		return this;
	}
	
	public boolean delete() {
		return FileUtils.deleteQuietly(this.file);
	}
	
	private String tempFilePrefix() {
		return "temp-test-" + UUID.randomUUID() + ".file";
	}
	
	private String getPathForTestFiles() {
		String currentPath  = new File(".").getAbsolutePath();
		String resourcePath = currentPath.substring(0, currentPath.length() - 1) + getDirectoryName();
		return resourcePath + File.separator;
	}
	
	private String getDirectoryName() {
		return this.testDirectory != null ? path(TEST_RESOURCES_DIRECTORY, this.testDirectory) : TEST_RESOURCES_DIRECTORY;
	}
	
	private String getFileName() {
		return getPathForTestFiles() + this.name;
	}
	
	public enum Delay {
		NOW(0), MIN_AGO(60000);
		
		private final long delay;
		
		Delay(long delay) {
			this.delay = delay;
		}
		
		public long getStamp() {
			return new Date().getTime() - this.delay;
		}
	}
}
