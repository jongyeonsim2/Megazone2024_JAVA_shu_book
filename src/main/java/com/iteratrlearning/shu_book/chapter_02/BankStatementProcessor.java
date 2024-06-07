package com.iteratrlearning.shu_book.chapter_02;

import java.time.Month;
import java.util.List;

// 다양한 조건의 입출금 총액을 계산하는 책임을 지고 있는 클래스.
// SRP 적용 => 응집도 향상.
public class BankStatementProcessor {
	// 입출금 내역 정보
    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(final List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    // 입출금 내역의 총액 계산 : 조건 없음.
    public double calculateTotalAmount() {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            total += bankTransaction.getAmount();
        }
        return total;
    }
    
    
    public double calculateTotalInMonth(final Month month) {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDate().getMonth() == month) {

                total += bankTransaction.getAmount();
            }
        }
        return total;
    }
    
  

    // 입출금 내역의 총액 계산 : 입출금 내역에 대한 조건
    public double calculateTotalForCategory(final String category) {
        double total = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransaction.getDescription().equals(category)) {
                total += bankTransaction.getAmount();
            }
        }
        return total;
    }
}
