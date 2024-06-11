package com.iteratrlearning.shu_book.chapter_05;

/*
 * 실제 WorkFlow 에 Business Logic 를 태우는 것.
 */
public class Main {

    public static void main(final String...args) {

    	// Facts
        var env = new Facts(); // 지역 변수 형식 추론
        						// => 컴파일러가 자동으로 추론
        
        					// 명시적 형식 선언
        					// Facts env = new Facts();
        env.setFact("name", "Bob");
        env.setFact("jobTitle", "CEO");

        // Condition, Action 은....?
        // Rule( Condition, Action )
        
        // BusinessRule 관리 및 실행하는 클래스
        final var businessRuleEngine = new BusinessRuleEngine(env);
        
        // 엔진은 있는데, 필요한 Rule 은..... ?

        // Rule Builder
        // RuleBuilder.when().then(); => 사용자 친화적 API( 복잡도가 낮음. )
        // 1. 메소드 체이닝 => Rule 인스턴스가 생성됨.
        //    RuleBuilder 를 사용해서 Rule 인스턴스 생성.
        // 2. RuleBuilder 만 이용하면, 다양한 Business Logic Rule을
        //    만드는 것을 공통화 할 수 있음. => SRP, OCP
        // 3. Business Domain 을 적용.
        //    어떤 고객이 CEO 이면, 영업팀으로 메일 발송( 기본 설계 )
        //                 when... then...
        
        // 어떤 고객이 CEO 이면, 영업팀으로 메일 발송(rule) 을 만들고 싶음.
        //    => ruleSendEmailToSalesWhenCEO => 변수명 자체가 도메인 룰 => DDD
        final Rule ruleSendEmailToSalesWhenCEO =
        		// Builder pattern 사용 => SRP( Rule 만 생성 책임 )
                RuleBuilder
                  //1. RuleBuilder 인스턴스 생성.
                  //   Condition(조건) 설정 => OCP 적용
                  .when(facts -> "CEO".equals(facts.getFact("jobTitle")))
                  //2. 메소드 체인으로 RuleBuilder 의 멤버 메소드 then() 호출
                  //   Rule 생성. Condition(조건), Action(수행하려는 동작) 을
                  //   매개변수로 해서 Rule 인스턴스 생성. => OCP 적용
                  .then(facts -> {
						            var name = facts.getFact("name");
						            // "잠재 우량 고객 ~~~~님" 이라고 출력
						            System.out.println("Relevant customer!!!: " + name);
                  				}
                	   );

        // 생성된 Rule 은 전용 엔진에 등록해서 사용.
        businessRuleEngine.addRule(ruleSendEmailToSalesWhenCEO);
        
        // 생성된 Rule 사용.
        businessRuleEngine.run();

    }
}
