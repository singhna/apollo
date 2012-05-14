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

import org.springframework.beans.factory.FactoryBean;

import com.bskyb.cg.environments.cassandra.CassandraConfigurationBuilder;
import com.bskyb.cg.environments.cassandra.CassandraDao;

public class MessageDaoFactory implements FactoryBean<MessageDao> {

	public static MessageDao instance;

	
	public MessageDaoFactory(CassandraConfigurationBuilder builder) {
		// You can add more db dao's in future and return the required one.
		builder.buildSchema();
		instance = new CassandraDao(builder.build());
	}

	@Override
	public MessageDao getObject() throws Exception {
		return instance;
	}

	@Override
	public Class<MessageDao> getObjectType() {
		return MessageDao.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}


