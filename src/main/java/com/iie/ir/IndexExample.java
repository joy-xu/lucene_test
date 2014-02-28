package com.iie.ir;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class IndexExample {
	
	private Directory dir = null;
	private Document doc = null;
	
	public IndexExample() {
		try {
			dir = FSDirectory.open(new File("d:/lucene/index02"));
			doc = new Document();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void indexer() throws IOException {
		//Directory dir = FSDirectory.open(new File("d:/lucene/index02"));
		IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		doc = new Document();
		File path = new  File("d:/lucene/example");
		for(File file : path.listFiles()) {
			doc.add(new Field("content", new FileReader(file)));
		}
		writer.addDocument(doc);
		if(writer != null) {
			writer.close();
		}
	}
	
	public void delIndexer() throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
		writer.forceMergeDeletes();
		System.out.println("删除");
;		if(writer != null) {
			writer.close();
		}
	}
	
}
