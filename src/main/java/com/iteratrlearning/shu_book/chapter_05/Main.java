package com.iteratrlearning.shu_book.chapter_05;

/*
 * 실제 WorkFlow 에 Business Logic 를 태우는 것.
 */
public class Main {

    public static void main(final String...args) {

    	// Facts
        var env = new Facts();
        env.setFact("name", "Bob");
        env.setFact("jobTitle", "CEO");

        // Condition, Action 은....?
        // Rule( Condition, Action )
        
        // BusinessRule 관리 및 실행하는 클래스
        final var businessRuleEngine = new BusinessRuleEngine(env);
        
        // 엔진은 있는데, 필요한 Rule 은..... ?

        // Rule Builder
        // RuleBuilder.when().then();
        // 1. 메소드 체이닝 => Rule 인스턴스가 생성됨.
        //    RuleBuilder 를 사용해서 Rule 인스턴스 생성.
        final Rule ruleSendEmailToSalesWhenCEO =
                RuleBuilder
                  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
                  .then(facts -> {
						            var name = facts.getFact("name");
						            System.out.println("Relevant customer!!!: " + name);
                  				}
                	   );

        businessRuleEngine.addRule(ruleSendEmailToSalesWhenCEO);
        businessRuleEngine.run();

    }
}
