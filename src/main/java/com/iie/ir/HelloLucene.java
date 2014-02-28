package com.iie.ir;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;




import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReader.FieldOption;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class HelloLucene {
	/**
	 * 建立索引
	 * @throws IOException 
	 * @throws LockObtainFailedException 
	 * @throws CorruptIndexException 
	 */
	public void indexer() throws CorruptIndexException, LockObtainFailedException, IOException {
		
		//1、创建Directory
		//Directory directory = new RAMDirectory();//创建内存索引
		Directory dir = FSDirectory.open(new File("D:/lucene/index01"));//创建在硬盘上
		//2、创建IndexWriter
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
		IndexWriter writer = new IndexWriter(dir, iwc);
		
		//3、创建Document对象
		Document doc = new Document();
		//4、为Document添加Field(Document和Field的关系相当于数据库中表跟字段的关系)
		File f = new File("D:/lucene/example");
		for (File file : f.listFiles()) {
			doc.add(new Field("content", new FileReader(file)));
			//或者
			//String content = FileUtils.readFileToString(file);
			//doc.add(new Field("content", content, Field.Store.NO, Index.ANALYZED));
			doc.add(new Field("filename", file.getName(), Field.Store.YES, Index.NOT_ANALYZED));
			doc.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Index.NOT_ANALYZED));
		}
		//5、通过IndexWriter添加文档到索引中
		writer.addDocument(doc);
		if(writer != null) {
			writer.close();
		}
	}
	/**
	 * 搜索
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void searcher() throws IOException, ParseException {
		//1、创建Directory
		Directory dir = FSDirectory.open(new File("D:/lucene/index01"));//创建在硬盘上
		//2、创建IndexReader,读取索引
		IndexReader reader = IndexReader.open(dir);
		//3、根据IndexReader创建IndexSearcher
		IndexSearcher searcher = new IndexSearcher(reader);
		//4、创建搜索的Query
		//创建parser来确定要搜索文件的内容，第二个参数表示搜索的域
		QueryParser parser = new QueryParser(Version.LUCENE_35, "content", new StandardAnalyzer(Version.LUCENE_35));
		//创建query，表示搜索域为content中包含java的文档
		Query query = parser.parse("java");//搜索内容包含“java”的文档
		//5、根据searcher搜索并且返回TopDocs
		TopDocs tds = searcher.search(query, 10);
		//6、根据TopDocs获取ScoreDoc对象
		ScoreDoc[] scoreDocs = tds.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			//7、根据seacher和ScoreDoc对象获取具体的Document对象
			Document document = searcher.doc(scoreDoc.doc);
			//8、根据Document对象获取需要的值
			System.out.println(document.get("filename") + "[" + document.get("path") + "]");
			System.out.println(document.get("content"));//返回null，因为没有存储，但是索引了
		}
		//9、关闭reader
		if(reader != null) {
			reader.close();
		}
		
	}
	
	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
		new HelloLucene().indexer();
		new HelloLucene().searcher();
	}

}
