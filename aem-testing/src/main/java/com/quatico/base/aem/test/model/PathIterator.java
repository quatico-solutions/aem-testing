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
package com.quatico.base.aem.test.model;


import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;


public class PathIterator implements Iterator<String> {
	
	public static final String SEPARATOR = "/";
	
	private final boolean  isAbsolute;
	private final String[] path;
	private final int      length;
	private final int      startPos;
	private       int      current;
	
	public PathIterator(String path) {
		if (StringUtils.isBlank(path)) {
			this.path = null;
			this.length = 0;
			this.current = 0;
			this.startPos = 0;
			this.isAbsolute = false;
		} else {
			this.isAbsolute = path.startsWith(SEPARATOR);
			this.path = path.split(SEPARATOR);
			this.length = this.path.length;
			this.current = this.path.length - 1;
			this.startPos = 0;
		}
	}
	
	public String first() {
		this.current = this.startPos;
		String result = getCurrent(this.current);
		return StringUtils.isBlank(result) ? SEPARATOR : result;
	}
	
	@Override
	public boolean hasNext() {
		return this.current < this.length - 1;
	}
	
	@Override
	public String next() {
		if (hasNext()) {
			return getCurrent(++this.current);
		}
		return null;
	}
	
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public boolean hasPrevious() {
		return this.current > this.startPos;
	}
	
	public String previous() {
		if (hasPrevious()) {
			String result = getCurrent(--this.current);
			return StringUtils.isEmpty(result) && this.isAbsolute ? SEPARATOR : result;
		}
		return null;
	}
	
	private String getCurrent(int pos) {
		StringBuilder result = new StringBuilder();
		for (int idx = 0; idx < pos + 1 && idx < this.length; idx++) {
			if (this.startPos > 0 && idx != 1 || this.startPos == 0 && idx > 0) {
				result.append(SEPARATOR);
			}
			result.append(this.path[idx]);
		}
		return result.toString();
	}
}
