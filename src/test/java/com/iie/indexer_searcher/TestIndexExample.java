package com.iie.indexer_searcher;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

import com.iie.indexer_searcher.IndexExample;

public class TestIndexExample {
	
	@Test
	public void testIndexer() throws IOException {
		IndexExample ie = new IndexExample();
		ie.indexer();
	}
	
	@Test
	public void testDel() throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexExample ie = new IndexExample();
		ie.delIndexer();
	}
}
