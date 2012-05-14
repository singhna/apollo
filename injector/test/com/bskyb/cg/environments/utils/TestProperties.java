/*
Copyright 2012 BSkyB Ltd.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package com.bskyb.cg.environments.utils;

import junit.framework.TestCase;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations={"classpath:context/propertiesAppCtx.xml"})

public class TestProperties extends TestCase {

	private DynamicProperties dynamicProperties;

	@Autowired
    public void setProperties(DynamicProperties dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }
	
	@Test
    public void testLoadProperties() throws Exception{
    	PropertiesConfiguration dynamicPropertiesConfigurer = dynamicProperties.getConfigure();

    	
    }
    
	
}
