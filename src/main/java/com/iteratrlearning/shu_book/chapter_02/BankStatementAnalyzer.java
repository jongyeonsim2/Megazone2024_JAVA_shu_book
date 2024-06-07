package com.iteratrlearning.shu_book.chapter_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

public class BankStatementAnalyzer {
    private static final String RESOURCES = "src/main/resource/";

    // CSV 파일 파싱에 필요한 책임을 활용. => CSV parser
    // 현재는 CSV 이지만, 향후에 다른 파서가 나오면, 그냥 갈아끼워서 사용할 수 있도록 되어 있음.
    // analyze() 는 약한 결합 상태. 파싱과 관련해서는 신경을 쓰지 않아도 됨. => 나는 책임 없음.
    // => SRP가 적용됨.
    public void analyze(final String fileName, final BankStatementParser bankStatementParser) throws IOException {

        final Path path = Paths.get(RESOURCES + fileName);
        final List<String> lines = Files.readAllLines(path);

        // 파싱 책임을 위임. 
        // 파싱 완료되면 Domain Entity List 반환됨. => 입출금 내역 정보
        final List<BankTransaction> bankTransactions = bankStatementParser.parseLinesFrom(lines);

        // Domain Entity List 를 활용. => Domain Entity 를 통해서 재사용성이 높아짐.
        // 보고서 작성에 필요한 통계처리 책임을 지고 있음. => 위임 처리
        final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);

        // 입출금 내역 정보를 활용 해서 보고서 작성. => my responsibility
        // SRP 가 적용이 됨. => 보고서 작성
        collectSummary(bankStatementProcessor);

    }

    // 총액 계산은 모두 위침 처리함.
    // 하지만, 보고서 작성은 자기가 함. 
    //  => SRP 가 적용됨. => 자기 책임인 리포트 작성만 책임을 지고 있음.
    // 					보고서 작성 이외의 책임은 모두 위임하고 있음.
    private static void collectSummary(final BankStatementProcessor bankStatementProcessor) {

        System.out.println("The total for all transactions is "
                + bankStatementProcessor.calculateTotalAmount());
        System.out.println("The total for transactions in January is "
                + bankStatementProcessor.calculateTotalInMonth(Month.JANUARY));
        System.out.println("The total for transactions in February is "
                + bankStatementProcessor.calculateTotalInMonth(Month.FEBRUARY));
        System.out.println("The total salary received is "
                + bankStatementProcessor.calculateTotalForCategory("Salary"));
    }
}
