package com.iie.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class TestAnalyzerUtil {
	
	@Test
	public void testDisplayToken() {
		Analyzer a1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer a2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer a3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer a4 = new WhitespaceAnalyzer(Version.LUCENE_35);
		String txt = "this is my house,I'm come from yunnang zhaotong," +
				"My email is ynkonghao@gmail.com,My QQ is 64831031";
		
		AnalyzerUtil.displayToken(txt, a1);
		AnalyzerUtil.displayToken(txt, a2);
		AnalyzerUtil.displayToken(txt, a3);
		AnalyzerUtil.displayToken(txt, a4);
	}
	
	@Test
	public void test02() {
		Analyzer a1 = new StandardAnalyzer(Version.LUCENE_35);
		Analyzer a2 = new StopAnalyzer(Version.LUCENE_35);
		Analyzer a3 = new SimpleAnalyzer(Version.LUCENE_35);
		Analyzer a4 = new WhitespaceAnalyzer(Version.LUCENE_35);
		//Analyzer a5 = new MMSegAnalyzer(new File("D:\\tools\\javaTools\\lucene\\mmseg4j-1.8.5\\data"));
		String txt = "我来自中国云南昭通昭阳区师专";
		
		AnalyzerUtil.displayToken(txt, a1);
		AnalyzerUtil.displayToken(txt, a2);
		AnalyzerUtil.displayToken(txt, a3);
		AnalyzerUtil.displayToken(txt, a4);
		//AnalyzerUtil.displayToken(txt, a5);
	}
}
