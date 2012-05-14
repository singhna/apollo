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


package com.bskyb.cg.environments.message;

import junit.framework.TestCase;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bskyb.cg.environments.utils.DynamicProperties;

@RunWith(SpringJUnit4ClassRunner.class)
//specifies the Spring configuration to load for this test fixture
@ContextConfiguration(locations={"classpath:context/messageHandlerAppCtx.xml"})

public class TestMessageHandler extends TestCase {
	@Autowired
	private ApplicationContext applicationContext;

	@Test
    public void testGetMessageFormat() throws Exception{

    	MessageHandler mh = (MessageHandler) applicationContext.getBean("messageHandler");
    	byte[] rawMessage = new String("auditd||2012-03-22T11:15:32.277+00:00||chi-cg-dat-app-256||imklog 3.22.1, log source = /proc/kmsg started.||").getBytes();
		mh.processMessage(rawMessage);
    	
    	
    }
    
	
}
