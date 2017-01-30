import org.apache.jena.rdf.model.ModelFactory;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.HashSet;
import java.util.List ;
import java.util.Map ;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.* ;
import org.apache.jena.rdf.model.* ;

public class Rdf_Querier {
	private String serviceURI;
	private ArrayList resources;
	private String rdfs;
	private String rdf;
	
	/**
     * public constructor that initialize the variables with links and types
     * @param non
     * @return non
     */
	public Rdf_Querier()
	{
	 serviceURI  = "http://dbpedia-live.openlinksw.com/sparql" ;
	 resources = new ArrayList();
	 rdfs="<http://www.w3.org/2000/01/rdf-schema#>";
	 rdf="<http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
	}
	public void change_serviceURL(String new_service)
	{
		serviceURI=new_service;
	}
	/**
     * public method that receives a query and an endpoint and then fire the query against the endpoint of wikidata to return the result of that query 
     * @param Query and serviceURI both as strings
     * @return result of that query
     */
	public  List<String> enquirywiki(String Query,String serviceURI)
	{
	
		/*String queryString = "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                "SELECT DISTINCT ?country\n" +
                "WHERE\n" +
                "{\n" +
                "\t?country wdt:P31 wd:Q3624078 .\n" +
                "    ?country wdt:P1622 wd:Q13196750.\n" +
                "    ?country wdt:P30 wd:Q15\n" +
                "\tFILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}\n" +
                "\tSERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}\n" +
                "ORDER BY ?countryLabel";*/
		String queryString = "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                Query;
		
		Query query = QueryFactory.create(queryString);
		String result="";
		QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", queryString);
        try {
            ResultSet results = qexec.execSelect();
            ResultSet temp=results;
           // ResultSetFormatter.out(System.out, results, query);
            result=ResultSetFormatter.asText(temp);
           // ResultSetFormatter.out(System.out, temp, query);
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }
        
        Matcher matcher = Pattern.compile("<(.*?)>").matcher(result);
        List<String> ResList = new ArrayList();
		while (matcher.find()) {
			ResList.add(matcher.group(1));
		}
		
	return ResList;
    
	}
	
	/**
     * public method that receives a query and an endpoint and then fire the query against the endpoint of dbpedia to return the result of that query 
     * @param Query and serviceURI both as strings
     * @return result of that query
     */
	public List<String> enquirydbpedia(String Query,String serviceURI)
	{
        String queryString = 
        		"PREFIX	dct: <http://purl.org/dc/terms/> \n"+
        	"PREFIX dbp: <http://dbpedia.org/property/> \n"+	
            "SELECT * WHERE { " +
            "    SERVICE <" + serviceURI + "> { " +
            //Your Query Goes Here:		
            Query +
            
            "    }" +
            "}" ;
        
        Query query = QueryFactory.create(queryString) ;
        String Result;
        try(QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Map<String, Map<String,List<String>>> serviceParams = new HashMap<String, Map<String,List<String>>>() ;
            Map<String,List<String>> params = new HashMap<String,List<String>>() ;
            List<String> values = new ArrayList<String>() ;
            values.add("2000") ;
            params.put("timeout", values) ;
            serviceParams.put(serviceURI, params) ;
            qexec.getContext().set(ARQ.serviceParams, serviceParams) ;
            ResultSet rs = qexec.execSelect() ;
           // ResultSetFormatter.out(System.out,  rs, query);
        	Result=ResultSetFormatter.asText(rs);
        	
        }
        Matcher matcher = Pattern.compile("<(.*?)>").matcher(Result);
        List<String> ResList = new ArrayList();
		while (matcher.find()) {
			ResList.add(matcher.group(1));
		}
		
       return ResList;
    
	}
	
	public String enquiry(String Query,String serviceURI)
	{
		//String serviceURI  = "http://dbpedia-live.openlinksw.com/sparql" ;
	//	String serviceURI  = "http://dbpedia.org/sparql" ;
        String queryString = 
            "SELECT * WHERE { " +
            "    SERVICE <" + serviceURI + "> { " +
            //Your Query Goes Here:		
            Query +
            
            "    }" +
            "}" ;
        
        Query query = QueryFactory.create(queryString) ;
        String Result;
        try(QueryExecution qexec = QueryExecutionFactory.create(query, ModelFactory.createDefaultModel())) {
            Map<String, Map<String,List<String>>> serviceParams = new HashMap<String, Map<String,List<String>>>() ;
            Map<String,List<String>> params = new HashMap<String,List<String>>() ;
            List<String> values = new ArrayList<String>() ;
            values.add("2000") ;
            params.put("timeout", values) ;
            serviceParams.put(serviceURI, params) ;
            qexec.getContext().set(ARQ.serviceParams, serviceParams) ;
            ResultSet rs = qexec.execSelect() ;
       
        	Result=ResultSetFormatter.asText(rs);

        }
       return Result;
	}
	public ArrayList<String> resource_enquiry(String predicate,String object,String language)
	{
		 ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
	                "prefix rdfs:    "+rdfs+"\n" +
	                "prefix rdf:    "+rdf+"\n" +
	                "\n" +
	                "select ?resource where {\n" +
	                "  ?resource rdfs:label ?label\n" +
	                "}" ); 
		 	Literal _object= ResourceFactory.createLangLiteral( object, language );
	        qs.setParam( "label", _object );
	        Literal _predicate= ResourceFactory.createPlainLiteral(predicate);
	        qs.setParam( "rdfs:label", _predicate );
	        System.out.println( qs );
	        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia-live.openlinksw.com/sparql", qs.asQuery() );

	        // Normally you'd just do results = exec.execSelect(), but I want to 
	        // use this ResultSet twice, so I'm making a copy of it.  
	        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );
	        while ( results.hasNext() ) {
	            // don't use the `?` in the variable
	            // name here. Use *just* the name of the variable.
	        	resources.add(results.next().get( "resource" ));
	        }
	        // A simpler way of printing the results.
	        //ResultSetFormatter.out( results );
	        //to remove duplicate we use Set
	        Set<String> hs = new HashSet<String>();
	        hs.addAll(resources);
	        resources.clear();
	        resources.addAll(hs);
		
		return resources;
	}
	
	
	
	//we use thttps://www.ebi.ac.uk/rdf/querying-sparql-javahose
	//
	//http://stackoverflow.com/questions/24116853/query-sparql-to-dbpedia-using-java-code

}

