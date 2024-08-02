package com.iteratrlearning.shu_book.chapter_06.dipEx;

/*
 * 
 * 1. 목적 
 *    switch 를 공용화해서 다양하게 사용하고 싶음.
 *    Radio 스위치, Fan 스위치 등등으로...
 *    
 * 2. 의존 관계에 따른 원인과 문제 
 *    
 *    현재는 전등전용스위치(LightElectronicPowerSwitch) 만으로만 사용 가능함.
 *    즉, 전등 전용 스위치임으로 공용 스위치가 아님.
 *    
 *    물론, 지금처럼 전용 스위치의 형태로 스위치로 만들어서 사용해도 되지만,
 *    아래와 같은 문제점이 발생.
 *    
 *    - switch를 on/off 하는 기능이 중복됨.
 *      push() 메소드가 각각의 전용 스위치 클래스에 존재하게 됨.
 *      => 한 곳을 수정하면 논리적으로 다른 곳에도 영향이 가게됨.
 *     
 *    - switch 를 다양하게 사용하고 싶다는 근본 목적을 생각해보면,
 *      여전히 스위치(고수준)가 Light, Radio, Fan(저수준) 에 의존하게 됨.(원인)
 *      
 *      즉, switch의 push() 기능이 각각의 전용 스위치에 존재하고 있어,
 *      switch 의 공통 기능이 각각의 전용 스위치 클래스에 존재하고 있는 상태임.(결과)
 *      
 * 3. 의존 관계의 역전으로 문제 해결
 * 
 *    switch 를 공용화해서 다양하게 사용할 수 있게 됨.
 *    
 *    - switch push() on/off 기능 분리 => 고수준과 저수준의 분리
 *      각각의 전용 스위치 클래스에 있는 switch 의 push() 의 기능을 분리해야 함.
 *    
 *    - switch push() on/off 기능 동작 => 고수준 -> 추상화
 *      스위치가 필요한 모든 곳에서 동작이 되도록 해야함.
 *      즉, 스위치 push() 에서 모든 대상이 동작되도록 해야함.
 *      => 하나의 스위치 push() 에 하나의 유형(타입)이 대상이 되도록 하면 됨.
 *    
 *    - 각 제품의 on/off 의 상세 동작( 스위치의 push() on/off 가 아님. ) 
 *      => 추상화 -> 저수준
 *      
 *      Light, Radio, Fan 의 on/off 의 동작에 대한 각각의 상세 설정은
 *      각각 유지가 되어야 함.
 *      
 *      
 * 
 * 
 * 
 * 
 */
		
		
public class DipExp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
