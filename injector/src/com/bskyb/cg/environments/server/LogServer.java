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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.bskyb.cg.environments.message.MessageHandler;

import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

@ContextConfiguration(locations={"classpath:context/logServerAppCtx.xml"})

public class LogServer implements Runnable{
	private static Logger logger = Logger.getLogger(LogServer.class);
	
	@Autowired
	private static ApplicationContext applicationContext;

	@Autowired
	private MessageHandler messageHandler;
	
	private int port;

	protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    
    Configuration config = Configuration.getInstance();
    
    

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    logger.error("LogServer Stopped." ) ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(
                new WorkerRunnable(
                    clientSocket, "Stronghold LogServer")
            ).start();
        }
        logger.info("LogServer Stopped." ) ;
    }

    public class WorkerRunnable implements Runnable{

        protected Socket clientSocket = null;
        protected String serverText   = null;

        public WorkerRunnable(Socket clientSocket, String serverText) {
            this.clientSocket = clientSocket;
            this.serverText   = serverText;
        }

        public void run() {
            try {
            	
                InputStream input  = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream();
                
                BufferedReader is = new BufferedReader(new InputStreamReader(input));
                
                
                String line;
                line = is.readLine();

                messageHandler.processMessage(line.getBytes());
     
                output.close();
                input.close();

            } catch (IOException e) {
            	logger.error("Exception in LogServer", e);
            }
        }
    }
    

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
        	logger.info("Starting server on port : " + port);
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.port, e);
        }
    }

    public static void main (String[] args) {
    	ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("context/logServerAppCtx.xml");
    	LogServer logServer = (LogServer) applicationContext.getBean("logServer");
    	new Thread(logServer).start();
    }
}
