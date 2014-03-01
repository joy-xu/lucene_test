package com.iie.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerUtil {
	
	public static void displayToken(String str, Analyzer a) {
		TokenStream stream = a.tokenStream("content", new StringReader(str));
		//创建一个属性，这个属性会添加到流中，随着这个TokenStream的增加而移动
		CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
		try {
			while(stream.incrementToken()) {
				System.out.print("["+cta+"]");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		
	}
}
