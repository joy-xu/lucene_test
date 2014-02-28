package com.iie.ir;

import org.junit.Ignore;
import org.junit.Test;

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
