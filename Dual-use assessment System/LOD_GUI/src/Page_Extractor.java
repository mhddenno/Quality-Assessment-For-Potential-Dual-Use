import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Page_Extractor {

	/**
     * public method that receives a link as a parameter and then scrap it and return the info and text within that link 
     * @param Link to be farmed
     * @return List of strings which are the content of the link sent
     */
	public List <String>  getPageInfo(String Link) throws IOException{

		List<String> result = new ArrayList<String>();
		String html = Jsoup.connect(Link).get().html();
	
		PrintWriter out = new PrintWriter("TESTINGCRAWLING.html");
	
		out.println(html);
		out.close();
				 Document document = Jsoup.parse( new File( "TESTINGCRAWLING.html" ) , "utf-8" );
		 Elements links = document.getElementsByClass("ExpandedAnswer");
		 for (Element link : links) {

				result.add(link.toString());
			}	
		 return result;
	}
	/**
     * public method that receives a link as a parameter and then return the quora answers within that link
     * @param qoura link
     * @return List of strings which are the answers of the link sent
     */
	public List<String> extractAnswers(String link) throws IOException{
		List<String> Res = getPageInfo(link);
		List<String> Answers = this.getAnswers(Res);
		return Answers;
	}
	
	/**
     * public method that receives a link as a parameter and it extracts the answer of the page and rank them according to the profiles
     * @param Link to get the answers from
     * @return Ranking of the answers
     */
	public float extractRank(List<String> Answers) throws IOException{
		//List<String> Res = getPageInfo(link);
		//List<String> Answers = this.getAnswers(Res);
		int answersNum=Answers.size();
		Tokenizer tokenAnswer=new Tokenizer();
		float rank=0;
		if(answersNum!=0)
		{
			if(answersNum==1)
			{
				int Temp=0;
				ArrayList<String> tokens1= tokenAnswer.tokenize(Answers.get(0));
				float rank1=JDBCMySQLConnection.calcualteTheQuestionRank(tokens1);
				rank=rank1;
			}
			else if (answersNum==2){
				ArrayList<String> tokens1= tokenAnswer.tokenize(Answers.get(0));
				float rank1=JDBCMySQLConnection.calcualteTheQuestionRank(tokens1);
			    ArrayList<String> tokens2= tokenAnswer.tokenize(Answers.get(1));
			    float rank2=JDBCMySQLConnection.calcualteTheQuestionRank(tokens2);
			    rank=(rank1+rank2)/2;
			}
			else {
				System.out.print("I am over 3");
				ArrayList<String> tokens1= tokenAnswer.tokenize(Answers.get(0));
				float rank1=JDBCMySQLConnection.calcualteTheQuestionRank(tokens1);
				System.out.print("R rank: "+ rank1);
			    ArrayList<String> tokens2= tokenAnswer.tokenize(Answers.get(1));
			    float rank2=JDBCMySQLConnection.calcualteTheQuestionRank(tokens2);
			    ArrayList<String> tokens3= tokenAnswer.tokenize(Answers.get(2));
			    float rank3=JDBCMySQLConnection.calcualteTheQuestionRank(tokens3);
			    rank=(rank1+rank2+rank3)/3;
			//	System.out.println("Rank1: "+rank1+"Rank2 :"+rank2+"Rank3: "+rank3);
			}
		}		
		return rank*10;
	}
	
	/**
     * public method that receives a list of strings which are content of a quora page, then it process this data and return the answers within this page.
     * @param Content of quora web page as a string
     * @return List of strings which are the answers found in that page
     */
public List <String> getAnswers(List<String> Res){
		
		String listString = "";

		for (String s : Res)
		{
		    listString += s + "\t";
		}
		
		String str = listString;
		String findStr = "ExpandedQText ExpandedAnswer";
		int lastIndex = 0;
		int count = 0;
		
		// get indices of answers 
		List<Integer> answerIndices = new ArrayList<Integer>();
		while(lastIndex != -1){

		    lastIndex = str.indexOf(findStr,lastIndex);
		 //   System.out.println(lastIndex);
		    answerIndices.add(lastIndex);
		    if(lastIndex != -1){
		        count ++;
		        lastIndex += findStr.length();
		    }
		    
		}

		// Getting the answers
		ArrayList<String> singleAddress = new ArrayList<String>();
		int preLastID=0;
		for(int i=0;i<answerIndices.size();i++){
			if(answerIndices.get(i+1)!=-1){
			//System.out.println(answerIndices.get(i));
			singleAddress.add(listString.substring(answerIndices.get(i), answerIndices.get(i+1)));
			}
			else{
				preLastID=answerIndices.get(i);
				break;
			}
		}
		
		// Substract the answers and prepare them for the list which will be returned 
		singleAddress.add(listString.substring(preLastID, listString.length()));
		System.out.println(count+" Answers returned");
		ArrayList<String> answers = new ArrayList<String>();
		for (String ansCon : singleAddress)
		{
			//System.out.println(Jsoup.parse(ansCon).text());
			String answer = Jsoup.parse(ansCon).text().substring(Jsoup.parse(ansCon).text().lastIndexOf(">") + 1);
			System.out.println(answer);
			answers.add(answer);
		}	
		return answers;
	}
}
