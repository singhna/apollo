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

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.log4j.Logger;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;

import com.bskyb.cg.environments.cassandra.CassandraConfiguration;
import com.bskyb.cg.environments.cassandra.LogConsistencyLevelPolicy;
import com.bskyb.cg.environments.server.LogServer;

public class CassandraConfigurationBuilder {
	
	private static Logger logger = Logger.getLogger(CassandraConfigurationBuilder.class);
	private String columnFamilyName;
	private int rowCount;
	private String hosts;
	private int thriftSocketTimeout;
	private String exhaustPolicy;
	private int maxActive;
	private int maxIdle;
	private int maxWaitTimeWhenExhausted;
	private boolean lifo;
	private String clusterName;
	private String keySpaceName;
	private String readConsistencyLevel;
	private String writeConsistencyLevel;
	private int timeToLiveInMinutes;
	private int timeToGoStaleInMinutes;
	private String msgType;
	private boolean autoDiscoverHosts;
	private boolean retryDownedHosts;
	private int retryDownedHostsDelayInSeconds;
	
	public CassandraConfiguration build() {

		
		CassandraHostConfigurator cassandraHostConfigurator = new CassandraHostConfigurator(hosts);
		cassandraHostConfigurator.setCassandraThriftSocketTimeout(thriftSocketTimeout);
		cassandraHostConfigurator.setMaxActive(maxActive);
		cassandraHostConfigurator.setMaxWaitTimeWhenExhausted(maxWaitTimeWhenExhausted);
		cassandraHostConfigurator.setLifo(lifo);
		cassandraHostConfigurator.setAutoDiscoverHosts(autoDiscoverHosts);
		cassandraHostConfigurator.setRetryDownedHosts(retryDownedHosts);
		cassandraHostConfigurator.setRetryDownedHostsDelayInSeconds(retryDownedHostsDelayInSeconds);
		cassandraHostConfigurator.setHosts(hosts);
		LogConsistencyLevelPolicy consistencyLevelPolicy = new LogConsistencyLevelPolicy(readConsistencyLevel, writeConsistencyLevel);
		Cluster cluster = HFactory.getOrCreateCluster(clusterName, cassandraHostConfigurator);
		Keyspace keyspace = HFactory.createKeyspace(keySpaceName, cluster, consistencyLevelPolicy);
		CassandraConfiguration cassandraConfiguration = new CassandraConfiguration(keyspace, columnFamilyName, rowCount, timeToLiveInMinutes, timeToGoStaleInMinutes);		
		return cassandraConfiguration;
	}

	public void buildSchema() {
		
		
		Cluster cluster = HFactory.getOrCreateCluster(clusterName,hosts);
	
		try {
			ColumnFamilyDefinition col = HFactory.createColumnFamilyDefinition(keySpaceName, msgType, ComparatorType.TIMEUUIDTYPE);
			ThriftCfDef tcf = new ThriftCfDef(col);
			tcf.setName(msgType);
			tcf.setColumnType(ColumnType.STANDARD);
			tcf.setComparatorType(ComparatorType.UUIDTYPE);
			tcf.setDefaultValidationClass(ComparatorType.UTF8TYPE.getClassName());
			tcf.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName());
			cluster.addColumnFamily(tcf,true);
			ColumnFamilyDefinition colCounter = HFactory.createColumnFamilyDefinition(keySpaceName, msgType+"Counter", ComparatorType.TIMEUUIDTYPE);
			colCounter.setName(msgType+"Counter");
			ThriftCfDef tcfCounter = new ThriftCfDef(colCounter);
			tcfCounter.setName(msgType+"Counter");
			tcfCounter.setColumnType(ColumnType.STANDARD);
			tcfCounter.setComparatorType(ComparatorType.UTF8TYPE);
			tcfCounter.setDefaultValidationClass(ComparatorType.COUNTERTYPE.getClassName());
			tcfCounter.setKeyValidationClass(ComparatorType.UTF8TYPE.getClassName());
			cluster.addColumnFamily(tcfCounter,true);


		} catch (HectorException e) {
			logger.warn("column exists :" + msgType, e);
		}
		  catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void dropSchema() {
		
		
		Cluster cluster = HFactory.getOrCreateCluster(clusterName,hosts);
	
		try {
			cluster.dropColumnFamily(keySpaceName, msgType, true);
			cluster.dropColumnFamily(keySpaceName, msgType+"Counter", true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getColumnFamilyName() {
		return columnFamilyName;
	}

	public void setColumnFamilyName(String columnFamilyName) {
		this.columnFamilyName = columnFamilyName;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public int getThriftSocketTimeout() {
		return thriftSocketTimeout;
	}

	public void setThriftSocketTimeout(int thriftSocketTimeout) {
		this.thriftSocketTimeout = thriftSocketTimeout;
	}

	public String getExhaustPolicy() {
		return exhaustPolicy;
	}

	public void setExhaustPolicy(String exhaustPolicy) {
		this.exhaustPolicy = exhaustPolicy;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitTimeWhenExhausted() {
		return maxWaitTimeWhenExhausted;
	}

	public void setMaxWaitTimeWhenExhausted(int maxWaitTimeWhenExhausted) {
		this.maxWaitTimeWhenExhausted = maxWaitTimeWhenExhausted;
	}

	public boolean isLifo() {
		return lifo;
	}

	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getKeySpaceName() {
		return keySpaceName;
	}

	public void setKeySpaceName(String keySpaceName) {
		this.keySpaceName = keySpaceName;
	}

	public String getReadConsistencyLevel() {
		return readConsistencyLevel;
	}

	public void setReadConsistencyLevel(String readConsistencyLevel) {
		this.readConsistencyLevel = readConsistencyLevel;
	}

	public String getWriteConsistencyLevel() {
		return writeConsistencyLevel;
	}

	public void setWriteConsistencyLevel(String writeConsistencyLevel) {
		this.writeConsistencyLevel = writeConsistencyLevel;
	}

	public int getTimeToLiveInMinutes() {
		return timeToLiveInMinutes;
	}

	public void setTimeToLiveInMinutes(int timeToLiveInMinutes) {
		this.timeToLiveInMinutes = timeToLiveInMinutes;
	}

	public int getTimeToGoStaleInMinutes() {
		return timeToGoStaleInMinutes;
	}

	public void setTimeToGoStaleInMinutes(int timeToGoStaleInMinutes) {
		this.timeToGoStaleInMinutes = timeToGoStaleInMinutes;
	}

	public boolean isAutoDiscoverHosts() {
		return autoDiscoverHosts;
	}

	public void setAutoDiscoverHosts(boolean autoDiscoverHosts) {
		this.autoDiscoverHosts = autoDiscoverHosts;
	}

	public boolean isRetryDownedHosts() {
		return retryDownedHosts;
	}

	public void setRetryDownedHosts(boolean retryDownedHosts) {
		this.retryDownedHosts = retryDownedHosts;
	}

	public int getRetryDownedHostsDelayInSeconds() {
		return retryDownedHostsDelayInSeconds;
	}

	public void setRetryDownedHostsDelayInSeconds(int retryDownedHostsDelayInSeconds) {
		this.retryDownedHostsDelayInSeconds = retryDownedHostsDelayInSeconds;
	}

	
}
