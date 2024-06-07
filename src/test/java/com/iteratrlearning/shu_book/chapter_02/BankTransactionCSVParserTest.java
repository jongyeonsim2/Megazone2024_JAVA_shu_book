package com.iteratrlearning.shu_book.chapter_02;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * 테스트 종류 및 범위
 * - 단위 테스트(Unit Test)
 * 		응용 프로그램에서 테스트 가능한 가장 작은 소프트웨어를 실행하여
 * 		예상대로 동작하는지 확인하는 테스트. 코드 구현한 사람이 담당.
 * 
 * - 통합 테스트(Integration Test)
 * 		단위 테스트보다 더 큰 동작을 달성하기 위해 여러 모듈들을 모아
 * 		의도대로 동작하는지 확인하는 테스트.
 * 
 * - 시스템 테스트(System Test)
 * 		통합테스트까지 완료하면, 시스템 전체가 정상적으로 작동하는지를 확인하는 테스트.
 * 
 * - 인수 테스트(Acceptance Test)
 * 		소프트웨어의 인수를 목적으로 하는 테스트임.
 * 		소프트웨어를 인수하기 전에 명세한 요구사항(인수조건) 대로 
 * 		잘 동작하는지 검증하는 테스트.
 * 
 * 		비즈니스 쪽에 초점을 두어서 실시함.
 * 
 * 
 * 테스트 코드 작성( 단위 테스트 )
 * -  Given-When-Then 패턴
 * 	  Given : 테스트를 수행하기 위한 환경을 설정.
 * 	  When : 테스트 목적을 기술. 내가 기 구현한 메소드의 기능에 문제가 없는지를 수행.
 * 			실제 테스트 코드가 포함되고 테스트 결과값을 반환. 
 * 	  Then : 테스트 결과를 검증. 일반적으로 When 에서 반환된 값을 검증함.
 * 
 */


public class BankTransactionCSVParserTest {

    private BankStatementParser statementParser = new BankStatementCSVParser();

    // CSV 파일에서 한행을 파싱 => Domain Entity 로 반환
    // 반환된 Domain Entity의 멤버값이 내가 예상하는 값과 동일한지 검증.
    
    @Test
    public void shouldParseOneCorrectLine() throws Exception {
    	
    	// ********************** given **********************
    	// CSV 파일의 데이터를 사용.
        String line = "30-01-2017,-50,Tesco";
        

        
        // ********************** when **********************
        BankTransaction result = statementParser.parseFrom(line);
        
        
        
        // ********************** then **********************
        BankTransaction expected = new BankTransaction(LocalDate.of(2017, Month.JANUARY, 30), -50, "Tesco");
        Assert.assertEquals(expected.getDate(), result.getDate());
        Assert.assertEquals(expected.getAmount(), result.getAmount(), 0.0d);
        Assert.assertEquals(expected.getDescription(), result.getDescription());
        
        
    }

}
