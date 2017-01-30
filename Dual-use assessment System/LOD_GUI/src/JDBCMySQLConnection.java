import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import com.github.andrewoma.dexx.collection.HashMap;
import com.github.andrewoma.dexx.collection.List;
import com.github.andrewoma.dexx.collection.Map;

//import com.github.andrewoma.dexx.collection.HashMap;
//import com.github.andrewoma.dexx.collection.List;
//import com.github.andrewoma.dexx.collection.Map;

public class JDBCMySQLConnection {
	// Initialization
	private static JDBCMySQLConnection instance = new JDBCMySQLConnection();
    public static final String URL = "jdbc:mysql://localhost/gdus?characterEncoding=UTF-8&useSSL=false";
    public static final String USER = "root";
    public static final String PASSWORD = "LOD123";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver"; 
    public static final String Db_Name="gdus";
    
    /**
     * private constructor that would load the driver required for the system to connect to MySql database. 
     * @param non
     * @return non
     */
    public JDBCMySQLConnection() {
        try {
            // Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * private method that establishes a connection with the designated MySql database. 
     * @param non
     * @return non
     */
    private Connection createConnection() {
 
        Connection connection = null;
        try {
            // Establish Java MySQL connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    /**
     * private static method that searches the resource passed as a parameter in the database and if found it returns its ID. 
     * target table: dbpedia
     * @param resource to search for in the database
     * @return the ID of the resource in the database
     */
	public static float checkIfResExistDBpedia(String Res)
	{
		float ID=0;
    	ResultSet rs = null;
    	Connection connection = null;
    	Statement statement = null; 
		String query="Select ID from "+Db_Name+".dbpediadata where Resource='"+Res+"'";

		   try{
		  //  Statement st=con.createStatement();
	      //  ResultSet re=st.executeQuery(query);
			   connection = JDBCMySQLConnection.getConnection();
	              statement = connection.createStatement();
	              rs = statement.executeQuery(query);
				if(rs.next())
				{
					ID=Float.parseFloat(rs.getString(1));
			
				}
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		return ID;	
	}
	
	/**
     * private static method that searches the resource passed as a parameter in the database and if found it returns its ID.
     * target table: wikidata 
     * @param resource to search for in the database
     * @return the ID of the resource in the database
     */
	public static float checkIfResExistWIKIData(String Res)
	{
		float ID=0;
    	ResultSet rs = null;
    	Connection connection = null;
    	Statement statement = null; 
		String query="Select ID from "+Db_Name+".wikidata where Resource=\""+Res+"\"";
		//System.out.println(query);
		   try{
		  //  Statement st=con.createStatement();
	      //  ResultSet re=st.executeQuery(query);
			   connection = JDBCMySQLConnection.getConnection();
	              statement = connection.createStatement();
	              rs = statement.executeQuery(query);
				if(rs.next())
				{
					ID=Float.parseFloat(rs.getString(1));
			
				}
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		return ID;	
	}
	
	/**
     * public static method that takes a word and returns its root.
     * @param word
     * @return return the root of that word
     */
	public static String stemWord(String Word){
		Class stemClass;
		String Result="";
		StemmerInit stemmer;
		try {
			stemClass = Class.forName("englishStemmer");
			 try {
				stemmer = (StemmerInit) stemClass.newInstance();
				stemmer.setCurrent(Word);
				stemmer.stem();
				Result=stemmer.getCurrent();
			   // System.out.println(stemmer.getCurrent());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Result;
	}
	
	
	/**
     * public static method that takes a word and returns its rank according to the profiles.
     * @param word & connection
     * @return return the rank of the word 
     */
	  public static float calcualteStemedQuestionRank(String word,Connection connection)
		{
	    	//int ratio = words.size();
	    	ResultSet rs = null;
	    	//Connection connection = null;
	    	Statement statement = null; 
			float rank=0;
			
		//	for (String object: words) {
			   // System.out.println(object);
			 //   String query="SELECT Rank FROM "+Db_Name+".word WHERE Word='"+word+"'";
			  String query="SELECT Rank FROM dbo.word WHERE Root=\""+word+"\"";  
			try{
				 //  connection = JDBCMySQLConnection.getConnection();
		           statement = connection.createStatement();
		           rs = statement.executeQuery(query);
			    //Statement st=con.createStatement();
				//ResultSet re=st.executeQuery(query);
		           // Test if there is value 
					if(rs.next())
					{
						rank=Float.parseFloat(rs.getString(1));
					}
			   }
			   catch (SQLException e) {
		              e.printStackTrace();
		          }
			//}
			return rank;	
		}
	
  /**
   * public static method that receives an array of tokenized words and then find the ranking of these words in the database, calculate it and return it
   * if a word is not found, 0 is returned.
   * @param ArrayList of tokenized words
   * @return Total Rank of these words
   */
  public static float calcualteTheQuestionRank(ArrayList<String> words)
	{
  	int ratio = words.size();
  	ResultSet rs = null;
  	Connection connection = null;
  	Statement statement = null; 
		float rank=0;
		
		   connection = JDBCMySQLConnection.getConnection();
		
		for (String object: words) {
		   // System.out.println(object);
			//object="bombs";
			String result = object.replaceAll("[\"-+.^:,]","");
		    String query="SELECT Rank FROM "+Db_Name+".word WHERE Word=\""+result+"\"";
		   try{
			
	           statement = connection.createStatement();
	           rs = statement.executeQuery(query);
		    //Statement st=con.createStatement();
			//ResultSet re=st.executeQuery(query);
	           // Test if there is value 
				if(rs.next())
				{
					rank=rank+Float.parseFloat(rs.getString(1));
				}
				else
				{
					//String word= stemWord(object);
					rank=rank+calcualteStemedQuestionRank(stemWord(result),connection);
				}
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		}
		return rank/ratio;	
	}
    /*
    public static void mysqlTest(){
	    	ResultSet rs = null;
	    	Connection connection = null;
	    	Statement statement = null; 
    	  String query = "SELECT * FROM gdus.category";
    	  try {           
              connection = JDBCMySQLConnection.getConnection();
              statement = connection.createStatement();
              rs = statement.executeQuery(query);
               
              while (rs.next()) {
            	  System.out.println("RANK IS: "+rs.getString("Category"));
              //    employee = new Employee();
               //   employee.setEmpId(rs.getInt("emp_id"));
                //  employee.setEmpName(rs.getString("emp_name"));
                //  employee.setDob(rs.getDate("dob"));
                //  employee.setSalary(rs.getDouble("salary"));
                //  employee.setDeptId((rs.getInt("dept_id")));
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
    }
    */
    
  
  /**
   * public static method that fills wikidata with resources related to the words in each category
   * @param non
   * @return non
   */
  public static float fillWikiData()
	{
		int counter=28992;
		Rdf_Querier r1=new Rdf_Querier();
		//String Result=r1.enquiry(RDFQuestion,"http://dbpedia.org/sparql");
		List<String> ResList;
		Map<Integer, String> map = new HashMap<Integer, String>();
		float ID=0;
		String word;
  	ResultSet rs = null;
  	Connection connection = null;
  	Statement statement = null; 
		String query="SELECT ID,Word FROM gdus.word"; 
		//System.out.println(query);
		   try{
		  //  Statement st=con.createStatement();
	      //  ResultSet re=st.executeQuery(query);
			   connection = JDBCMySQLConnection.getConnection();
	              statement = connection.createStatement();
	              rs = statement.executeQuery(query);
	              while(rs.next())
	              {
						ID=(int)Float.parseFloat(rs.getString(1));
						word=rs.getString(2).substring(0, 1).toUpperCase()+rs.getString(2).substring(1).toLowerCase();
						map.put((int) ID,word);
	              }
	              
	              
				for(int j=0;j<map.size();j++)
				{
					ResList =(List<String>) r1.enquirywiki("SELECT ?item WHERE {{ 	?item ?x \""+map.get(j+1)+"\"@en. 	SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }  }}","https://query.wikidata.org/sparql");
					for (int i=0;i<ResList.size();i++)
					{
						
						int temp=j+1;
						if(temp==125)
						{
							int r=5;
						}
						
						counter=counter+1;
						String query2="INSERT INTO wikidata (wikidata.ID,wikidata.WordID,wikidata.Resource) VALUES ("+counter+","+temp+",'"+ResList.get(i)+"')"; 
						statement.executeUpdate(query2);

					}
				}
	              
		   }
		   catch (SQLException e) {
	              e.printStackTrace();
	          }
		return ID;	
	}
  
  
  
  
  
  
  
    /**
     * public method that returns the current connection when invoked. 
     * @param non
     * @return non
     */
    public static Connection getConnection() {
        return instance.createConnection();
    }
    
    /**
     * public method will return the number of resources in each category from the dataset DBpedia in the database.
     * @param non
     * @return an array with 8 (Military,Nuclear,Terrorism,Weapon,Technology,Security,Harm,Suicide) indices each one will represent a category
     */
    public static float[] malicious_resources_DBpedia() {
    	float[] reslut=new float[8];
        Connection connection = null;
        Statement statement = null; 
        ResultSet rs = null;
        String query1="SELECT COUNT(*) FROM gdus.dbpediadata,gdus.word WHERE gdus.dbpediadata.WordId=gdus.word.ID GROUP BY gdus.word.CategoryID;";
        connection = JDBCMySQLConnection.getConnection();
        try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query1);
			if(rs.next())
				reslut[0]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[1]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[2]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[3]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[4]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[5]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[6]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[7]=Float.parseFloat(rs.getString(1));
        }
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return reslut;
    }
    
    
    /**
     * public method will return the number of resources in each category from the dataset Wikidata in the database.
     * @param non
     * @return an array with 8 (Military,Nuclear,Terrorism,Weapon,Technology,Security,Harm,Suicide) indices each one will represent a category
     */
    public static float[] malicious_resources_WIKIData() {
    	float[] reslut=new float[8];
        Connection connection = null;
        Statement statement = null; 
        ResultSet rs = null;
        String query1="SELECT COUNT(*) FROM gdus.wikidata,gdus.word WHERE gdus.wikidata.WordId=gdus.word.ID GROUP BY gdus.word.CategoryID;";
        connection = JDBCMySQLConnection.getConnection();
        try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query1);
			if(rs.next())
				reslut[0]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[1]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[2]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[3]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[4]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[5]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[6]=Float.parseFloat(rs.getString(1));
			if(rs.next())
				reslut[7]=Float.parseFloat(rs.getString(1));
        }
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return reslut;
    }
    
    /**
     * public method that returns the category of the resource from Wikidata. 
     * @param the Id of the resource
     * @return the category of the Id given
     */
    public static String category_name_WIKIData(int ID) {
    	Connection connection = null;
    	int category_int=0;
        Statement statement = null; 
        ResultSet rs = null;
        String query1="SELECT CategoryID FROM gdus.wikidata,gdus.word WHERE gdus.wikidata.ID="+ID+" AND gdus.wikidata.WordId=gdus.word.ID";
        connection = JDBCMySQLConnection.getConnection();
        try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query1);
			
			
			if(rs.next())
				category_int=(int) Float.parseFloat(rs.getString(1));
        }
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        String category;
        switch (category_int) {
            case 1:  category = "Military";
                     break;
            case 2:  category = "Nuclear";
                     break;
            case 3:  category = "Terrorism";
                     break;
            case 4:  category = "Weapon";
                     break;
            case 5:  category = "Technology";
                     break;
            case 6:  category = "Security";
                     break;
            case 7:  category = "Harm";
                     break;
            case 8:  category = "Suicide";
                     break;
            default: category = "Invalid month";
                     break;
        }
        return category;
    }
    
    /**
     * public method that returns the category of the resource from DBpedia. 
     * @param the Id of the resource
     * @return the category of the Id given
     */
    public static String category_name_DBPedia(int ID) {
    	Connection connection = null;
    	int category_int=0;
        Statement statement = null; 
        ResultSet rs = null;
        String query1="SELECT CategoryID FROM gdus.dbpediadata,gdus.word WHERE gdus.dbpediadata.ID="+ID+" AND gdus.dbpediadata.WordId=gdus.word.ID";
        connection = JDBCMySQLConnection.getConnection();
        try {
			statement = connection.createStatement();
			rs = statement.executeQuery(query1);
			
			
			if(rs.next())
				category_int=(int) Float.parseFloat(rs.getString(1));
        }
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        String category;
        switch (category_int) {
            case 1:  category = "Military";
                     break;
            case 2:  category = "Nuclear";
                     break;
            case 3:  category = "Terrorism";
                     break;
            case 4:  category = "Weapon";
                     break;
            case 5:  category = "Technology";
                     break;
            case 6:  category = "Security";
                     break;
            case 7:  category = "Harm";
                     break;
            case 8:  category = "Suicide";
                     break;
            default: category = "Invalid month";
                     break;
        }
        return category;
    }
}
//server