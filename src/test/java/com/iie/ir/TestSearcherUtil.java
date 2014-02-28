package com.iie.ir;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestSearcherUtil {

	private SearcherUtil su;

	@Before
	public void init() throws Exception {
		su = new SearcherUtil();
	}

	@Ignore
	public void testSearchByTerm() throws Exception {
		su.searchByTerm("content", "i", 3);
	}

	@Test
	public void testSearchByTermRange() throws Exception {
		// 查询name字母在"a"和"s"之间的
		su.searchByTermRange("name", "a", "s", 10);
		// 由于attaches是数字类型，使用TermRange无法查询
		su.searchByTermRange("attaches", "2", "5", 10);
	}

	@Test
	public void testSearchByNumericRange() throws Exception {

		su.searchByNumericRange("attach", 2, 10, 10);
	}

	@Test
	public void testSearchByPrefix() throws Exception {

		su.searchByPrefix("content", "s", 10);
	}

	@Test
	public void testSearchByWildCard() throws Exception {
		// 匹配@itat.org结尾的所有字符
		su.searchByWildCard("email", "*@itat.org", 10);
		// 匹配j开头的有三个字符的name
		su.searchByWildCard("name", "j???", 10);
	}

	@Test
	public void testSearchByBoolean() throws Exception {
		su.searchByBoolean(10);	
	}
	
	@Test
	public void testSearchByQueryParse() throws Exception {
		//1、创建QueryParser对象,默认搜索域为content
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		Query query = null;
		
		//query = parser.parse("like");//有like的
		
		//parser.setDefaultOperator(Operator.AND);
		//query = parser.parse("i swim");//有i或者football的，空格默认就是OR
		//可以通过parser.setDefaultOperator(Operator.AND)修改空格的默认设置
		
		//query = parser.parse("i  AND football");//有i并且有football的
		
		//改变搜索域为name为mike
		//query = parser.parse("name:mike");
		
		//同样可以使用*和?来进行通配符匹配
		//query = parser.parse("name:j*");
		
		//开启第一个字符的通配符匹配，默认关闭因为效率不高
		//parser.setAllowLeadingWildcard(true);
		//query = parser.parse("email:*@itat.org");
		//通配符默认不能放在首位,可以通过parser.setAllowLeadingWildcard(true);设置
		
		//匹配name中没有mike但是content中必须有football的，+和-要放置到域说明前面
		//query = parser.parse("- name:mike + content:football");
			
		//匹配一个区间，注意:TO必须是大写
		//query = parser.parse("id:[1 TO 3]");//闭区间
		//query = parser.parse("id:{1 TO 3}");//开区间,只会匹配到2
		
		//完全匹配i like football的
		//query = parser.parse("\"I like football\"");
		
		//匹配I 和football之间有一个单词距离的
		//query = parser.parse("\"I football\"~1");
		
		//模糊查询
		//query = parser.parse("name:make~");
		
		//没有办法匹配数字范围(需要自己扩展Parser)
		//query = parser.parse("attach:[2 TO 10]");
		
		su.searchByQueryParse(query, 10);
	}

}
