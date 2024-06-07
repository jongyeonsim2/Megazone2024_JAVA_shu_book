package com.iteratrlearning.shu_book.chapter_02;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class BankStatementAnalyzerSimple {

    private static final String RESOURCES = "src/main/resource/";

    /**
     * 입출금 금액의 합산 기능을 구현.
     * 
     */
    
    public static void main(final String[] args) throws Exception {
    		// 파일 접근
            //final Path path = Paths.get(RESOURCES + "bank-data-simple.csv");
            
    		// java BankStatementAnalyzerSimple bank-data-simple.csv
            final Path path = Paths.get(RESOURCES + args[0]);
            
            // 파일 내용 읽음
            final List<String> lines = Files.readAllLines(path);
            
            // **************** 파일 읽기 *****************
            
            double total = 0;
            // 첫 행 ~ 마지막 행의 2번째 칼럼의 금액 정보 추출 후 누계
            for(final String line: lines) {
            	// "," 를 기준으로 token 을 추출
                String[] columns = line.split(",");
                // 두 번째 token(입출금 금액정보) 을 더하기 위한 자료형으로 변환
                double amount = Double.parseDouble(columns[1]);
                
                
                // ****************** 파싱 *********************
                
                // 누계
                total += amount;
                
                // ***************** 데이터 처리 *****************
            }

            // ***************** 보고서 작성 ***************
            System.out.println("The total for all transactions is " + total);
    }
}

