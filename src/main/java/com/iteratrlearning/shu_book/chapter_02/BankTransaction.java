package com.iteratrlearning.shu_book.chapter_02;

import java.time.LocalDate;
import java.util.Objects;

// Domain Entity Classs => 입출금 내역 정보를 관리하는 클래스
// 한 행은 BankTransaction 으로 표현이 됨.
// => 가독성, 유지보수성이 높아짐.
public class BankTransaction {
	// CSV 파일의 한 행의 3개의 칼럼.
	// 첫번째 칼럼 : [0], 두 번째 칼럼 : [1], 세 번째 칼럼 : [2]
	// 가독성이 많이 개선됨.
    private final LocalDate date;
    private final double amount;
    private final String description;


    public BankTransaction(final LocalDate date, final double amount, final String description) {
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "date=" + date +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankTransaction that = (BankTransaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                date.equals(that.date) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, description);
    }


}