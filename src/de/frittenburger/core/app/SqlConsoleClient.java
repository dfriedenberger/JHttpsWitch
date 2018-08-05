package de.frittenburger.core.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.frittenburger.datawarehouse.bo.BusinessVault;


public class SqlConsoleClient {

	public static void main(String[] args) throws SQLException, IOException, ReflectiveOperationException {

		Class.forName("org.sqlite.JDBC");

		String url = "jdbc:sqlite:tracking/tracking.db";
		Connection conn = DriverManager.getConnection(url);

		// Get the database metadata
	
		DatabaseMetaData metadata = conn.getMetaData();
	
		// Specify the type of object; in this case we want tables
		
		String[] types = {"TABLE"};
	
		ResultSet resultSet = metadata.getTables(null, null, "%", types);
		
		
		dump(resultSet);
		
	
		  
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while(true)
		{
			System.out.print(">");
			System.out.flush();
			String line = br.readLine();
			if(line.trim().startsWith("q")) break;
			if(line.trim().equals("test")) 
			{
				String[] selectsql = new String[]{
						"sql/selectBV_Infrastructure.sql",
						"sql/selectBV_Request.sql"

				};
				for(int i = 0; i < selectsql.length;i++)
				{
					Statement stmt = conn.createStatement();
					System.out.println(selectsql[i]);
				 	ResultSet rs = stmt.executeQuery(BusinessVault.readFile(selectsql[i]));
				 	dump(rs);
				}
				continue;
			}

			Statement stmt = conn.createStatement();
			if(line.trim().toLowerCase().startsWith("drop"))
			{
				if(stmt.execute(line))
					System.out.println("successful");
			}
			else
			{
			 	ResultSet rs = stmt.executeQuery(line);
			 	dump(rs);
			}
		}
		
		conn.close();
		
		
	}

	public static void dump(ResultSet rs) throws SQLException {
		 ResultSetMetaData md = rs.getMetaData();
	 	  int c = md.getColumnCount();
	 	  List<List<String>> rows = new ArrayList<List<String>>();
 		  int[] width = new int[c];
 		 
 		  List<String> row = null;
 		  rows.add(row = new ArrayList<String>());
 		  for(int i=1; i<=c; ++i)
 		  {   
	 		  row.add(md.getColumnName(i));
	 		  width[i-1] = md.getColumnName(i).length();
	 	  }
	 	  
	 	  while (rs.next()){
	 		 rows.add(row = new ArrayList<String>());
	 	     for(int i=1; i<=c; ++i){   
		 		String d = rs.getString(i);
		 		if(d == null) d = "null";
	 	    	row.add(d);
			 	width[i-1] = Math.max(d.length(),width[i-1]);
	 	     }
	 	  }
	 	  
	 	  
	 	  for(List<String> r : rows)
	 	  {
	 		 for(int i = 0;i < c;i++)
	 		 {
	 			 String data = r.get(i);
	 			 
	 			 int b = (width[i] - data.length())/2;
	 			 int a = (width[i] - data.length()) - b;
	 			 System.out.print("|");
	 			 System.out.print(new String(new char[b]).replace("\0", " "));
	 			 System.out.print(data);
	 			 System.out.print(new String(new char[a]).replace("\0", " "));
	 		 }
	 		 System.out.println("|");
	 	  }
	 	  
	 	  
	 	  
	}

}
