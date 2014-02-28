package com.iie.ccf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestIndex {
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new FileReader("d:/lucene/example/sample-social"));
		for(int i=0; i<1; i++) {
			String html = bf.readLine();
			System.out.println(html);
			Document doc = Jsoup.parse(html);
			System.out.println(doc);
		}
	}
	
}
