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

import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.bskyb.cg.environments.data.MessageDao;
import com.bskyb.cg.environments.exception.ParseException;

public class BaseMessageFormat implements MessageFormat{
	
	private static Logger logger = Logger.getLogger(BaseMessageFormat.class);
	String delimiter;
	String datePattern;

	public BaseMessageFormat(String delimiter, String datePattern) {
		this.delimiter = delimiter;
		this.datePattern = datePattern;
	}


	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public static String getMessageType(String inMessage, String del) throws ParseException {

		StringTokenizer st = new StringTokenizer(inMessage, del);
		String msgType = (String) st.nextElement();
		return msgType;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	protected DateTime parseDate(String datestring) throws java.text.ParseException, IndexOutOfBoundsException {

		DateTimeFormatter df = DateTimeFormat.forPattern(datePattern);

		DateTime d = new DateTime();
		d = df.parseDateTime(datestring);

		return d;
	}
	
	@Override
	public Message parse(byte[] inMessage) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}
}


