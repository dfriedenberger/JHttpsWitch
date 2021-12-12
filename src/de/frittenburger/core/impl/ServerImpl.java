package de.frittenburger.core.impl;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.frittenburger.core.interfaces.Server;
import de.frittenburger.core.interfaces.SocketHandler;
import de.frittenburger.io.interfaces.ServerSocketWrapper;
import de.frittenburger.io.interfaces.SocketWrapper;

public class ServerImpl extends Thread implements Server {

	private final Logger logger = LogManager.getLogger(this.getClass());
	private final ServerSocketWrapper serverSocket;
	private final SocketHandler handler;
	public ServerImpl(ServerSocketWrapper serverSocket, SocketHandler handler)
	{
		this.serverSocket = serverSocket;
		this.handler = handler;
	}
	
	
	@Override
	public void run() {

			  logger.info("Start "+serverSocket);
		      while (!interrupted()) {

		    	  try {
						
						// waiting for Connection from client
						SocketWrapper clientSocket = serverSocket.accept();
						logger.debug("new connect from "+clientSocket+" to "+serverSocket);
						handler.handle(clientSocket);
						
					} catch (IOException e) {
						this.logger.warn(e);
					}
		      }
		 
			  logger.info("Stop");

		
	
	}
	
	
}
