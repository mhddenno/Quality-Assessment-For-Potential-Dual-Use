package GDUS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class Program
{
	 private static ArrayList<String> Synonyms;
	 public static ArrayList<String> getSynonyms()
	 {
		 return Synonyms;
	 }
	 public static void setSynonyms(ArrayList<String> value)
	 {
		 Synonyms = value;
	 }
	//static public List<String> Antonyms { get; set; }
	private static ArrayList<String> RelatedWords;
	public static ArrayList<String> getRelatedWords()
	{
		return RelatedWords;
	}
	public static void setRelatedWords(ArrayList<String> value)
	{
		RelatedWords = value;
	}
	public static ArrayList<Category> _Categories;
	static void main(String[] args) throws IOException, SQLException
	{

		_Categories = ReadFromFile2("C:\\Users\\Ola\\Desktop\\sample of synonem tree.txt");
		//writeInXML();
		//InsertInDB2(_Categories);
		Sentiment(_Categories);
		System.out.println("done");
		//Thesaurus(englishWord);
		//MainWindow.tbDicOutput.Text = word + ": " + englishWord;


	}
	/**
	   * public static method that receives a list with categories and their related words, 
	   * find the rank of each word using uClassify api 
	   * and fill the rank column in Word table in the database.
	   * @param list of categories and their related words
	   */
	public static void Sentiment(ArrayList<Category> cat) throws IOException, SQLException
	{
		String _apiKey = "rY2l6vlozHsS";
		for (Category category : cat)
		{
			for (String synonym : category._synonyms)
			{
				try
				{
					String link = "https://api.uclassify.com/v1/uClassify/Sentiment/classify/?readKey=rY2l6vlozHsS&text=" + synonym;
					URL oracle = new URL(link);
			        URLConnection yc = oracle.openConnection();
			        BufferedReader in = new BufferedReader(new InputStreamReader(
			                                    yc.getInputStream()));
			        String response;
			        while ((response = in.readLine()) != null) 
			            System.out.println(response);
					

					//try (java.io.InputStreamReader sr = new java.io.InputStreamReader(response.GetResponseStream()))
					{
						String Rank = response;
						String[] rankings = Rank.split("[,]", -1);
						rankings[0] = tangible.DotNetToJavaStringHelper.remove(rankings[0], 0, 12);
						rankings[1] = tangible.DotNetToJavaStringHelper.remove(rankings[1], 0, 11);
						rankings[1] = tangible.DotNetToJavaStringHelper.remove(rankings[1], rankings[1].length() - 1);
						double maxRank = Math.max(Double.parseDouble(rankings[0]),Double.parseDouble(rankings[1]));
						String connectionString = "Data Source=.\\SQLEXPRESS;AttachDbFilename=\"C:\\Users\\Ola\\Documents\\Visual Studio 2010\\Projects\\GDUS\\GDUS\\GDUS.mdf\";Integrated Security=True;User Instance=True";
						try (Connection con = DriverManager.getConnection( connectionString );)
						{

							Statement stmt = con.createStatement( );
							String SQL = "UPDATE Word SET Rank="+maxRank+" WHERE Word="+synonym;
							ResultSet rs = stmt.executeQuery( SQL );

						}
					}
				}
				catch (RuntimeException e)
				{
					System.out.println(e.getMessage());
					new Scanner(System.in).nextLine();
				}
			}
		}

	}
	/**
	   * public static method that receives the categories and words file path, 
	   * the method will read and fill a list called _categories with them.
	   * @param path of categories and words file
	   */
	private static ArrayList<Category> ReadFromFile2(String path) throws IOException
	{
		
		List<String> readAllLines = Files.readAllLines(Paths.get(path));

		_Categories = new ArrayList<Category>(readAllLines.size());

		int index = 0;
		for (String line : readAllLines)
		{

			String[] temp = line.split("[:]", -1);
			String[] temp2 = temp[1].split("[,]", -1);
			_Categories.add(new Category(temp[0]));
			for (int i = 0; i < temp2.length; i++)
			{
				//Thesaurus(temp2[i]);

				if (!_Categories.get(index)._synonyms.contains(temp2[i]))
				{
					_Categories.get(index)._synonyms.add(temp2[i]);
				}
			}
			index++;
		}
		//Console.WriteLine("Press any key to exit.");
		//System.Console.ReadKey();
		return _Categories;
	}

	
	/**
	   * public static method that receives a list with categories and their related words,
	   * Insert the categories in the categories table 
	   * Insert the words in the words table
	   * then query DBpedia and WikiData for each word
	   * and insert the resources in DBpedia and WikiData resource table
	   * @param list of categories and their related words
	   */
	
	private static void InsertInDB2(ArrayList<Category> cat) throws SQLException, IOException, ParserConfigurationException, SAXException
	{
		String connectionString = "Data Source=.\\SQLEXPRESS;AttachDbFilename=\"C:\\Users\\Ola\\Documents\\Visual Studio 2010\\Projects\\GDUS\\GDUS\\GDUS.mdf\";Integrated Security=True;User Instance=True";
		try (Connection con = DriverManager.getConnection( connectionString );)
		{
			int index = 1;
			int index2 = 1;
			 for (int i = 0; i < _Categories.size(); i++)
			 {
				 Statement stmt = con.createStatement( );
				 String SQL = "INSERT INTO Category (ID, Category) VALUES ("+i + 1+", "+ _Categories.get(i)._Category+")";
				 ResultSet rs = stmt.executeQuery( SQL );
				 for (String Synonym : _Categories.get(i)._synonyms)
				 {
					 Statement stmt1 = con.createStatement( );
					 String SQL1 ="INSERT INTO Word (ID,CategoryID, Word) VALUES ("+index+","+i + 1+", "+Synonym+")";
					 ResultSet rs1 = stmt.executeQuery( SQL1 );

					 SparqlQuery sparql = new SparqlQuery();
					 String upperSyn = FirstLetterToUpper(Synonym);
					 String query = "http://dbpedia-live.openlinksw.com/sparql?default-graph-uri=&query=PREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0D%0APREFIX+dbo%3A+%3Chttp%3A%2F%2Fdbpedia.org%2Fontology%2F%3E%0D%0ASELECT+distinct+%3Fx+WHERE+%7B%7B%0D%0A++++%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F" + upperSyn + "%3E+%3Fy+%3Fx+%0D%0A%7D+union%0D%0A%7B%0D%0A++++%3Fx+%3Fy+%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F" + upperSyn + "%3E+%0D%0A%7D%7D%0D%0A%0D%0A%0D%0A%0D%0A&should-sponge=&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=on";
					 String xmlRes = sparql.ExecuteQuery(query);
					 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					 Document doc ;
					 if (!xmlRes.equals(""))
					 {
						 doc = dBuilder.parse(xmlRes);
						 doc.getDocumentElement().normalize();
						 NodeList nList = doc.getElementsByTagName("a");
						 
						 for (int j = 0; j <= nList.getLength() - 1; j++)
						 {
							 Node nNode = nList.item(j);
							 Element eElement = (Element) nNode;
							 String resource = eElement.getNodeValue();
							 if ((resource.contains("dbpedia")) && (!resource.contains("?")))
							 {
								 String SQL2 = "INSERT INTO DBpediaRes (ID, WordId, Resource) VALUES ("+index2+++", "+index+", "+resource+")";
								 Statement stmt2 = con.createStatement( );
								 ResultSet rs2 = stmt.executeQuery( SQL2 );
							 }
							 else
							 {
								 if ((resource.contains("wikidata")) && (!resource.contains("?")))
								 {
									 String SQL3 = "INSERT INTO WikiDataRes (ID, WordID, Resource) VALUES ("+index2+++", "+index+", "+resource+")";
									 Statement stmt3 = con.createStatement( );
									 ResultSet rs3 = stmt.executeQuery( SQL3 );
								 }
							 }
						 }
					 }
					 index++;
				 }

			 }

			//string category = _Categories[i]._Category;

			con.close();
		}
	}
	public static String FirstLetterToUpper(String str)
	{
		if (str == null)
		{
			return null;
		}

		if (str.length() > 1)
		{
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		}

		return str.toUpperCase();
	}

}