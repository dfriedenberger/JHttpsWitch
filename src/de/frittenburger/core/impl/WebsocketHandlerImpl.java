package de.frittenburger.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;

import de.frittenburger.core.interfaces.Client;
import de.frittenburger.core.interfaces.WebsocketHandler;
import de.frittenburger.io.bo.HttpRequest;
import de.frittenburger.io.bo.HttpResponse;
import de.frittenburger.io.impl.HttpResponseOutputStreamWriterImpl;
import de.frittenburger.io.impl.StreamReaderWriterImpl;
import de.frittenburger.routing.bo.Target;

public class WebsocketHandlerImpl implements WebsocketHandler {

	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void handle(Target target, HttpRequest req, InputStream in, OutputStream out) throws IOException, GeneralSecurityException, RuntimeException, ReflectiveOperationException {

		Client client = Builder.custom(ClientBuilder.class).configure(new URL(target.getUrl())).build();
		
		// Nun auf entfernten Server zugreifen
		try
		{
			//Handshake
			client.connect();
			
			
			logger.info("send request "+req);
			client.write(req);
			
			HttpResponse res =  client.read();
			logger.info("recv response  "+res);

			
			new HttpResponseOutputStreamWriterImpl().write(out,res);

			//bidirectional communication

			Thread t1 = new Thread(new StreamReaderWriterImpl(in,client.getOutputStream()));
			t1.start();
			
			Thread t2 = new Thread(new StreamReaderWriterImpl(client.getInputStream(),out));
			t2.start();
			logger.info("streams connected");

			t1.join();
			t2.join();
			logger.info("streams closed");

			
		} 
		catch(IOException e)
		{
			logger.error(e);
		} catch (InterruptedException e) {
			logger.error(e);
		}
		finally
		{
			client.disconnect();
		}
		
	}

}
