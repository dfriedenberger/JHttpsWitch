package de.frittenburger.io.impl;
/*
 *  Copyright notice
 *
 *  (c) 2016 Dirk Friedenberger <projekte@frittenburger.de>
 *
 *  All rights reserved
 *
 *  This script is part of the JHttpSwitch project. The JHttpSwitch is
 *  free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  The GNU General Public License can be found at
 *  http://www.gnu.org/copyleft/gpl.html.
 *
 *  This script is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  This copyright notice MUST APPEAR in all copies of the script!
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import de.frittenburger.core.interfaces.StreamHandler;
import de.frittenburger.io.interfaces.Connection;
import de.frittenburger.io.interfaces.SocketWrapper;

public class ConnectionImpl extends Thread implements Connection {

	private final Logger logger = Logger.getLogger(this.getClass());

	private boolean lock = false;
	private Object ob = new Object();

	private InputStream in = null;
	private OutputStream out = null;
	private SocketWrapper socket = null;

	private final StreamHandler streamHandler;

	private String threadName = "Unknown";

	public ConnectionImpl(StreamHandler streamHandler)
	{
		this.streamHandler = streamHandler;
	}
	
	
	@Override
	public Connection lock() {
		if (lock)
			throw new RuntimeException("Client is locked ["+threadName+"]");
		logger.debug("lock connection ["+threadName+"]");
		lock = true;
		return this;
	}

	@Override
	public boolean isAvailable() {
		return (lock == false);
	}

	@Override
	public void newConnection(SocketWrapper socket) {

		logger.info("new connection handler=[" + threadName + "] socket="+socket);

		this.socket = socket;

		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			logger.error(e);
			cleanup();
		}

		synchronized (ob) {
			ob.notify();
		}
	}

	private void cleanup() {
		
		logger.info("cleanup connection");

		try {
			if(in != null)
				in.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			in = null;
		}
		
		
		try {
			if(out != null)
				out.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			out = null;
		}

		try {
			if(socket != null)
				socket.close();
		} catch (Exception e) {
			logger.warn(e);
		}
		finally
		{
			socket = null;
		}
		lock = false;
		logger.debug("unlock connection");
	}

	@Override
	public void run()
	{
		synchronized(threadName)
	    {
			threadName = Thread.currentThread().getName();
	    }
		
		while(true)
		{
			 synchronized(ob)
		     {
				 try {
					
					//wait for new connection
					ob.wait();
					logger.debug("handle connection");
					
					//handle connection
					streamHandler.handle(socket.getProtocol(),socket.getInetAddress(),in,out);
				} catch (Exception e) {
					logger.error(e);
				}
				
				cleanup(); 
		     }
		}
	}
}
