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



package com.bskyb.cg.environments.queue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;

import com.bskyb.cg.environments.message.Message;
import com.bskyb.cg.environments.utils.FileExtFilter;



public class Queue {
    private final String dirname;

    private ConcurrentHashMap<String, Message> hash;

    private final static String TEMPFILEPOSTFIX = ".tmp";
    
    private final static String STOREFILEEXT = "ser";
    
    public Queue(String dirname) throws IOException {
        this.dirname = dirname;
       
        hash = new ConcurrentHashMap<String, Message>();
        File directory = new File(dirname);
        
        if (directory.isDirectory()) {
        	refreshFromStore(this.dirname);
        } else {
            createEmptyStore(this.dirname);
        }
    }

    public synchronized void clear() throws IOException {
    	hash.clear();
        refreshStore();
    }

    public synchronized boolean isEmpty() {
        return hash.size() == 0;
    }
    
    public synchronized int size() {
        return hash.size();
    }
    
    public synchronized Message lookup(String key) {
        if (hash.size() != 0) 
            return hash.get(key);
        
        return null;
    }
    
    public synchronized Message remove(String key) throws IOException {
        if (hash.size() == 0) {
            return null;
        }
    	Message message = hash.remove(key);
    	removeEntryFromStore(key);
    	
        return message;
    }

    public synchronized void add(String key, Message message) throws IOException {
    	appendEntryToStore(message);
        
        hash.put(key, message);
        
        return;
    }
    
    private void createEmptyStore(String dirname) throws IOException {
        File emptyDir = new File(dirname);
        if (!emptyDir.mkdirs()) {
            throw new IOException("Could not create new dir: " + dirname);
        }
    }
    
    private synchronized void refreshFromStore(String dirname) throws IOException {
    	
    	FileInputStream fis;
    	BufferedInputStream bis;
        Serializable messageEntry;
        ObjectInputStream ois;
    	File emptyDir = new File(dirname);
    	FilenameFilter onlyDat = new FileExtFilter(STOREFILEEXT);
    	File [] files = emptyDir.listFiles(onlyDat);


        hash.clear();
        
    	for (File file : files) {
    		fis = new FileInputStream(file);
    		bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            try {
                messageEntry = (Serializable)ois.readObject();
            } catch (ClassNotFoundException e) {
                throw new IOException(e.toString()); 
            } catch (ClassCastException e) {
                throw new IOException(e.toString()); 
            } catch (StreamCorruptedException e) {
                throw new IOException(e.toString());
            }
            try {
                hash.put(file.getName(),(Message)messageEntry);
            } catch (ClassCastException e) {
                throw new IOException(e.toString()); 
            } 
        	fis.close();
    	}
    }
    
    private synchronized void removeEntryFromStore(String key)
            throws IOException {
    	
    	File file = new File(dirname, key);
    	file.delete();
    }

    private synchronized void appendEntryToStore(Message message)
            throws IOException {
    	
    	String filename = message.getKey();

    	File file = new File(dirname, filename);
        FileOutputStream fos = new FileOutputStream(file, true);
    	BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        
        oos.writeObject(message);
        oos.flush(); oos.close();
        fos.flush(); fos.close();
    }
    
    private synchronized void writeQueue(String newdirname) throws IOException {
        
    	FileOutputStream fos;
    	
    	Enumeration<String> e = hash.keys();
    	if (e == null) return;
    	Message message;
        ObjectOutputStream oos = null;
        BufferedOutputStream bos = null;
        createEmptyStore(newdirname);
        String key;
    	while(e.hasMoreElements()) {
    		key = (String) e.nextElement();
    		message = hash.get(key);
    		File outFile = new File(newdirname, key);
    		fos = new FileOutputStream(outFile);
    		bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            oos.flush(); oos.close();
            fos.flush(); fos.close();

    	}
    	
    }
    
    private synchronized void refreshStore() throws IOException {
        String refreshedDirName = dirname + TEMPFILEPOSTFIX;
        
        writeQueue(refreshedDirName);
        
        File refreshedDir = new File(refreshedDirName);
        File originalDir = new File(dirname);
        
        FileUtils.deleteDirectory(originalDir);
        if (!refreshedDir.renameTo(originalDir)) {
            throw new IOException("Unable to rename " + refreshedDir + 
                    " to " + dirname);
        }
    }
 
}
