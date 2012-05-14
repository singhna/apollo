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



import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;

public class LogConsistencyLevelPolicy implements ConsistencyLevelPolicy {

	private final String readConsistencyLevel;
	private final String writeConsistencyLevel;
	
	public LogConsistencyLevelPolicy(String readConsistencyLevel, String writeConsistencyLevel) {
		this.readConsistencyLevel = readConsistencyLevel;
		this.writeConsistencyLevel = writeConsistencyLevel;
	}
	
	@Override
	public HConsistencyLevel get(OperationType operationtype) {
		try {
			switch (operationtype) {
			case READ:
				return HConsistencyLevel.valueOf(readConsistencyLevel);
			case WRITE:
				return HConsistencyLevel.valueOf(writeConsistencyLevel);
			default:
				return HConsistencyLevel.QUORUM;
			}
		} catch (IllegalArgumentException e) {
			return HConsistencyLevel.QUORUM;
		}
	}

	@Override
	public HConsistencyLevel get(OperationType operationtype, String s) {
		return get(operationtype);
	}

}
