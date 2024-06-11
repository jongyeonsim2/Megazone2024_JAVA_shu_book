package com.iteratrlearning.shu_book.chapter_05;

/*
 * Business Logic 에 대한 가상 환경에서의 평가 결과 클래스
 */
public class Diagnosis {

	// 조건부 액션
    private final ConditionalAction conditionalAction;
    // 조건에 따라 수행할 액션의 대상
    private final Facts facts;
    // 평가(진단) 결과 => Business Logic 가상 환경에서의 평가 결과.
    // Business Logic = ConditionalAction + Facts 의 조합.
    private final boolean isPositive;

    public Diagnosis(final Facts facts,
                     final ConditionalAction conditionalAction,
                     final boolean isPositive) {
        this.facts = facts;
        this.conditionalAction = conditionalAction;
        this.isPositive = isPositive;
    }

    public ConditionalAction getConditionalAction() {
        return conditionalAction;
    }

    public Facts getFacts() {
        return facts;
    }

    public boolean isPositive() {
        return isPositive;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "conditionalAction=" + conditionalAction +
                ", facts=" + facts +
                ", result=" + isPositive +
                '}';
    }
}
