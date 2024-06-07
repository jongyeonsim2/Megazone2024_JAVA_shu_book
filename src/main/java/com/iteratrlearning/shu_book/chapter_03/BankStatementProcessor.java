package com.iteratrlearning.shu_book.chapter_03;

import java.time.Month;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ch3 에서 가장 중요한 클래스임.
 * 
 * OCP 를 적용해서 정말 좋은점이 많다는 것을 알게 되었으나,
 * 과하면 좋지 않은 점도 있음. 
 * 
 * 함수형 인터페이스가 너무 많이 늘어나게 되는 점이 문제가 될 수 있음.
 * - 파악해야 할 메소드가 많아지게 되어, 복잡도가 높아지게 됨.
 * 
 * 이러한 전체적인 상황 : 명시적 API 제공 vs 암묵적 API 제공
 * 
 * 관리기준을 새로 팀내에서 세워야 함.(우선순위 등을 고려하게 됨.)
 * 
 * 
 */

public class BankStatementProcessor {

	// 입출금 내역 정보 List. BankTransaction(Domain Entity) List
	// 현재 멤버 정보를 바탕으로 현재 클래스는 조회 기능이 있을것으로 추측.
	// 현재 멤버를 DB 라고 생각하면, 바로 느낌이 옴. DB 는 데이터 조회가 목적이니까.
    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(final List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public SummaryStatistics summarizeTransactions() {

        final DoubleSummaryStatistics doubleSummaryStatistics = bankTransactions.stream()
                .mapToDouble(BankTransaction::getAmount)
                .summaryStatistics();

        return new SummaryStatistics(doubleSummaryStatistics.getSum(),
                                     doubleSummaryStatistics.getMax(),
                                     doubleSummaryStatistics.getMin(),
                                     doubleSummaryStatistics.getAverage());
    }

    
    
    
    // OCP 원칙이 적용이 됨. OCP( Open/Closed Principle, 개방폐쇠원칙 )
    // 개방 : 새로운 요구사항이 발생하더라도 대응을 할 수 있도록  open 됨.
    // 폐쇠 : 수정사항이 발생하더라도, 다른 곳에 영향이 가지 않도록 close 되었다는 것임.
    
    // - 기존 코드를 변경하지 않으므로, 잘못될 가능성이 차단됨.
    // - 코드가 중복되지 않으므로, 기존 코드의 재사용성이 높아짐.
    // - 결합도가 낮아지게 되어, 유지보수성이 향상됨.
    
    
    
    // calculateTotalInMonth() -> summarizeTransactions() 호출함.
    // 입출금 내역 리스트를 반복처리 기능이 있는 메소드.
    // OCP 를 적용하기 위해서, 함수형 IF 를 매개변수로 사용함.
    // 함수형 IF => BankTransactionSummarizer class 임.
    public double summarizeTransactions(final BankTransactionSummarizer bankTransactionSummarizer) {
        double result = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            result = bankTransactionSummarizer.summarize(result, bankTransaction);
        }
        return result;
    }

    
    // 메소드에는 아래의 1, 2에 대한 조건식을 표현함.
    // 1. 조건에 맞는 월이면, 현재 달의 입출금액을 가져와서 합산.
    // 2. 조건에 맞지 않는 월이면, 합산하지 않음.
    
    // 향후에 조건에 월이 아니고, 입출금 내역의 카테고리를 기준으로 검색을 하게 된다면....
    // 현재의 month 매개변수 대신에 카테고리를 매개변수로 사용하는 
    // 새로운 메소드가 필요해지게 됨.
    
    // 그래서, 새로운 메소드를 만든다면, 예상되는 문제점이 뭘까?
    // 1. 이전 메소드 와 새로운 메소드에서 입출금 내역 리스트 의 반복문이 중복됨.
    // 2. 이러한 메소드가 여러개 만들어진다고 가정하고,
    //	  공통된 반복문에서 수정이 발생한다면, 모든 메소드를 수정해야함.
    // 3. 이 반복되는 반복문을 공통 함수로 뽑아내고 싶어짐.
    // 4. 기존 메소드에는 조건식만 남게됨.
    // 5. 조건식을 가지고 있는 함수는 반복문이 있는 메소드를 사용만 할 수 있으면, 
    //	  문제점이 해결이됨.
    // 6. 조건식이 있는 메소드는 람다식으로 만들고,
    //    반복문이 있는 메소드에서 람다식의 매개변수로 받으면 문제점의 해결이 됨.(리팩토링)
    public double calculateTotalInMonth(final Month month) {
        return summarizeTransactions((acc, bankTransaction) ->
        
        	// for 입출금 내역 리스트
        		// if 리스트에서 하나의 입출금 내역을 아래의 조건식으로 비교.
                bankTransaction.getDate().getMonth() == month 
                	? acc + bankTransaction.getAmount() : acc);//조건식
    }

    /**
     * 
     * 바로 위의 메소드인 summarizeTransactions() 호출할 때, 람다식으로 매개변수로 전달함.
     * 바로 람다식 전달이 이해가 안되면 풀어서 생각하면됨.
     * 
     * 1. 익명클래스로 객체를 생성해서 매개변수로 전달하는 방법.
     * 
     * final BankTransactionSummarizer bankTransactionSummarizer =
     * 		
     * 		new BankTransactionSummarizer(
     * 			double summarize(double accumulator, 
     * 					BankTransaction bankTransaction) {
     * 				return bankTransaction.getDate().getMonth() == month 
                	? accumulator + bankTransaction.getAmount() : accumulator);
     * 			}
     * 		);
     * 
     * 
     * 2. 익명클래스로 줄여서 람다식으로 전달하는 방법.
     * 
     * final BankTransactionSummarizer bankTransactionSummarizer =
     * 		
     * 		
     * 			(accumulator, bankTransaction) 
     * 			->
     *  			 bankTransaction.getDate().getMonth() == month 
                	? accumulator + bankTransaction.getAmount() : accumulator)
     * 			
     * 		
     * 
     */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // findTransactionsGreaterThanEqual() -> findTransactions() 호출함.
    public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {

    	return findTransactions(bankTransaction -> 
        						
        						bankTransaction.getAmount() >= amount//조건식
        						
        						);

        // 바로 처리하게 되는 경우라면 => Stream 을 활용해서 바로 처리하는 방법도 고려.
        // Stream 을 적용한다는 것은
    	// 중간 연산, 최종 연산 결과로 List<BankTransaction> 로 반환하면 됨.
    	/*
    	return this.bankTransactions
    			.stream()
    			.filter(bankTransaction -> bankTransaction.getAmount() >= amount)
    			.collect(Collectors.toList());
    	*/
    	
    }

    public List<BankTransaction> findTransactions(final BankTransactionFilter bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }
        return result;
    }
    
    
    
}
