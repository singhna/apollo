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

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.bskyb.cg.environments.cassandra.CassandraConfigurationBuilder;
import com.bskyb.cg.environments.utils.DynamicProperties;
import java.util.TreeMap;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


public class MessageFormatFactory  {

	private static Logger logger = Logger.getLogger(MessageFormatFactory.class);	
	protected  Map<String, Class<MessageFormat>> classes = new TreeMap<String, Class<MessageFormat>>();
	protected  Map<String, Object[]> args = new TreeMap<String, Object[]>();
	protected DynamicProperties keysToClassNames;
	
	public MessageFormatFactory(DynamicProperties keysToClassNames) {
		this.keysToClassNames = keysToClassNames;
		try {
			initFromProperties();
		} catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getClass().getName() + ": " +
                    ex.getMessage());
        }
    }

	public DynamicProperties getKeysToClassNames() {
		return keysToClassNames;
	}

	@Autowired
	public void setKeysToClassNames(DynamicProperties keysToClassNames) {
		this.keysToClassNames = keysToClassNames;
	}
	
    protected void initFromProperties() throws Exception {

    	PropertiesConfiguration propertiesConfiguration = keysToClassNames.getConfigure();
    	Iterator <String> iterator = propertiesConfiguration.getKeys();
    	String key;
    	String className;
    	String[] params = null;
	        while (iterator.hasNext()) {
	        	key = (String) iterator.next();
	        	params = propertiesConfiguration.getStringArray(key);
        		className = params[0];
        		Class<MessageFormat> cls = (Class<MessageFormat>) Class.forName(className);
        		classes.put(key, cls);
       			args.put(key,ArrayUtils.subarray(params, 0, params.length));
	        }
        
    }
    

    private Object newInstance(String logFormat, Class<?>[] paramTypes,
            Object[] params) {
    	Object obj = null;
    	String classname = logFormat;

        try {
            Class<?> cls = classes.get(classname);

            if (cls == null) {
                throw new RuntimeException("No class registered under " +
                        logFormat);
            }

            Constructor<?> ctor = cls.getConstructor(paramTypes);
            obj = ctor.newInstance(params);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return obj;
    }
    
	public MessageFormat getMessageFormat(String logFormat) {
		String delimiter = (String) args.get(logFormat)[1];
		String datePattern = (String) args.get(logFormat)[2];
		logger.info("Date Pattern : " + datePattern);
       return (MessageFormat) newInstance(logFormat, new Class[] {String.class, String.class }, new Object[] {  delimiter, datePattern});
	}


}
