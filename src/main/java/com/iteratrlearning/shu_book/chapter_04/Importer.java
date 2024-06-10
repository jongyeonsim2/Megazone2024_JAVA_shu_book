package com.iteratrlearning.shu_book.chapter_04;

import java.io.File;
import java.io.IOException;

// tag::importer[]
interface Importer {
	/*
	 * importFile() 를 구현한다는 의미의 논리적인 의미는?
	 * 
	 * - LSP의 원칙(후행조건)
	 *   자식 클래스나, 인터페이스를 구현하는 쪽은 기반쪽에서 하려고 하는 행동을
	 *   그대로 유지해야 한다.
	 *   
	 * - 유효한 파일( 임포트 대상 파일이 존재 & 읽을 수 있는 파일 ) 을 대상으로
	 *   importFile() 메소드를 실행했다면, 이 결과가 문서 관리 시스템에 등록이 
	 *   되어야 함.
	 *   
	 *   그래서, 여기서 중요한 원칙 : 이 결과가 문서 관리 시스템에 등록이 되도록
	 *   코드로 구현해야 함. ( 구현하지 않아도, 문법적으로 문제는 되지 않지만,
	 *   논리성이 결여됨. 이것이 본질임. )
	 *  
	 * - 정상적으로 문서 관리 시스템에 등록이 되면, 조회가 됨. 근본적 인유임.
	 * 
	 * - 따라서, letter, invoice, report, xray image file 에 대해서
	 *   importFile() 를 구현시, 위에서 말한 원칙을 지켜야 함.
	 *   
	 *   앞으로 새로운  형식의 파일에 대해서도 위에서 말한 원칙을 지켜야 함.
	 */
    Document importFile(File file) throws IOException;
}
// end::importer[]
