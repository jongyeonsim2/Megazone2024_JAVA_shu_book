package com.iteratrlearning.shu_book.chapter_04;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

public class DocumentManagementSystem {
    private final List<Document> documents = new ArrayList<>();
    private final List<Document> documentsView = unmodifiableList(documents);
    // tag::importer_lookup[]
    private final Map<String, Importer> extensionToImporter = new HashMap<>();

    // 생성자
    /*
     * 문서관리 시스템에서 다양한 형식의 파일을 임포트시 담당하게 될 임포터들을
     * 사전에 등록해두는 작업.
     */
    public DocumentManagementSystem() {
    	// key : 임포트 대상 파일의 확장자, value : 대상 파일 전용 임포터
        extensionToImporter.put("letter", new LetterImporter());
        extensionToImporter.put("report", new ReportImporter());
        extensionToImporter.put("jpg", new ImageImporter());
    }
    // end::importer_lookup[]
    {
        extensionToImporter.put("invoice", new InvoiceImporter());
    }

    // tag::importFile[]
    // 임포터 시작
    /*
     * 1. 파일 존재 여부 체크
     * 2. 파일 확장자의 유무 체크
     * 3. 등록된 파일 확장자인지 체크(파싱 가능 여부 체크)
     * 4. 등록된 확장자 => 전용 임포터 사용
     * 5. 등록된 확장자 => 전용 임포터가 없는 경우 => 예외 처리
     * 6. 임포팅 작업이 완료 => Document(문서) 반환.
     * 7. 반환된 문서를 문서 관리 시스템에 등록.
     */
    public void importFile(final String path) throws IOException {
        final File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException(path);
        }

        final int separatorIndex = path.lastIndexOf('.');
        if (separatorIndex != -1) {
            if (separatorIndex == path.length()) {
                throw new UnknownFileTypeException("No extension found For file: " + path);
            }
            final String extension = path.substring(separatorIndex + 1);
            
            // 파일 확장자별로 임포트가 등록되어 있어, 조회하면, 임포터가 반환됨.
            final Importer importer = extensionToImporter.get(extension);
            if (importer == null) {
                throw new UnknownFileTypeException("For file: " + path);
            }

            // 전용 임포터가 대상 파일의 임포트를 처리.
            // 완료 후 Document 가 반환됨.
            // 직접 구현체를 사용하지 않으므로, 약한 결합.
            final Document document = importer.importFile(file);
            
            // 문서 관리 시스템에 임포트 완료된 파일 정보를 문서로 등록.
            documents.add(document);
        } else {
            throw new UnknownFileTypeException("No extension found For file: " + path);
        }
    }
    // end::importFile[]

    public List<Document> contents() {
        return documentsView;
    }

    /*
     * 검색
     */
    public List<Document> search(final String query) {
        return documents.stream()
                        .filter(Query.parse(query))// Predicate 함수형 IF
                        .collect(Collectors.toList());
    }
}
