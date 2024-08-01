package com.iteratrlearning.shu_book.chapter_06.web_adapter;

import java.io.BufferedReader;
import java.io.FileReader;

/*
 * 문자기반의 보조스트림 사용하는 예제.
 * 
 * BufferedReader 사용 예제.
 * 
 * file 을 읽어서, 처리하는데,
 * 보조스트림을 이용하고, 읽은 데이터에 대해서는 콘솔에 출력.
 * 
 */
public class BufferedReaderEx {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			// file 을 읽음. 
			FileReader fr = new FileReader("C:\\DEV\\eclipse_workspace\\shu_book\\src\\main\\java\\com\\iteratrlearning\\shu_book\\chapter_06\\web_adapter\\BufferedReaderEx.java");
			
			// 보조스트림을 연결해서 읽음.
			BufferedReader br = new BufferedReader(fr);
			
			String line = "";
			// 파일이 끝날때까지 읽도록 처리.
			
			// line 단위로 읽은 데이터를 콘솔에 출력
			// BufferedReader 는 문자 기반 보조스트림으로,
			// 라인 단위로 데이터를 읽을 수 있도록 메소드를 지원.
			for (int i = 1; (line = br.readLine()) != null; i++ ) {
				System.out.println(i + " : " + line);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}