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


package com.bskyb.cg.environments.cassandra;

import java.util.UUID;

import org.apache.log4j.Logger;

import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import com.bskyb.cg.environments.cassandra.CassandraConfiguration;
import com.bskyb.cg.environments.data.MessageDao;
import com.bskyb.cg.environments.exception.CassandraException;
import com.bskyb.cg.environments.exception.MessageDaoException;
import com.bskyb.cg.environments.exception.NotFoundException;
import com.bskyb.cg.environments.message.Message;

public class CassandraDao implements MessageDao {
	private static Logger logger = Logger.getLogger(MessageDao.class);
	private final CassandraConfiguration configuration;

	public CassandraDao(CassandraConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public CassandraConfiguration getConfiguration() {
		return configuration;
	}
	
	public void insert(UUID cassandrauuid, String epochtime, String msgtype, String syslogMsg , String rowkey, String hostname) throws CassandraException {

		logger.info("inserting in  cassandradao UUID :" + cassandrauuid);
		Composite composite = new Composite();

		composite.addComponent(msgtype, StringSerializer.get());
		composite.addComponent(cassandrauuid, UUIDSerializer.get());
//		HColumn<Composite,String> col =  HFactory.createColumn(composite, syslogMsg, new CompositeSerializer(), StringSerializer.get());
		HColumn<UUID, String> col =  HFactory.createColumn(cassandrauuid, syslogMsg, UUIDSerializer.get(), StringSerializer.get());
		HCounterColumn<String> colCounter = HFactory.createCounterColumn(epochtime, 0L);
		Mutator<String> mutator = HFactory.createMutator(configuration.getKeyspace(), StringSerializer.get());
		logger.info("row key :" + rowkey);
		logger.info("msg type :" + msgtype);

		mutator.insert(rowkey, msgtype , col);
		mutator.incrementCounter(rowkey, msgtype+"Counter", epochtime, 1L);
		logger.info("inserting in  cassandradao UUID4 :" + cassandrauuid);

	}

	public void insertComposite(UUID cassandrauuid, String epochtime, String msgtype, String syslogMsg , String rowkey, String hostname) throws CassandraException {

	
		Composite composite = new Composite();

		composite.addComponent(msgtype, StringSerializer.get());
		composite.addComponent(cassandrauuid, UUIDSerializer.get());
		HColumn<Composite,String> col =  HFactory.createColumn(composite, syslogMsg, new CompositeSerializer(), StringSerializer.get());
		
		Mutator<String> mutator = HFactory.createMutator(configuration.getKeyspace(), StringSerializer.get());

		hostname = hostname.replaceAll("-", "");
		
		mutator.insert(rowkey,hostname , col);

	}

	@Override
	public void createLogMessage(Message message) throws MessageDaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message retrieveLogMessage(String id) throws MessageDaoException,
			NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLogMessage(Message message) throws MessageDaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteLogMessage(String id) throws MessageDaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteExpiredLogMessages(int timeToGoStaleMins,
			int timeToLiveMins) {
		// TODO Auto-generated method stub
		
	}

}
