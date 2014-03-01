package com.iie.indexer_searcher;

import org.junit.Test;

import com.iie.indexer_searcher.FileIndexUtil;

public class TestFileIndex {
	
	@Test
	public void testIndexer() {
		FileIndexUtil.index(true);
	}
}
