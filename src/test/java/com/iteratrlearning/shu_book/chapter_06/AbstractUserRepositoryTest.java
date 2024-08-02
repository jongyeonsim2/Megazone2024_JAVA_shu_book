package com.iteratrlearning.shu_book.chapter_06;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.iteratrlearning.shu_book.chapter_06.FollowStatus.ALREADY_FOLLOWING;
import static com.iteratrlearning.shu_book.chapter_06.TestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@Ignore("abstract base test")
public abstract class AbstractUserRepositoryTest
{
    private ReceiverEndPoint receiverEndPoint = mock(ReceiverEndPoint.class);
    private UserRepository repository;

    protected abstract UserRepository newRepository();

    @Before
    public void setUp()
    {
        repository = newRepository();

        repository.clear();
    }

    // 사용자 등록 및 등록 확인
    @Test
    public void shouldLoadSavedUsers()
    {
        repository.add(userWith(USER_ID));

        assertThat(repository.get(USER_ID).get(), matchesUser());
    }

    // 사용자 중복 등록 확인.
    @Test
    public void shouldNotAllowDuplicateUsers()
    {
        assertTrue(repository.add(userWith(USER_ID)));

        assertFalse(repository.add(userWith(USER_ID)));
    }

    // 트위트의 관계 설정 : follow(구독) 설정
    @Test
    public void shouldRecordFollowerRelationships()
    {
    	// follow 설정을 위해서 트위트 사용자 2명을 생성.
        final User user = userWith(USER_ID);
        final User otherUser = userWith(OTHER_USER_ID);

        // 사용자 관리 Map 에 등록
        repository.add(user);
        repository.add(otherUser);
        
        // follow(구독) 설정
        repository.follow(user, otherUser);

        final UserRepository reloadedRepository = newRepository();
        final User userReloaded = reloadedRepository.get(USER_ID).get();
        final User otherUserReloaded = reloadedRepository.get(OTHER_USER_ID).get();
        assertEquals(ALREADY_FOLLOWING, otherUserReloaded.addFollower(userReloaded));
    }

    @Test
    public void shouldRecordPositionUpdates()
    {
        final String id = "1";

        final Position newPosition = new Position(2);
        final User user = userWith(USER_ID);
        repository.add(user);
        assertEquals(Position.INITIAL_POSITION, user.getLastSeenPosition());

        user.receiveTwoot(twootAt(id, newPosition));
        repository.update(user);

        final UserRepository reloadedRepository = newRepository();
        final User reloadedUser = reloadedRepository.get(USER_ID).get();
        assertEquals(newPosition, user.getLastSeenPosition());
        assertEquals(newPosition, reloadedUser.getLastSeenPosition());
    }

    @After
    public void shutdown() throws Exception
    {
        repository.close();
    }

    private User userWith(final String userId)
    {
        final User user = new User(userId, PASSWORD_BYTES, SALT, Position.INITIAL_POSITION);
        user.onLogon(receiverEndPoint);
        return user;
    }

    private Matcher<User> matchesUser()
    {
        return allOf(
            hasProperty("id", equalTo(USER_ID)),
            hasProperty("password", equalTo(PASSWORD_BYTES)));
    }
}
