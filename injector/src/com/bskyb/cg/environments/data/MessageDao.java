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


package com.bskyb.cg.environments.data;


import java.util.UUID;

import com.bskyb.cg.environments.exception.CassandraException;
import com.bskyb.cg.environments.exception.MessageDaoException;
import com.bskyb.cg.environments.exception.NotFoundException;
import com.bskyb.cg.environments.message.Message;

public interface MessageDao {

	public void createLogMessage(Message message) throws MessageDaoException;
	public void insert(UUID cassandrauuid, String epochtime, String msgtype, String syslogMsg , String rowkey, String hostname) throws CassandraException; 
	public Message retrieveLogMessage(String id) throws MessageDaoException, NotFoundException;
	public void updateLogMessage(Message message) throws MessageDaoException;
	public void deleteLogMessage(String id) throws MessageDaoException;
	void deleteExpiredLogMessages(int timeToGoStaleMins, int timeToLiveMins);

}
