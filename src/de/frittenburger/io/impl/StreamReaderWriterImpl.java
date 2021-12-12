package de.frittenburger.io.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import de.frittenburger.io.interfaces.StreamReaderWriter;

public class StreamReaderWriterImpl extends Thread implements StreamReaderWriter {

	private final Logger logger = Logger.getLogger(this.getClass());
	private final InputStream in;
	private final OutputStream out;

	public StreamReaderWriterImpl(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {

		try
		{
			logger.info("Start reading");
			byte[] tmp=new byte[1024];
			int timer = 0;
		    while(timer < 30000) { //30 Sekunden
		      while(in.available()>0){
		    	timer = 0;
		        int i=in.read(tmp, 0, 1024);
		        logger.info("read "+i+" bytes");
		        if(i<0) break;
		        out.write(tmp,0,i);
		      }
		      timer += 100;
		      Thread.sleep(100);
		    }	
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		} finally {
			logger.info("Stop reading");
		}
	}

}
