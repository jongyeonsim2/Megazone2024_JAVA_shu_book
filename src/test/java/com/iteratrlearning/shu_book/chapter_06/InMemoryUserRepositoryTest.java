package com.iteratrlearning.shu_book.chapter_06;

import com.iteratrlearning.shu_book.chapter_06.in_memory.InMemoryUserRepository;
import org.junit.Before;

/*
 * TDD 방식으로 점진적인 기능 구현을 위함.
 * 
 * 사용자 정보를 저장하기 위해 내부 메모리 저장소를 구현.
 * 사용자 정보는 key, value 형태로 관리하는 것이 편리하기 때문에, Map 형태로 구현.
 */
public class InMemoryUserRepositoryTest extends AbstractUserRepositoryTest
{
    private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();

    @Override
    protected UserRepository newRepository()
    {
        return inMemoryUserRepository;
    }
}
