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

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.junit.Test;



import java.util.Date;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;



import com.bskyb.cg.environments.exception.CassandraException;

public class TestCassandraDao extends TestCase {

	private static CassandraDao cassandraDao;
	private static CassandraConfiguration cassandraConfiguration;
	
	private final static String testLog = "(root) CMD (   cd / && run-parts --report /etc/cron.hourly";
	private final static String  clusterName = "Test Cluster";
	private final static String exhaustPolicy = "";
	private final static String hosts = "10.20.191.1:9160,10.20.191.2:9160,10.20.191.3:9160,10.20.191.4:9160,10.20.191.5:9160";
	private final static String keySpaceName = "auditKS";
	private final static String lifo = "";
	private final static String maxActive = "";
	private final static String maxIdle = "";
	private final static String maxWaitTimeWhenExhausted = "";
	private final static String readConsistencyLevel = "";
	private final static String rowCount = "";
	private final static String thriftSocketTimeout = "";
	private final static String timeToGoStaleInMinutes = "";
	private final static String timeToLiveInMinutes = "";
	private final static String writeConsistencyLevel = "";
	private final static String msgType = "auditd";
	
	public final static int MAX_MESSAGE_SIZE =  64*1024;
	
	@Test
	public void testAdd() throws InterruptedException {
		
		while (true) {
		long timestamp = System.currentTimeMillis() ;
		UUID uuid = TimeUUIDUtils.getTimeUUID(timestamp);
		String epochtime = new Long(timestamp).toString();
		String msgtype="auditd";
		String rowkey="chicgdatapp109.cg.bskyb.com";
		String hostname="";
		try {
			cassandraDao.insert(uuid, epochtime, msgtype, testLog, rowkey, hostname);
		} catch (CassandraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread.currentThread().sleep(100);
		}

	}
        
	@Test
	public void testGet() throws InterruptedException {

	}
        
	@Test
	public void testRemove() throws InterruptedException {
		
	}
        
	



	@Before
	public  void setUp() throws Exception {

		CassandraConfigurationBuilder cassandraConfigurationBuilder = new CassandraConfigurationBuilder();
		cassandraConfigurationBuilder.setClusterName(clusterName);
		cassandraConfigurationBuilder.setExhaustPolicy(exhaustPolicy);
		cassandraConfigurationBuilder.setHosts(hosts);
		cassandraConfigurationBuilder.setKeySpaceName(keySpaceName);
		cassandraConfigurationBuilder.setLifo(true);
		cassandraConfigurationBuilder.setMaxActive(10);
		cassandraConfigurationBuilder.setMaxIdle(10);
		cassandraConfigurationBuilder.setMaxWaitTimeWhenExhausted(10);
		cassandraConfigurationBuilder.setReadConsistencyLevel(readConsistencyLevel);
		cassandraConfigurationBuilder.setRowCount(10);
		cassandraConfigurationBuilder.setThriftSocketTimeout(10);
		cassandraConfigurationBuilder.setTimeToGoStaleInMinutes(2);
		cassandraConfigurationBuilder.setTimeToLiveInMinutes(2);
		cassandraConfigurationBuilder.setWriteConsistencyLevel(writeConsistencyLevel);
		cassandraConfigurationBuilder.setMsgType(msgType);
		cassandraConfigurationBuilder.buildSchema();
		cassandraConfiguration = cassandraConfigurationBuilder.build();
		cassandraDao = new CassandraDao(cassandraConfiguration);
		

	}
	
	@After
	public void tearDown() throws Exception {

		CassandraConfigurationBuilder cassandraConfigurationBuilder = new CassandraConfigurationBuilder();
		cassandraConfigurationBuilder.setClusterName(clusterName);
		cassandraConfigurationBuilder.setExhaustPolicy(exhaustPolicy);
		cassandraConfigurationBuilder.setHosts(hosts);
		cassandraConfigurationBuilder.setKeySpaceName(keySpaceName);
		cassandraConfigurationBuilder.setLifo(true);
		cassandraConfigurationBuilder.setMaxActive(10);
		cassandraConfigurationBuilder.setMaxIdle(10);
		cassandraConfigurationBuilder.setMaxWaitTimeWhenExhausted(10);
		cassandraConfigurationBuilder.setReadConsistencyLevel(readConsistencyLevel);
		cassandraConfigurationBuilder.setRowCount(10);
		cassandraConfigurationBuilder.setThriftSocketTimeout(10);
		cassandraConfigurationBuilder.setTimeToGoStaleInMinutes(2);
		cassandraConfigurationBuilder.setTimeToLiveInMinutes(2);
		cassandraConfigurationBuilder.setWriteConsistencyLevel(writeConsistencyLevel);
		cassandraConfigurationBuilder.setMsgType(msgType);
		//cassandraConfigurationBuilder.dropSchema();

	}

}
