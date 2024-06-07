package com.iteratrlearning.shu_book.chapter_02;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

// CSV 파일의 파싱을 책임지는 클래스
// BankStatementParser IF 를 활용해수 구현. => 약한 결합.
//    향후에 XML 파서가 필요해지면, XML 파서를 새롭게 만들어서 사용하면 됨.
//    즉, 현재의 BankStatementCSVParser 에는 전혀 영향을 받지 않음. => SRP 가 적용.
public class BankStatementCSVParser implements BankStatementParser {

    private static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // CSV 한 행을 파싱 => Domain Entity 생성.
    // 1. 재사용성이 높아짐.
    //		Domain Entity 로 생성.
    // 2. 메소드를 자기의 다른 곳에서 재활용.
    //      응집도가 높아졌다고 볼 수 있음.
    // 3. 메소드적으로 SRP 가 적용이 됨.
    //		
    public BankTransaction parseFrom(final String line) {
    	// "," 를 기준으로 칼럼을 분리
        final String[] columns = line.split(",");

        // 필요한 자료형으로 파싱
        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final double amount = Double.parseDouble(columns[1]);

        // 한 행의 파싱이 완료된 후 Domain Entity 로 생성.
        return new BankTransaction(date, amount, columns[2]);
    }

    // 파일의 모든 데이터(모든 행)를 매개변수로 받아서,
    // 
    public List<BankTransaction> parseLinesFrom(final List<String> lines) {
        
    	// this::parseFrom => 메소드 참조 => 람다식을 더 줄인 형태. => 메소드
    	// 자기 자신의 메소드를 재사용하고 있음. => 응집도가 높아진 상태임.
    	return lines.stream().map(this::parseFrom).collect(toList());
    }
}
