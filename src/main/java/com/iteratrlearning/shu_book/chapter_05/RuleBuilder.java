package com.iteratrlearning.shu_book.chapter_05;

public class RuleBuilder {
	// 함수형 IF
    private Condition condition;

    // private 생성자.
    // Business Rule 용으로 객체를 생성시켜야 함으로
    // 외부에서 생성자를 사용할 수 없도록 해서,
    // 내부에서 일정 형태의  Rule 이 되도록 해야 함.
    private RuleBuilder(Condition condition) {
        this.condition = condition;
    }

    // static 메소드
    // 반환형은 RuleBuilder 자기 자신 타입임.
    // OCP 가 적용됨. 매개변수가 함수형 IF 임.
    public static RuleBuilder when(Condition condition) {
    	// 생성자 호출
    	// 
        return new RuleBuilder(condition);
    }

    // OCP 가 적용됨. 매개변수가 함수형 IF 임.
    public Rule then(Action action) {
        return new Rule(condition, action);
    }
}