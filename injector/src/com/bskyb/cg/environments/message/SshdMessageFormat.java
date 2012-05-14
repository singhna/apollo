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
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.joda.time.DateTime;

import com.bskyb.cg.environments.exception.ParseException;

public class SshdMessageFormat extends BaseMessageFormat {


	public SshdMessageFormat(String delimiter, String datePattern) {
		super(delimiter, datePattern);
	}

	@Override
	public Message parse(byte[] inMessage) throws ParseException {
		Message message = new LogMessage();
		String textMessage = new String(inMessage);

		StringTokenizer st = new StringTokenizer(textMessage, delimiter);
		String msgType = (String) st.nextElement();
		String timeStamp = (String) st.nextElement();
		DateTime longDate = null;
		try {
			longDate = parseDate(timeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (longDate == null) {
			return message;
		}
		String year = new Integer(longDate.getYear()).toString();
		String month = String.format("%02d", new Integer(longDate.getMonthOfYear()));
		String day = String.format("%02d", new Integer(longDate.getDayOfMonth()));

		String hostName = (String) st.nextElement();

		long epochTime = longDate.getMillis();

		UUID uuid = TimeUUIDUtils.getTimeUUID(epochTime);

		String filename = year + "-" + month + "-" + day + "_" + hostName + "_" + msgType + "_" + uuid.toString() + "_" + new Long(epochTime).toString();
		
		String messageText = (String) st.nextElement();
		message.setKey(filename);
		message.setMessage(messageText.getBytes());

		return message;


	}

}

