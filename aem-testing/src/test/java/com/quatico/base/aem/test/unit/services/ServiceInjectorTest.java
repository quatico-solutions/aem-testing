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
package com.quatico.base.aem.test.unit.services;


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.quatico.base.aem.test.api.services.IServiceBuilder.InjectedService;
import com.quatico.base.aem.test.api.services.ServiceInjector;

import org.junit.Test;


public class ServiceInjectorTest {
	@Test
	public void createWithObjectOfSameClassInjectsField() throws Exception {
		Zip target   = new Zip();
		Zap expected = new Zap();
		
		Zip actual = new ServiceInjector<Zip>(new InjectedService<>(Zap.class, expected)).create(target);
		
		assertSame(expected, actual.getFirst());
	}
	
	@Test
	public void createWithObjectOfSameClassInjectsAllFieldsOfThatType() throws Exception {
		Zip target   = new Zip();
		Zap expected = new Zap();
		
		Zip actual = new ServiceInjector<Zip>(new InjectedService<>(Zap.class, expected)).create(target);
		
		assertSame(expected, actual.getSecond());
	}
	
	@Test
	public void createWithObjectOfSubClassDoesNotInjectField() throws Exception {
		Zip target   = new Zip();
		
		Zip actual = new ServiceInjector<Zip>(new InjectedService<>(Zup.class, new Zup())).create(target);
		
		assertNull(actual.getFirst());
	}
	
	@Test
	public void createWithObjectOfSuperClassDoesNotInjectField() throws Exception {
		Zap target = new Zap();
		
		Zap actual = new ServiceInjector<Zap>(new InjectedService<>(Zap.class, new Zap())).create(target);
		
		assertNull(actual.getThird());
	}
	
	private static class Zip {
		private Zap first;
		private Zap second;
		
		public Zap getFirst() {
			return first;
		}
		
		public Zap getSecond() {
			return second;
		}
	}
	
	
	private static class Zap {
		private Zup third;
		
		public Zup getThird() {
			return third;
		}
	}
	
	
	private static class Zup extends Zap {}
}
