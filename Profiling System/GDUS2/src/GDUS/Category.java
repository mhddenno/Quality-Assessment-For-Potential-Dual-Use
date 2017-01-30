package GDUS;

import java.util.*;

public class Category
{

		public String _Category;
		public ArrayList<String> _synonyms;
		public ArrayList<String> _relatedWords;
		/**
		   * public constructor for the class Category, it sets the category name and create a list for synonyms.
		   * @param string of CategoryName
		   */
		public Category(String CategoryName)
		{
			_Category = CategoryName;
			_synonyms = new ArrayList<String>();
			_relatedWords = new ArrayList<String>();
		}

}