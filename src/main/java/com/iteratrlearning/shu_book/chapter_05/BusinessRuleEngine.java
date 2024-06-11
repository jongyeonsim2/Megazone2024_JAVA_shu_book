package com.iteratrlearning.shu_book.chapter_05;

import java.util.ArrayList;
import java.util.List;

/*
 * 회사의 WorkFlow 에 관련된 모든 Business Logic Rule 를 
 * 가지고 있고, 관리, 실행하는 클래스
 */
public class BusinessRuleEngine {

	// Rule 의 목록
    private final List<Rule> rules;
    private final Facts facts;

    public BusinessRuleEngine(Facts facts) {
        this.facts = facts;
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }

    public void run() {
        this.rules.forEach(rule -> rule.perform(facts));
    }
}
