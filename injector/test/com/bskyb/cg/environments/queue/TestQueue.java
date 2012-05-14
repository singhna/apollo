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


package com.bskyb.cg.environments.queue;

import org.junit.Test;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StreamCorruptedException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;


import com.bskyb.cg.environments.message.LogMessage;
import com.bskyb.cg.environments.message.Message;
import com.bskyb.cg.environments.utils.FileExtFilter;


public class TestQueue extends TestCase {

	private static Queue resultQueue;
	private static Queue messageQueue;
	
	private final static String testMessageDir = "resources/testmessages";
	private final static String testMessageStore = "out/testmessage/store";
	private final static String resultMessageStore = "out/testresult/store";
	private final static String testLog = "(root) CMD (   cd / && run-parts --report /etc/cron.hourly";
	
	public final static int MAX_MESSAGE_SIZE =  64*1024;
	
	@Test
	public void testRemove() {

		try {
			resultQueue.remove("key6");
			assertEquals(resultQueue.size(),messageQueue.size() - 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAdd() {
		Message logMessage = null;
		try {
			logMessage = new LogMessage("key6", testLog.getBytes("UTF-8"));
			resultQueue.add("key6", logMessage);
			int size=resultQueue.size();
			

			String lastMessage = new String(resultQueue.remove("key6").getMessage());
			
			assertEquals(lastMessage,testLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	@Test
	public void testLookup() {
		Message logMessage = null;
		Message testMessage = null;
		
		try {
			logMessage = resultQueue.lookup("key6");
			String resultMessage = new String(logMessage.getMessage());
			testMessage = messageQueue.lookup("key6");
			
			String testLogMessage = new String(testMessage.getMessage());
			assertEquals(resultMessage, testLogMessage);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public  void setUp() throws Exception {

    	FileInputStream fis;
    	BufferedInputStream bis;
        Message messageEntry;
        
        resultQueue = new Queue(resultMessageStore);
        messageQueue = new Queue(testMessageStore);
    	File emptyDir = new File(testMessageDir);
    	FilenameFilter onlyTxt = new FileExtFilter("txt");
    	File [] files = emptyDir.listFiles(onlyTxt);


    	for (File file : files) {

    		fis = new FileInputStream(file);
    		bis = new BufferedInputStream(fis);
    		byte[]b = new byte[MAX_MESSAGE_SIZE];
            try {
            	while (bis.read(b) != -1) {
                    messageEntry = new LogMessage(file.getName(), b);
                    resultQueue.add(file.getName(), messageEntry);
                    messageQueue.add(file.getName(), messageEntry);
            	}
 
            } catch (ClassCastException e) {
                throw new IOException(e.toString()); 
            } catch (StreamCorruptedException e) {
                throw new IOException(e.toString());
            }
        	fis.close();
    	}

	}
	
	@After
	public void tearDown() throws Exception {
		resultQueue.clear();
		messageQueue.clear();

	}

}
