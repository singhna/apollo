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


package com.bskyb.cg.environments.utils;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class DynamicProperties {

	private PropertiesConfiguration dynamicPropertiesConfigurer = null;


	public DynamicProperties(PropertiesConfiguration dynamicPropertiesConfigurer) throws ConfigurationException {
		this.setConfigure(dynamicPropertiesConfigurer);
		dynamicPropertiesConfigurer.load();
		
	}


	public PropertiesConfiguration getConfigure() {
		return dynamicPropertiesConfigurer;
	}


	public void setConfigure(PropertiesConfiguration configure) {
		this.dynamicPropertiesConfigurer = configure;
	}
}
