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
 */

// 고수준
// 저수준에 있었던, switch 기능을 추출함. 저수준에서 분리.
interface Switch {
	void push();
	boolean isOn();
}

// 추상화
/*
 * Light, Radio, Fan 제품이 스위치의 push()로 모두 동작이 되도록 해야함.
 * 앞으로 추가되는 제품도 하나의 동일한 스위치로 push() 를 사용할 수 있게됨. (재사용)
 * 
 * - Light, Radio, Fan 를 하나의 공통 타입으로 묶어야 함.
 * - 각각의 제품은 동일한 방법으로 동작이 되지만,
 *   상세 처리 방법은 각각의 제품에서 알아서 동작이 되어야 함.
 *   
 * 결론의 상기의 두 가지가 모두 만족되도록 하기 위해서 전원 on/off 전용 interface 를 만듬.
 */
interface Powerable {
	void activate();
	void deactivate();
}

/*
 * 모든 제품에 대해서 스위치도 동작이 되어야 하고,
 * 스위치 동작에 따른 각 제품의 전원 on/off 도 동작이 되어야 함.
 * 
 */
class ElecPowerSwitch implements Switch {

	private Powerable device;
	private boolean on;
	
	// 의존 주입
	public ElecPowerSwitch(Powerable device) {
		this.device = device;
	}
	
	@Override
	public void push() {
		// TODO Auto-generated method stub
		if (isOn()) {
			device.deactivate();
			on = false;
		} else {
			device.activate();
			on = true;
		}
	}

	@Override
	public boolean isOn() {
		// TODO Auto-generated method stub
		return on;
	}
	
}

// 저수준
class Light2 implements Powerable {

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		System.out.println("전등 켜짐.");
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		System.out.println("전등 꺼짐.");
	}
	
}

// 저수준
class Fan implements Powerable {

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		System.out.println("팬 켜짐.");
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		System.out.println("팬 꺼짐");
	}
	
}


		
public class DipExp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Powerable light = new Light2();
		Switch lightSwitch = new ElecPowerSwitch(light);
		lightSwitch.push();
		lightSwitch.push();
		
		Powerable fan = new Fan();
		Switch fanSwitch = new ElecPowerSwitch(fan);
		fanSwitch.push();
		fanSwitch.push();
		
	}

}
