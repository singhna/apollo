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


import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.bskyb.cg.environments.cassandra.CassandraConfigurationBuilder;
import com.bskyb.cg.environments.data.MessageDao;
import com.bskyb.cg.environments.data.MessageDaoFactory;
import com.bskyb.cg.environments.queue.Queue;


@ContextConfiguration(locations={"classpath:context/messageHandlerAppCtx.xml"})

public class MessageHandler {
	
	private static Logger logger = Logger.getLogger(MessageHandler.class);
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CassandraConfigurationBuilder cassandraConfigurationBuilder ;

	@Autowired
	private Queue queue;

	
	public CassandraConfigurationBuilder getCassandraConfigurationBuilder() {
		return cassandraConfigurationBuilder;
	}


	public void setCassandraConfigurationBuilder(
			CassandraConfigurationBuilder cassandraConfigurationBuilder) {
		this.cassandraConfigurationBuilder = cassandraConfigurationBuilder;
	}

		
	private Map<String, String> getFields(String key) {
		Map <String, String> map = new HashMap<String, String>();
		StringTokenizer tk = new StringTokenizer(key,"_");
		String datestring = (String) tk.nextElement();
		map.put("date", datestring);
		String hostname = (String) tk.nextElement();
		map.put("hostname", hostname);
		String msgtype = (String) tk.nextElement();
		map.put("msgtype", msgtype);
		String uuid = (String) tk.nextElement();
		map.put("uuid", uuid);
		String epochtime = (String) tk.nextElement();
		map.put("epochtime", epochtime);

		return map;

	}


	public void processMessage(byte[] rawMessage) {

		try {


			String textMessage = new String(rawMessage);
			
			logger.info("Raw message :" +textMessage);

			String msgtype = BaseMessageFormat.getMessageType(textMessage,"||");

	    	MessageFormatFactory messageFormatFactory = (MessageFormatFactory) applicationContext.getBean("messageFormatFactory");

	    	MessageFormat logMessageFormat = messageFormatFactory.getMessageFormat(msgtype);

			Message logMessage = logMessageFormat.parse(rawMessage);

			String key = logMessage.getKey();

			Map <String, String> keyfields = getFields(key);

			String uuid = keyfields.get("uuid");
			String date = keyfields.get("date");       		
			String hostname = keyfields.get("hostname");
			String epochtime = keyfields.get("epochtime");
			
			
			logger.info("UUID :" + uuid);
			logger.info("date :" + date);
			logger.info("hostname :" + hostname);
			logger.info("epochtime :" + epochtime);
			
			UUID cassandrauuid = UUID.fromString(uuid);
			String syslogMsg = new String(logMessage.getMessage());
			String rowKey = hostname + ":" + date;

			queue.add(key,logMessage);
			cassandraConfigurationBuilder.setMsgType(msgtype);
			MessageDaoFactory  messageDaoFactory = new MessageDaoFactory(cassandraConfigurationBuilder);
			
			MessageDao messageDao = messageDaoFactory.getObject();
			messageDao.insert(cassandrauuid, epochtime, msgtype, syslogMsg, rowKey, hostname);
			queue.remove(key);
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	public Queue getQueue() {
		return queue;
	}


	public void setQueue(Queue queue) {
		this.queue = queue;
	}



}
