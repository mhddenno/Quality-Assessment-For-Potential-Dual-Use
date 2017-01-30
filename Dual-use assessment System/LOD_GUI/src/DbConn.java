import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * This class is responsible for DB connections in windows machines with MSSQL Server
 * @param non
 * @return non
 */
public class DbConn {
	static Connection con;
	static String dbName = "New Projects DB"; 
	public static void OpenConnection(){
		try{	
			String url = "jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName="+dbName+";integratedSecurity=true";
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection(url);		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public float calcualteTheQuestionRank(ArrayList<String> words)
	{
		float rank=0;
		for (String object: words) {
		   // System.out.println(object);
		    String query="SELECT [Rank] FROM ["+dbName+"].[dbo].[Word] WHERE [Word]='"+object+"'";
		   try{
		    Statement st=con.createStatement();
			ResultSet re=st.executeQuery(query);
				if(re.next())
				{
					rank=rank+Float.parseFloat(re.getString(1));
				}
		   }
		   catch(Exception e)
		   {
			   
		   }
		   
		}
		return rank;	
	}
	
	public float checkIfResExist(String Res)
	{
		float ID=0;
		
		String query="Select ID from DbpediaRes where Resource='"+Res+"'";
		//System.out.println(query);
		   try{
		    Statement st=con.createStatement();
			ResultSet re=st.executeQuery(query);
				if(re.next())
				{
					ID=Float.parseFloat(re.getString(1));
			
				}
		   }
		   catch(Exception e)
		   {
			   
		   }
		return ID;	
	}
	
	
	public void resourcesOfAwordTypeSynonyms(String word)
	{
		String query="select Resource from [Resources] where Synonym='"+word+"'";
		System.out.println(query);
		excuteQuery(query);
	}
	public void resourcesOfAwordTypeCategories(String word)
	{
		String query="select Resource from [Resources] where Category='"+word+"'";
		excuteQuery(query);
	}
	public void countTheNumberOfResourcesOfAwordTypeSynonyms(String word)
	{
		String query="select COUNT(Resource) AS total from [Resources] where Synonym='"+word+"'";
		excuteQuery(query);
	}
	public void countTheNumberOfResourcesOfAwordTypeCategories(String word)
	{
		String query="select COUNT(Resource) AS total from [Resources] where Category='"+word+"'";
		excuteQuery(query);
	}
	private void excuteQuery(String query)
	{
		try
		{
		Statement st=con.createStatement();
		ResultSet re=st.executeQuery(query);
			while(re.next())
			{
			System.out.println(re.getString(1));
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
}
