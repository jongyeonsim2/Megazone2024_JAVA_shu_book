package com.iteratrlearning.shu_book.chapter_06.dipEx;

// 고수준(class) -> 저수준 모듈(class)
// 고수준 -> 추상화 -> 저수준 : 스위치를 다른 곳에서 다양하게 사용하기 위함.

class Light {
	public void activate() {
		System.out.println("전등에 불이 들어왔음.");
	}
	
	public void deactivate() {
		System.out.println("전등에 불이 꺼졌음.");
	}
}

// 전등 스위치
// 라디오 스위치, Fan 스위치 등으로 스위치를 다양한 곳에 사용하고 싶음.
// 따라서, 현재의 문제점은 같은 기능이 중복되고,
// 스위치가 라디오와 Fan 클래스에 강하게 결합되는 문제가 있음.
// ( 즉, 고수준(switch)이 저수준(Radio, Fan, Light)에 의존하고 있다는 것임. )
// => 상기의 문제점을 해결하면, 의존 관계 역전이 됨.
// => 고수준 -> 추상화 -> 저수준
class LightElectronicPowerSwitch {
	
	private Light light;
	private boolean on;
	
	public LightElectronicPowerSwitch(Light light) {
		this.light = light;
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void push() {
		if (isOn()) {
			light.deactivate();
			on = false;
		} else {
			light.activate();
			on = true;
		}
	}
	
}

// 라디오 스위치
class RadioElectronicPowerSwitch {
	
}
// Fan 스위치
class FanElectronicPowerSwitch {
	
}

public class LightExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Light light = new Light();
		
		LightElectronicPowerSwitch powerSwithc =
				new LightElectronicPowerSwitch(light);
		
		powerSwithc.push();
		powerSwithc.push();
	}

}
