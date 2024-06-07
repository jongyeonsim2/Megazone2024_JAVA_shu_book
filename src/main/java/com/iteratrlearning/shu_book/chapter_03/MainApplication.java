package com.iteratrlearning.shu_book.chapter_03;


public class MainApplication {

    public static void main(final String[] args) throws Exception {

        final BankStatementAnalyzer bankStatementAnalyzer
                = new BankStatementAnalyzer();

        final BankStatementParser bankStatementParser
                = new BankStatementCSVParser();

        // 보고서를 html 포맷으로 생성.
        // 향후에 excle 포맷으로 새로운 요구 사항이 발생한다면.....
        // 따라서, 느슨한 결합으로 구현.
        final Exporter exporter = new HtmlExporter();

        // ch2에서는 매개변수가 두 개였으나,
        // 이제는 콘솔에 출력하는게 아니고, html 로 출력해야 함.
        // 따라서, 마지막 매개변수에 HtmlExporter 인스턴스를 전달.
        bankStatementAnalyzer.analyze(
        		"bank-data-simple.csv", 
        		bankStatementParser, 
        		exporter);

    }
}
