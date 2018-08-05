# JHttpsWitch


JHttpsWitch is the easy, open source way for distribute HTTP(S) request to backend.

- ssl adapter
- routing
- tracking
- firewall
- opensource

# Get started
```
git clone https://github.com/dfriedenberger/JHttpsWitch.git
mvn compile 
mvn exec:java -Dexec.mainClass="de.frittenburger.core.app.Configure"
mvn exec:java -Dexec.mainClass="de.frittenburger.core.app.JHttpsWitch"
```

# Routing
- ssl redirect
- routing to backend server

# Tracking
- build sqlite-Database (DataVault und BusinessVault )
- visuallizize e.G. with metabase

# Web Application Firewall (WAF) / WebShield
- white listening 
- upload inspector

# Cache (InMemory)

# Notification (post-request)

# Loadbalancer 

# Html-Injection (Datenschutz, Cokkies)

# PageSpeed optimization


# Configuration (contains in config templates)
## server.json
```
[  
	{
	  "protocol" : "HTTPS",
	  "host" : "127.0.0.1",
	  "port" : 443
	},
	{
	  "protocol" : "HTTP",
	  "host" : "127.0.0.1",
	  "port" : 80
	}
]
```
## routing.json
```
[ 
	{
		  "sslEnabled" : true,
		  "target" : "http://localhost:3000",
		  "host" : "example.com"
	}, 
	{
		  "sslEnabled" : true,
		  "target" : "http://localhost:7777",
		  "host" : "*"
	} 
]
```
## letsencrypt.json
```
{
  "livePath" : "/etc/letsencrypt/live"
}
```



# Contact
Dirk Friedenberger, Waldaschaff, Germany

Write me (oder Schreibe mir)
projekte@frittenburger.de

http://www.frittenburger.de 

