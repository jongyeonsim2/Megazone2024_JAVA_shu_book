package com.iteratrlearning.shu_book.chapter_04;

public final class Attributes
{
    public static final String PATH = "path";
    
    // 환자명을 나타내는 key 값으로 사용
    public static final String PATIENT = "patient";
    // 환자의 주소를 나타내는 key 값으로 사용
    public static final String ADDRESS = "address";
    // 임포트 파일의 본문을 나타내는 key 값으로 사용
    public static final String BODY = "body";
    
    // 이미지(xray) 파일의 폭, 높이 나타내는 key 값으로 사용
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    
    // 임포트 될 파일의 유형 : "IMAGE", "REPORT", "INVOICE", "LETTER"
    public static final String TYPE = "type";
    
    // 치료 비용 청구서(INVOICE) 파일의 청구 금액
    public static final String AMOUNT = "amount";
}
