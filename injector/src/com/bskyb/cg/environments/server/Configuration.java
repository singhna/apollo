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


package com.bskyb.cg.environments.server;

import java.util.*;
import java.io.*;

public class Configuration {
	private static Configuration _instance = null;

	private Properties props = null;

	public Configuration() {
		props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(new File("/tmp/stronghold.properties"));
			props.load(fis);
		} catch (Exception e) {
			// catch Configuration Exception right here
		}
	}

	public synchronized static Configuration getInstance() {
		if (_instance == null)
			_instance = new Configuration();
		return _instance;
	}

	// get property value by name
	public String getProperty(String key) {
		String value = null;
		if (props.containsKey(key))
			value = (String) props.get(key);
		else {
			// the property is absent
		}
		return value;
	}
}
