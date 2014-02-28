package com.iie.ir;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Ignore;
import org.junit.Test;

public class TestHelloLucene {
	
	@Test
	public void testIndexer() throws CorruptIndexException, LockObtainFailedException, IOException {
		HelloLucene hl = new HelloLucene();
		hl.indexer();
	}
	
	@Test
	public void testSearcher() throws IOException, ParseException {
		HelloLucene hl = new HelloLucene();
		hl.searcher();
	}
}
