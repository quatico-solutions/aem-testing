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
package com.quatico.base.aem.test.unit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.quatico.base.aem.test.model.PathIterator;

import org.junit.Test;


public class PathIteratorTest {
	
	@Test
	public void hasNextWithRootPathSegmentReturnsFalse() {
		PathIterator testObj = new PathIterator("/");
		
		assertFalse(testObj.hasNext());
	}
	
	@Test
	public void hasNextWithSinglePathSegmentReturnsFalse() {
		PathIterator testObj = new PathIterator("/path");
		
		assertFalse(testObj.hasNext());
	}
	
	@Test
	public void hasNextWithTwoPathSegmentsReturnsFalse() {
		PathIterator testObj = new PathIterator("/a/path");
		
		assertFalse(testObj.hasNext());
	}
	
	@Test
	public void hasNextWithMultiPathSegmentsStartingFromFirstReturnsTrueEveryTime() {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		testObj.first();
		assertTrue(testObj.hasNext());
		assertTrue(testObj.hasNext());
		assertTrue(testObj.hasNext());
	}
	
	@Test
	public void hasNextWithMultiPathSegmentsStartingFromFirstReturnsTrue() {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		assertFalse(testObj.hasNext());
		assertEquals("/", testObj.first());
		assertTrue(testObj.hasNext());
		assertEquals("/a", testObj.next());
		assertTrue(testObj.hasNext());
	}
	
	@Test
	public void nextAndPreviousWithMultiPathSegmentsMoveCurrentForwardAndBackward() {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		assertEquals("/a/path", testObj.previous());
		assertEquals("/a/path/foo", testObj.next());
		assertEquals("/a/path", testObj.previous());
		assertEquals("/a", testObj.previous());
		assertEquals("/", testObj.previous());
		assertEquals("/", testObj.first());
		assertEquals("/a", testObj.next());
		assertEquals("/a/path", testObj.next());
		assertEquals("/a", testObj.previous());
		assertEquals("/a/path", testObj.next());
		assertEquals("/a/path/foo", testObj.next());
	}
	
	@Test
	public void nextWithMultiPathAndNoElementReturnsNull() throws Exception {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		assertNull(testObj.next());
	}
	
	@Test
	public void nextWithSinglePathAndNoElementReturnsNull() throws Exception {
		PathIterator testObj = new PathIterator("/a");
		
		assertNull(testObj.next());
	}
	
	@Test
	public void nextWithEmptyPathReturnsNull() throws Exception {
		assertNull(new PathIterator("/").next());
	}
	
	@Test
	public void nextWithEmptyStringReturnsNull() throws Exception {
		assertNull(new PathIterator("").next());
	}
	
	@Test
	public void nextWithNullStringReturnsNull() throws Exception {
		assertNull(new PathIterator(null).next());
	}
	
	@Test
	public void nextWithFirstAndMultiPathReturnsAllSegments() throws Exception {
		PathIterator testObj = new PathIterator("/foobar/en/meta/treecrumb/entry1");
		
		assertEquals("/", testObj.first());
		assertEquals("/foobar", testObj.next());
		assertEquals("/foobar/en", testObj.next());
		assertEquals("/foobar/en/meta", testObj.next());
		assertEquals("/foobar/en/meta/treecrumb", testObj.next());
		assertEquals("/foobar/en/meta/treecrumb/entry1", testObj.next());
		assertNull(testObj.next());
		assertNull(testObj.next());
	}
	
	@Test
	public void previousWithMultiPathReturnsAllSegments() throws Exception {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		assertEquals("/a/path", testObj.previous());
		assertEquals("/a", testObj.previous());
		assertEquals("/", testObj.previous());
		assertNull(testObj.previous());
		assertNull(testObj.previous());
		assertNull(testObj.previous());
	}
	
	@Test
	public void previousWithSinglePathReturnsParentBeforeReturnsNull() throws Exception {
		PathIterator testObj = new PathIterator("/a");
		
		assertEquals("/", testObj.previous());
		assertNull(testObj.previous());
	}
	
	@Test
	public void previousWithEmptyPathReturnsNull() throws Exception {
		assertNull(new PathIterator("/").previous());
	}
	
	@Test
	public void previousWithEmptyStringReturnsNull() throws Exception {
		assertNull(new PathIterator("").previous());
	}
	
	@Test
	public void previousWithNullStringReturnsNull() throws Exception {
		assertNull(new PathIterator(null).previous());
	}
	
	@Test
	public void previousWithNoLeadingSlashAndOnePathSegmentReturnsNull() {
		assertNull(new PathIterator("a").previous());
	}
	
	@Test
	public void previousWithNoLeadingSlashAndTwoPathSegmentsReturnsSegments() {
		PathIterator testObj = new PathIterator("a/path");
		
		assertEquals("a", testObj.previous());
	}
	
	@Test
	public void previousWithNoLeadingSlashAndMultiPathSegmentsReturnsAllSegments() {
		PathIterator testObj = new PathIterator("a/path/to/resource");
		
		assertEquals("a/path/to", testObj.previous());
		assertEquals("a/path", testObj.previous());
		assertEquals("a", testObj.previous());
	}
	
	@Test
	public void hasPreviousWithRootPathSegmentReturnsFalse() {
		PathIterator testObj = new PathIterator("/");
		
		assertFalse(testObj.hasPrevious());
	}
	
	@Test
	public void hasPreviousWithSinglePathSegmentReturnsTrue() {
		PathIterator testObj = new PathIterator("/path");
		
		assertTrue(testObj.hasPrevious());
		assertEquals("/", testObj.previous());
	}
	
	@Test
	public void hasPreviousWithTwoPathSegmentsReturnsTrueEverytime() {
		PathIterator testObj = new PathIterator("/a/path");
		
		assertTrue(testObj.hasPrevious());
		assertTrue(testObj.hasPrevious());
		assertTrue(testObj.hasPrevious());
	}
	
	@Test
	public void hasPreviousWithTwoPathSegmentsReturnsTrue() {
		PathIterator testObj = new PathIterator("/a/path");
		
		assertTrue(testObj.hasPrevious());
		assertEquals("/a", testObj.previous());
		assertTrue(testObj.hasPrevious());
	}
	
	@Test
	public void hasPreviousWithNoLeadingSlashAndTwoPathSegmentsReturnsTrue() {
		PathIterator testObj = new PathIterator("a/path");
		
		assertTrue(testObj.hasPrevious());
		assertEquals("a", testObj.previous());
		assertFalse(testObj.hasPrevious());
	}
	
	@Test
	public void firstWithRootPathSegmentReturnsSame() {
		PathIterator testObj = new PathIterator("/");
		
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
	}
	
	@Test
	public void firstWithSinglePathSegmentReturnsSame() {
		PathIterator testObj = new PathIterator("/path");
		
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
	}
	
	@Test
	public void firstWithTwoPathSegmentsReturnsFirstSegment() {
		PathIterator testObj = new PathIterator("/a/path");
		
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
		assertEquals("/", testObj.first());
	}
	
	@Test
	public void firstWithMultiPathSegmentsMovesCurrentToFirst() {
		PathIterator testObj = new PathIterator("/a/path/foo");
		
		assertEquals("/", testObj.first());
		assertEquals("/a", testObj.next());
		assertEquals("/a/path", testObj.next());
		assertEquals("/", testObj.first());
		assertEquals("/a", testObj.next());
		assertEquals("/a/path", testObj.next());
		assertEquals("/a/path/foo", testObj.next());
	}
}
