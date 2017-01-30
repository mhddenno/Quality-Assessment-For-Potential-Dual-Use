package GDUS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SparqlQuery
{
	 /**
	   * public method that receive a query and fire it against the datasets to obtain the results 
	   * @param query
	   * @return results
	   */
	public final String ExecuteQuery(String query) throws IOException
	{
		try
		{
			URL oracle = new URL(query);
	        URLConnection yc = oracle.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	                                    yc.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	            System.out.println(inputLine);
	        in.close();
	        return inputLine;
		}
		catch (Exception exp)
		{
			return "";
		}
	} //end ExecuteQuery()

}