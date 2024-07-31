package com.iteratrlearning.shu_book.chapter_06.in_memory;

import com.iteratrlearning.shu_book.chapter_06.FollowStatus;
import com.iteratrlearning.shu_book.chapter_06.User;
import com.iteratrlearning.shu_book.chapter_06.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> userIdToUser = new HashMap<>();

    @Override
    public Optional<User> get(final String userId) {
    	/*
    	 * Optional 객체 생성하기
    	 * 값이 null일 가능성이 있으면, of() 대신에 ofNullable()을 사용.
    	 * of() 는 매개변수의 값이 null 이면, NPE 가 발생함.
    	 * 
    	 * 따라서, 등록된 User 이면 User 객체를 반환함.
    	 */
        return Optional.ofNullable(userIdToUser.get(userId));
    }

    @Override
    public boolean add(final User user) {
    	/*
    	 * 신규 User 이면 Map 에 저장하고, null을 반환.
    	 * 기존 User 이면 Map 에서 Key에 해당하는 value 를 반환. 
    	 */
        return userIdToUser.putIfAbsent(user.getId(), user) == null;
    }

    @Override
    public void update(final User user) {
        // Deliberately blank - since we don't actually persist this data
    }

    @Override
    public FollowStatus follow(final User follower, final User userToFollow) {
        return userToFollow.addFollower(follower);
    }

    @Override
    public void clear() {
        userIdToUser.clear();
    }

    @Override
    public void close() {

    }
}
