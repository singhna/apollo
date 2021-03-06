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

import java.io.Serializable;


public class LogMessage implements Serializable, Message {
	private static final long serialVersionUID = 7286408362097951766L;


	byte[] message;
	String key;
	
	public LogMessage() {
	}
	
	public LogMessage(String inKey, byte[] inMessage) {
		key = inKey;
		message = inMessage;
	}
	public byte[] getMessage() {
		return message;
	}

	public String getKey() {
		return key;
	}

	public void setMessage(byte[] input) {
		message = input;
	}

	public void setKey(String inputKey) {
		key	= inputKey;
	} 

}
