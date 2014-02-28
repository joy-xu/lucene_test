package com.iie.ir;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearcherUtil {

	private Directory directory;
	private IndexReader reader;
	private String[] ids = { "1", "2", "3", "4", "5", "6" };
	private String[] emails = { "aa@itat.org", "bb@itat.org", "cc@cc.org",
			"dd@sina.org", "ee@zttc.edu", "ff@itat.org" };
	private String[] contents = { "welcome to visited the space,I like book",
			"hello boy, I like pingpeng ball", "my name is cc I like game",
			"I like football", "I like football and I like basketball too",
			"I like movie and swim" };
	private Date[] dates = null;
	private int[] attachs = { 2, 3, 1, 4, 5, 5 };
	private String[] names = { "zhangsan", "lisi", "john", "jetty", "mike",
			"jake" };
	private Map<String, Float> scores = new HashMap<String, Float>();

	public SearcherUtil() throws Exception {
		directory = FSDirectory.open(new File("d:/lucene/index03"));
		setDates();
		scores.put("itat.org", 2.0f);
		scores.put("zttc.edu", 1.5f);
		index();
	}

	private void setDates() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dates = new Date[ids.length];
		dates[0] = sdf.parse("2010-02-19");
		dates[1] = sdf.parse("2012-01-11");
		dates[2] = sdf.parse("2011-09-19");
		dates[3] = sdf.parse("2010-12-22");
		dates[4] = sdf.parse("2012-01-01");
		dates[5] = sdf.parse("2011-05-19");
	}

	public void index() throws Exception {
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,
				new StandardAnalyzer(Version.LUCENE_35));
		IndexWriter writer = new IndexWriter(directory, iwc);
		writer.deleteAll();
		Document doc = null;
		for (int i = 0; i < ids.length; i++) {
			doc = new Document();
			doc.add(new Field("id", ids[i], Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			doc.add(new Field("email", emails[i], Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("email", "test" + i + "@test.com",
					Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("content", contents[i], Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("name", names[i], Field.Store.YES,
					Field.Index.NOT_ANALYZED_NO_NORMS));
			// 存储数字
			doc.add(new NumericField("attach", Field.Store.YES, true)
					.setIntValue(attachs[i]));
			// 存储日期
			doc.add(new NumericField("date", Field.Store.YES, true)
					.setLongValue(dates[i].getTime()));
			String et = emails[i].substring(emails[i].lastIndexOf("@") + 1);
			if (scores.containsKey(et)) {
				doc.setBoost(scores.get(et));// 默认为1
			} else {
				doc.setBoost(0.5f);
			}
			writer.addDocument(doc);
		}
		if (writer != null)
			writer.close();
	}

	public IndexSearcher getSearcher() throws Exception {
		if (reader == null) {
			reader = IndexReader.open(directory);
		} else {
			IndexReader tr = IndexReader.openIfChanged(reader, true);
			if (tr != null) {
				reader.close();
				reader = tr;
			}
		}
		return new IndexSearcher(reader);
	}

	public void searchByTerm(String field, String text, int num)
			throws Exception {
		IndexSearcher searcher = getSearcher();
		Query query = new TermQuery(new Term(field, text));
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了:" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	public void searchByTermRange(String field, String low, String high, int num)
			throws Exception {
		IndexSearcher searcher = getSearcher();
		Query query = new TermRangeQuery(field, low, high, true, true);
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了:" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	public void searchByNumericRange(String field, int low, int high, int num)
			throws Exception {
		IndexSearcher searcher = getSearcher();
		Query query = NumericRangeQuery.newIntRange(field, low, high, true,
				true);
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	public void searchByPrefix(String field, String prefix, int num)
			throws Exception {
		IndexSearcher searcher = getSearcher();
		Query query = new PrefixQuery(new Term(field, prefix));
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	// 通配符搜索
	// 在传入的value中可以使用通配符:?和*,?表示匹配一个字符，*表示匹配任意多个字符
	public void searchByWildCard(String field, String reg, int num)
			throws Exception {
		IndexSearcher searcher = getSearcher();
		Query query = new WildcardQuery(new Term(field, reg));
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	public void searchByBoolean(int num) throws Exception {
		IndexSearcher searcher = getSearcher();
		BooleanQuery query = new BooleanQuery();
		/*
		 * BooleanQuery可以连接多个子查询 Occur.MUST表示必须出现 Occur.SHOULD表示可以出现
		 * Occur.MUSE_NOT表示不能出现
		 */
		query.add(new TermQuery(new Term("name", "zhangsan")), Occur.MUST);
		query.add(new TermQuery(new Term("content", "like")), Occur.MUST);
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	// 短语查询,如果slop跳到结尾没跳完，slop会从开始接着跳完
	public void searchByPhrase(int num) throws Exception {

		IndexSearcher searcher = getSearcher();
		PhraseQuery query = new PhraseQuery();
		query.setSlop(1);
		query.add(new Term("content", "i"));// 第一个Term
		query.add(new Term("content", "pingpeng"));// 产生距离之后的第二个Term
		// query.add(new Term("content","football"));
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了:" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();

	}

	//模糊查询
	public void searchByFuzzy(int num) throws Exception {

		IndexSearcher searcher = getSearcher();
		FuzzyQuery query = new FuzzyQuery(new Term("name", "mase"), 0.4f, 0);
		System.out.println(query.getPrefixLength());
		System.out.println(query.getMinSimilarity());
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了:" + tds.totalHits);
		for (ScoreDoc sd : tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id") + "---->" + doc.get("name") + "["
					+ doc.get("email") + "]-->" + doc.get("id") + ","
					+ doc.get("attach") + "," + doc.get("date"));
		}
		searcher.close();
	}

	//字符串查询
	public void searchByQueryParse(Query query, int num) throws Exception {
		IndexSearcher searcher = getSearcher();
		TopDocs tds = searcher.search(query, num);
		System.out.println("一共查询了:"+tds.totalHits);
		for(ScoreDoc sd:tds.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			System.out.println(doc.get("id")+"---->"+
					doc.get("name")+"["+doc.get("email")+"]-->"+doc.get("id")+","+
					doc.get("attach")+","+doc.get("date")+"=="+sd.score);
		}
		searcher.close();
	}
	
	
	public static void main(String[] args) throws Exception {
		new SearcherUtil().searchByTermRange("id", "1", "3", 10);
	}

}
