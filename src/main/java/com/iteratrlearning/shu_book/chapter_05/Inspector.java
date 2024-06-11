package com.iteratrlearning.shu_book.chapter_05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Business Logic 에 대한 가상의 테스트 환경 클래스
 * 
 * InspectorTest 단위 케이스에서 사용됨.
 */
public class Inspector {

    private final List<ConditionalAction> conditionalActionList;

    public Inspector(final ConditionalAction... conditionalActions) {
        this.conditionalActionList = Arrays.asList(conditionalActions);
    }

    /*
     * Business Logic 의 실제 검사를 담당 
     * 
     * Facts facts = {"jobTitle", "CEO"}
     * Diagnosis = Business Logic 평가 결과(3 가지 정보)
     */
    public List<Diagnosis> inspect(final Facts facts) {
    	// Business Logic 평가 결과 목록
        final List<Diagnosis> diagnosisList = new ArrayList<>();
        
        // 조건부 액션 목록 기반으로 반복 평가
        for (ConditionalAction conditionalAction : conditionalActionList) {
        	
        	//ConditionalAction의 구현체 => JobTitleCondition.evaluate()
            final boolean conditionResult = conditionalAction.evaluate(facts);
            
            //Business Logic 검사 결과
            final Diagnosis diagnosis = 
            		new Diagnosis(facts, conditionalAction, conditionResult);
            
            //결과 목록에 추가
            diagnosisList.add(diagnosis);
        }
        return diagnosisList;
    }
}
