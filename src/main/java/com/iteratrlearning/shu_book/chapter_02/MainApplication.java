package com.iteratrlearning.shu_book.chapter_02;

/**
 * 애플리케이션의 기능
 * - bank-data-simple.csv 의 내용을 읽어서, 고객이 원하는 정보를 리포트로 제공.
 * 
 * 
 * 
 * 현재 개발된 애플리케이션의 문제점.
 * - 1.
 * 		하나의 메소드에 관계가 없는 많은 기능이 구현되어 있음. ( 낮은 응집도 )
 * 		향후 각 기능에 대해 신규 요구사항 및 기능 확장시 현재의 메소드의 코드가
 * 		계속 증가되게 됨.
 * 
 * 		따라서, 향후 기능 파악과 구분이 힘들어지게 됨. 유지보수가 어려워지게 됨.
 * 
 * - 2.
 * 		현재는 CSV 형식의 파일만 가능함. CSV 형식의 전용 프로그램임.( 확장성 )
 * 		다양한 형식의 파일에 대한 대응을 할 수 없게 되어 있음.
 * 		새로운 형식의 파일에 대한 요구사항이 발생하게될 가능성이 매우 높음.
 * 
 * - 3. 
 * 		CSV 로 추출된 정보의 재사용, 사용 편리성, 유지 보수성 등이 낮음.
 * 
 * - 4.
 * 		3번과 연결됨.
 * 		CSV 파일의 칼럼 위치 변경에 대한 대응이 쉽지 않음.
 * 		따라서, 4번의 문제점을 고려해서, 3번에서 함께 고민하면 좋음.
 * 
 * 
 * 
 * 과거의 해결 이력
 * - ver 1
 *   BankStatementAnalyzerSimple.java 시작
 *   요구사항 접수 -> 수정 및 반영
 *   
 * - ver 2
 *   BankStatementAnalyzerProblematic.java
 *   수정을 생각없이 복사 & 붙이기
 *   
 * - ver 3
 *   BankStatementAnalyzerSRP.java
 *   무늬만 SRP 임.
 *   각각의 기능 각각 책임지도록 메소드로 분리함.
 *   메소드별로 책임을 지고 있음. => 클래스별로 책임을 지도록 수정해야 함.
 *   			무늬만 SRP => SRP 스러워짐. ( 리팩토링 )			 
 *   
 * - ver 4
 *   MainApplication.java
 *   많이 SRP 스러워짐.
 *   
 *   각각의 책임(기능)이 class 별로 나누어짐. => 응집도가 높아짐.
 *   
 *    
 *   
 * 
 * 
 * 문제점의 해결 방향
 * 1. SOLID 의 SRP(단일 책임 원칙 : single responsibility principle)
 * 	- 한 클래스는 한 기능만 책임(기능)을 진다.
 * 	- 클래스가 변경 및 수정되어야 하는 이유는 오직 하나여야 함.
 *  - 응집도가 높아지고, 결합도를 낮추기를 원하지만, 결합도는 다음에....
 * 
 * 
 */

public class MainApplication {

    public static void main(String[] args) throws Exception {

    	// 기능이 메소드에서 클래스로 변화된 것이 감지됨.
    	// ********** 입출금 내역을 분석하는 책임을 지고 있음. **********
    	// SOLID 의 SRP(단일 책임 원칙) 가 적용되어 보임.
        final BankStatementAnalyzer bankStatementAnalyzer
                = new BankStatementAnalyzer();

        // 유연한 코드 작성처럼 보여짐.
        // BankStatementParser 는 인터페이스니까...
        // 구현체를 확인할 필요가 있음.
        
        // ********** CSV 파일 파싱 하는 책임을 지고 있음. **********
        // SOLID 의 SRP(단일 책임 원칙) 가 적용되어 보임.
        final BankStatementParser bankStatementParser
                = new BankStatementCSVParser();

        // CSV 파일 처리와 관련된 각각의 책임 설정 => 설정된 각각의 책임을 활용.
        // CSV 파일 파싱에 필요한 parser 를 지정. 
        bankStatementAnalyzer.analyze("bank-data-simple.csv", bankStatementParser);

    }
}
