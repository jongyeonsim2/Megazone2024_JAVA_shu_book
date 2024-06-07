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
 * 
 * 문제점의 해결 방향
 * 1. SOLID 의 SRP(단일 책임 원칙 : single responsibility principle)
 * 
 * 
 */

public class MainApplication {

    public static void main(String[] args) throws Exception {

        final BankStatementAnalyzer bankStatementAnalyzer
                = new BankStatementAnalyzer();

        final BankStatementParser bankStatementParser
                = new BankStatementCSVParser();

        bankStatementAnalyzer.analyze("bank-data-simple.csv", bankStatementParser);

    }
}
