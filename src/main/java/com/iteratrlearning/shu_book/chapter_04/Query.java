package com.iteratrlearning.shu_book.chapter_04;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;


/*
 * public class 가 아님.
 * 
 * 캡슐화가 적용이 됨.
 * 
 * 자기 package 안에서만  Query 를 볼 수 있음.
 * 다른 package 에서는 사용할 일 이 없다고 생각해서 범위 설정을 이렇게 함.
 * 
 * 따라서, 다른 package 에서 불필요한 class 가 보이지 않게 되어
 * 복잡도가 낮아지게 됨. 
 */
class Query implements Predicate<Document> {
    private final Map<String, String> clauses;

    //검색 조건 문자열 : "patient:Joe,body:Diet Coke" => map 형태로 변환
    /*
     * patient(key), Joe(value) => 환자명 조회 조건
     * body(key), Diet Coke(value) => 본문 조회 조건
     * 
     * 
     * 
     * 
patient.report

Patient: Joe Bloggs => 환자명(patient key)

On 5th January 2017 I examined Joe's teeth. => 본문(body key)
We discussed his switch from drinking Coke to Diet Coke.
No new problems were noted with his teeth.
     * 
     * 
     * 
     */
    
    
    static Query parse(final String query) {
    	/*
    	 * 사용자의 API 접근성을 향상 시킴.(캡슐화가 적용이 됨.)
    	 * 
    	 * 1. 생성자를 private 로
    	 * 2. 조회 메소드에서 생성자 호출
    	 */
        return new Query(Arrays.stream(query.split(","))
        		// 1. stream 생성. 구분자는 "," 로 해서 생성.
        		//    "patient:Joe" "body:Diet Coke"
              .map(str -> str.split(":"))
                // 2. String 배열을 중간연산. 구분자는 ":"
                //    {patient,Joe} {body,Diet Coke}
              .collect(toMap(x -> x[0], x -> x[1])));
                // 3. Map<Key, Value> 형태로 최종 연산
        		//    <"patient", "Joe"> <"body", "Diet Coke">
    }

    private Query(final Map<String, String> clauses) {
        this.clauses = clauses;
    }

    @Override
    public boolean test(final Document document) {
        return clauses.entrySet()
                      .stream()
                      .allMatch(entry -> {
                          final String documentValue = document.getAttribute(entry.getKey());
                          final String queryValue = entry.getValue();
                          return documentValue != null && documentValue.contains(queryValue);
                      });
    }
}
