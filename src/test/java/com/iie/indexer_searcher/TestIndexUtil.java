package com.iie.indexer_searcher;

import org.junit.Ignore;
import org.junit.Test;

import com.iie.indexer_searcher.IndexUtil;

public class TestIndexUtil {
	
	@Test
	public void testIndex() {
		new IndexUtil().index();
	}
	
	@Test
	public void testDelete() {
		new IndexUtil().delete();
	}
	
	@Test
	public void testSearch() {
		new IndexUtil().search02();
	}
}
