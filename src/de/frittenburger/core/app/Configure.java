package de.frittenburger.core.app;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.frittenburger.io.impl.ConsoleImpl;
import de.frittenburger.io.impl.PersistenceServiceImpl;
import de.frittenburger.io.interfaces.Console;
import de.frittenburger.parser.impl.ApacheConfigParserImpl;
import de.frittenburger.parser.interfaces.ApacheConfigParser;

public class Configure {

	public static void main(String[] args) throws IOException {
	
		
		 Console console = new ConsoleImpl(System.in);
		 
		 //Server
		 if(console.getYesNo("generate config/server.json?",!new File("config/server.json").exists()))
		 {
			 String ip = console.getText("server ip",getDefaultIp());
			 CreateDefaultConfig.genServerConfig(ip,"config/server.json");
		 }
		 
		 //letsEncrypt
		 String letsEncryptPath = getLetsEncryptPath();
		 if(console.getYesNo("generate config/letsencrypt.json?",!new File("config/letsencrypt.json").exists()))
		 {
			 letsEncryptPath = console.getText("letsencrypt path",letsEncryptPath);
			 CreateDefaultConfig.genLetsEncryptConfig(letsEncryptPath,"config/letsencrypt.json");
		 }
			
		 //Routing
		 if(console.getYesNo("generate config/routing.json?",!new File("config/routing.json").exists()))
		 {	
			 Map<String, String> apacheTargets = getApacheTartgets();
			
			
			 
			 Map<String,List<String>> targets = new HashMap<String,List<String>>();
			 for(String domain : getLetsEncryptDomains(letsEncryptPath))
			 {
				 if(!console.getYesNo("use "+domain,true)) continue;
				 
				 String target = "http://localhost:8080";
				 if(apacheTargets.containsKey(domain))
					 target = apacheTargets.get(domain);
				 target = console.getText("target for domain","http://localhost:8080");
				 
				 
				 if(!targets.containsKey(target)) targets.put(target, new ArrayList<String>());
				 targets.get(target).add(domain);
				 int count = domain.length() - domain.replace(".", "").length();
				 if(count == 1)
					 targets.get(target).add("www."+domain);
			 }
			 CreateDefaultConfig.genRoutingConfig(targets, "config/routing.json");
		 }

		
		//UserAgents
		 if(console.getYesNo("generate config/useragent.json?",!new File("config/useragent.json").exists()))
		 {
			 CreateDefaultConfig.genUserAgent("config/useragent.json");
		 }
		
		

	}

	
	private static Map<String, String> getApacheTartgets() {
		ApacheConfigParser apacheConfigParser = new ApacheConfigParserImpl(new PersistenceServiceImpl());
		try {
			return apacheConfigParser.parse(getApacheConfigFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new HashMap<String, String>(); //empty
	}


	private static String getApacheConfigFilename() {
		return "/etc/apache2/ports.conf";
	}

	
	private static List<String> getLetsEncryptDomains(String path) {
		List<String> domains = new ArrayList<String>();
		for(File f : new File(path).listFiles())
		{
			if(!f.isDirectory()) continue;
			domains.add(f.getName());
		}
		return domains;
	}

	private static String getLetsEncryptPath() {
		return "/etc/letsencrypt/live";
	}

	private static String getDefaultIp() {
		 try {
	           InetAddress ipAddr = InetAddress.getLocalHost();
	           return ""+ipAddr.getHostAddress();
	        } catch (UnknownHostException ex) {
	            ex.printStackTrace();
	        }
		return "192.168.0.1";
	}

	
	
	
}
