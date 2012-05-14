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

import me.prettyprint.hector.api.Keyspace;

public class CassandraConfiguration {
	private final Keyspace keyspace;
	private final String columnFamilyName;
	private final int rowCount;
	private final int timeToLiveInSeconds;
	private final int timeToGoStaleInSeconds;

	public CassandraConfiguration(Keyspace keyspace, String columnFamilyName, int rowCount, int timeToLiveInMinutes, int timeToGoStaleInMinutes) {
		this.keyspace = keyspace;
		this.columnFamilyName = columnFamilyName;
		this.rowCount = rowCount;
		this.timeToLiveInSeconds = timeToLiveInMinutes * 60;
		this.timeToGoStaleInSeconds = timeToGoStaleInMinutes * 60;
	}

	public Keyspace getKeyspace() {
		return keyspace;
	}

	public String getColumnFamilyName() {
		return columnFamilyName;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getTimeToLiveInSeconds() {
		return timeToLiveInSeconds;
	}

	public int getTimeToGoStaleInSeconds() {
		return timeToGoStaleInSeconds;
	}

	@Override
	public String toString() {
		return "CassandraConfiguration [keyspace=" + keyspace + ", columnFamilyName=" + columnFamilyName + ", rowCount=" + rowCount + ", timeToLiveInSeconds=" + timeToLiveInSeconds + ", timeToGoStaleInSeconds=" + timeToGoStaleInSeconds + "]";
	}

}
