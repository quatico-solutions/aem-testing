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
package com.quatico.base.aem.test.common;


import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT;

import com.quatico.base.aem.test.TestDriver;
import com.quatico.base.aem.test.api.setup.Assets;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.Before;
import org.junit.Test;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;

import io.wcm.testing.mock.aem.builder.ContentBuilder;


public abstract class AssetTestDriver extends TestDriver {
	
	protected Assets testObj;
	
	@Before
	public void setUp() throws Exception {
		this.testObj = new Assets(client);
	}
	
	@Test
	public void getPathWithExistingResourceReturnsPath() throws Exception {
		Asset testObj = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		assertEquals("/libs/quatico/base/templates/backend/thumbnail.png", testObj.getPath());
	}
	
	@Test
	public void getNameReturnsResourceName() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		String actual = target.getName();
		
		assertEquals(target.getName(), actual);
	}
	
	@Test
	public void getMetadataValueReturnsMetadataValue() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png", "dam:Fileformat", "PNG");
		
		String actual = target.getMetadataValue("dam:Fileformat");
		
		assertEquals("PNG", actual);
	}
	
	@Test
	public void getMetadataStringReturnsMetadataValueAsString() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png", "dam:Fileformat", "PNG");
		
		Object actual = target.getMetadata("dam:Fileformat");
		
		assertEquals("PNG", actual);
	}
	
	@Test
	public void getLastModifiedWithExistingResourceIsBeforeNow() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		long actual = target.getLastModified();
		
		assertTrue(new Date(actual).before(new Date()));
	}
	
	@Test
	public void getRenditionStringWithExistingNameReturnsRendition() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		target.addRendition("cq5dam.thumbnail.48.48.png", ContentBuilder.createDummyImage(1, 1, EMPTY), EMPTY);
		
		Rendition actual = target.getRendition("cq5dam.thumbnail.48.48.png");
		
		assertEquals("cq5dam.thumbnail.48.48.png", actual.getName());
	}
	
	@Test
	public void getOriginalWithExistingRenditionReturnsOriginal() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		Rendition actual = target.getOriginal();
		
		assertEquals("original", actual.getName());
	}
	
	@Test
	public void getMetadata() throws Exception {
		Asset               target   = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		Map<String, Object> expected = target.adaptTo(Resource.class).getChild(JCR_CONTENT + "/metadata").adaptTo(ValueMap.class);
		
		Map<String, Object> actual = target.getMetadata();
		
		assertEquals(expected.size(), actual.size());
	}
	
	@Test
	public void getRenditionsWithExistingRenditionsIsNotEmpty() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		List<Rendition> actual = target.getRenditions();
		
		assertFalse(actual.isEmpty());
	}
	
	@Test
	public void listRenditionsWithExistingRendtionsIsNotEmpty() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.png");
		
		Iterator<Rendition> actual = target.listRenditions();
		
		assertTrue(actual.hasNext());
	}
	
	@Test
	public void getMimeTypeWithNoMimetypeReturnsDefaultMimetype() throws Exception {
		Asset target = anAsset("/libs/quatico/base/templates/backend/thumbnail.jpg");
		
		String actual = target.getMimeType();
		
		assertEquals("image/jpeg", actual);
	}
	
	@Test(expected = NullPointerException.class)
	public void assetWithNullPathThrowsNullPointerException() throws Exception {
		testObj.anAsset().path(null).build();
	}
	
	@Test(expected = RuntimeException.class)
	public void assetWithEmptyPathThrowsRunTimeException() throws Exception {
		testObj.anAsset().path(StringUtils.EMPTY).build();
	}
	
	@Test
	public void assetWithValidImageFileBuildsValidAsset() throws Exception {
		Asset target = testObj.anAsset().path("/path/to/asset").classpathResource("/sample-image.png").build();
		
		assertEquals("/path/to/asset", target.getPath());
		assertEquals("asset", target.getName());
		assertEquals(0, target.getLastModified());
		assertEquals("image/jpeg", target.getMimeType());
		assertEquals("/path/to/asset/jcr:content/renditions/original", target.getRenditions().get(0).getPath());
		assertEquals(9822, target.getRenditions().get(0).getSize());
	}
	
	@Test
	public void assetWithValidInputStreamBuildsValidAsset() throws Exception {
		InputStream is = getClass().getResourceAsStream("/sample-image.png");
		
		Asset target = testObj.anAsset().path("/path/to/asset").inputStream(is).build();
		
		assertEquals("/path/to/asset", target.getPath());
		assertEquals("asset", target.getName());
		assertEquals(0, target.getLastModified());
		assertEquals("image/jpeg", target.getMimeType());
		assertEquals("/path/to/asset/jcr:content/renditions/original", target.getRenditions().get(0).getPath());
		assertEquals(9822, target.getRenditions().get(0).getSize());
	}
	
	@Test
	public void assetWithRenditionAndExistingNameYieldsRendition() throws Exception {
		Asset target = testObj.anAsset().path("/libs/quatico/base/templates/backend/thumbnail.png").rendition(
			testObj.anAssetRendition().name("cq5dam.thumbnail.48.48.png").inputStream(ContentBuilder.createDummyImage(1, 1, EMPTY))
		).build();
		
		Rendition actual = target.getRendition("cq5dam.thumbnail.48.48.png");
		
		assertEquals("cq5dam.thumbnail.48.48.png", actual.getName());
	}
	
	private Asset anAsset(String path, Object... properties) throws Exception {
		return testObj.anAsset(path, properties).adaptTo(Asset.class);
	}
}
