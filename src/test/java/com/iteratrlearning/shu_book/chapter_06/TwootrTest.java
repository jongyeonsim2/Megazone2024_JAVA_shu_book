package com.iteratrlearning.shu_book.chapter_06;

import com.iteratrlearning.shu_book.chapter_06.in_memory.InMemoryTwootRepository;
import com.iteratrlearning.shu_book.chapter_06.in_memory.InMemoryUserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static com.iteratrlearning.shu_book.chapter_06.FollowStatus.ALREADY_FOLLOWING;
import static com.iteratrlearning.shu_book.chapter_06.FollowStatus.SUCCESS;
import static com.iteratrlearning.shu_book.chapter_06.TestData.TWOOT;
import static com.iteratrlearning.shu_book.chapter_06.TestData.twootAt;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class  TwootrTest
{

    private static final Position POSITION_1 = new Position(0);


    private final ReceiverEndPoint receiverEndPoint = mock(ReceiverEndPoint.class);
    // end::mockReceiverEndPoint[]

    /*
     * 트위에서 사용되는 데이터 저장소
     * - 트위의 메세지를 저장
     * - 사용자 정보를 저장 -> 인증, 팔로워
     */
    // 트위 메세지 관리 in memory : List 형태로 관리
    private final TwootRepository twootRepository = spy(new InMemoryTwootRepository());
    // 트위터 사용자 관리 in memory : Map 형태로 관리
    private final UserRepository userRepository = new InMemoryUserRepository();

    // 트윗과 관련된 핵심기능의 클래스(사용자 등록, 트윗 전송, 트윗 삭제)
    private Twootr twootr;
    // 트위 전송 기능을 구현한 클래스
    private SenderEndPoint endPoint;

    // 테스트를 수행하기 위한 환경의 사전 설정.
    @Before
    public void setUp()
    {
        twootr = new Twootr(userRepository, twootRepository);

        // 트윗 사용자 등록
        // 테스트 클래스의 사용자 정보를 이용해서 트윗 사용자 생성.
        assertEquals(RegistrationStatus.SUCCESS, twootr.onRegisterUser(TestData.USER_ID, TestData.PASSWORD));
        assertEquals(RegistrationStatus.SUCCESS, twootr.onRegisterUser(TestData.OTHER_USER_ID, TestData.PASSWORD));
    }

    // 트윗 사용자로 중복 등록이 되면 안됨.
    // 테스트를 위한 사전 사용자가 등록 된 후 이 사용자로 다시 사용자 등록을 수행.
    @Test
    public void shouldNotRegisterDuplicateUsers()
    {
        assertEquals(RegistrationStatus.DUPLICATE, twootr.onRegisterUser(TestData.USER_ID, TestData.PASSWORD));
    }

    // 인증가능한 유지인지 확인.
    @Test
    public void shouldBeAbleToAuthenticateUser()
    {
        logon();
    }

    // 틀린 인증 패스워드로는 인증이 되면 안됨.
    // tag::shouldNotAuthenticateUserWithWrongPassword[]
    @Test
    public void shouldNotAuthenticateUserWithWrongPassword()
    {
        final Optional<SenderEndPoint> endPoint = twootr.onLogon(
            TestData.USER_ID, "bad password", receiverEndPoint);

        assertFalse(endPoint.isPresent());
    }
    // end::shouldNotAuthenticateUserWithWrongPassword[]


    // 트위에 등록되지 않은 유저는 인증이 되면 안됨.
    @Test
    public void shouldNotAuthenticateUnknownUser()
    {
        final Optional<SenderEndPoint> endPoint = twootr.onLogon(
            TestData.NOT_A_USER, TestData.PASSWORD, receiverEndPoint);

        assertFalse(endPoint.isPresent());
    }

    // follow(구독)을 할 수 있는 트윗 사용자인가
    // tag::shouldFollowValidUser[]
    @Test
    public void shouldFollowValidUser()
    {
    	// Joe 가 follow 하려는 트윗 사용자가 존재함.
    	// 따라서, 반드시 로그인이 먼서 된 상태이어야 함.
        logon();

        // Joe 가 John 을 follow 하려고 함.
        // John 사용자 객체에 Joe 를 follower(구독자)로 등록
        final FollowStatus followStatus = endPoint.onFollow(TestData.OTHER_USER_ID);

        assertEquals(SUCCESS, followStatus);
    }
    // end::shouldFollowValidUser[]

    // follow(구독)하고 있는 트윗상용자를 중복해서 follow(구독) 하면 안됨.
    // tag::shouldNotDuplicateFollowValidUser[]
    @Test
    public void shouldNotDuplicateFollowValidUser()
    {
    	// John 사용자 객체에 Joe 를 follower(구독자)로 등록된 상태에서
    	// 다시 Joe 를 follower(구독자)로 등록하려고 함.
        logon();

        // John 사용자로 다시 테스트.
        endPoint.onFollow(TestData.OTHER_USER_ID);

        final FollowStatus followStatus = endPoint.onFollow(TestData.OTHER_USER_ID);
        assertEquals(ALREADY_FOLLOWING, followStatus);
    }
    // end::shouldNotDuplicateFollowValidUser[]

    // 무효한 트위 사용자는 follow(구독)하면 안됨.
    @Test
    public void shouldNotFollowInValidUser()
    {
        logon();

        // Jack 사용자는 트윗 사용자로 등록한 적이 없음.
        final FollowStatus followStatus = endPoint.onFollow(TestData.NOT_A_USER);

        assertEquals(FollowStatus.INVALID_USER, followStatus);
    }

    
    // 구독중인 사용자로부터의 트위(메세지) 수신
    // tag::shouldReceiveTwootsFromFollowedUser[]
    @Test
    public void shouldReceiveTwootsFromFollowedUser()
    {
        final String id = "1";

        //Joe 가 로그인
        logon();

        //Joe 가 John 을 follow
        endPoint.onFollow(TestData.OTHER_USER_ID);
        
        // Joe 와 John 모두가 트윗에 로그인 한 상태가 되어
        // 송수신이 되는 상태가 감.
        // John 이 트윗을 보내면, Joe 가 수신을 할 수 있는 상태.
        final SenderEndPoint otherEndPoint = otherLogon();
        
        // John이 트윗을 전송. => Hello World!
        // 다수의 follower(구독자:Joe)에 트위을 전송. => console 출력
        otherEndPoint.onSendTwoot(id, TWOOT);

        verify(twootRepository).add(id, TestData.OTHER_USER_ID, TWOOT);
        verify(receiverEndPoint).onTwoot(new Twoot(id, TestData.OTHER_USER_ID, TWOOT, new Position(0)));
    }
    // end::shouldReceiveTwootsFromFollowedUser[]

    // 로그아웃 된 상태에서 트위(메세지) 수신이 되면 안됨.
    @Test
    public void shouldNotReceiveTwootsAfterLogoff()
    {
        final String id = "1";

        // 트윗의 송수신이 되려면, 최소 2명이 로그인해야 가능.
        // 2명 중 한 명의 사용자가 로그인이 되지 않도록 함.
        // => 로그아웃 : 수신이 불가능한 상태
        userFollowsOtherUser();//=> 수신이 불가능한 상태로 설정.

        // John 만 로그인한 상태.
        final SenderEndPoint otherEndPoint = otherLogon();
        otherEndPoint.onSendTwoot(id, TWOOT);

        verify(receiverEndPoint, never()).onTwoot(new Twoot(id, TestData.OTHER_USER_ID, TWOOT, POSITION_1));
    }

    // 로그아웃 한 상태에서 follow(구독) 의 트윗이 전송이 되면
    // 다시 로그인 후 못받은 트윗을 받을 수 있는지
    // tag::shouldReceiveReplayOfTwootsAfterLogoff[]
    @Test
    public void shouldReceiveReplayOfTwootsAfterLogoff()
    {
        final String id = "1";

        // Joe가 John 을 follow(구독)함. => Joe가 로그아웃.
        userFollowsOtherUser();

        // John 일 트윗에 로그인
        final SenderEndPoint otherEndPoint = otherLogon();
        // John 이 트윗 전송
        otherEndPoint.onSendTwoot(id, TWOOT);

        // Joe 로 로그인
        logon();

        verify(receiverEndPoint).onTwoot(twootAt(id, POSITION_1));
    }
    // end::shouldReceiveReplayOfTwootsAfterLogoff[]

    
    // 트위 삭제
    @Test
    public void shouldDeleteTwoots()
    {
        final String id = "1";

        // john을 follower 설정 => 로그아웃
        userFollowsOtherUser();

        // john 이 로그인
        final SenderEndPoint otherEndPoint = otherLogon();
        // 트윗 전송
        otherEndPoint.onSendTwoot(id, TWOOT);
        // 트윗 삭제
        final DeleteStatus status = otherEndPoint.onDeleteTwoot(id);

        // Joe 가 로그인
        // John 이 삭제한 트윗을 수신이 되면 안됨.
        logon();

        assertEquals(DeleteStatus.SUCCESS, status);
        verify(receiverEndPoint, never()).onTwoot(twootAt(id, POSITION_1));
    }

    // 전송하지 않은 트위트 삭제
    @Test
    public void shouldNotDeleteFuturePositionTwoots()
    {
        logon();

        // 전송한 적이 없는 트위으로 삭제 하려고 함.
        final DeleteStatus status = endPoint.onDeleteTwoot("DAS");

        assertEquals(DeleteStatus.UNKNOWN_TWOOT, status);
    }

    // 다른 사용자가 작성한 트윗 삭제
    @Test
    public void shouldNotOtherUsersTwoots()
    {
        final String id = "1";

        // Joe 로 로그인
        logon();

        // John 로 로그인
        final SenderEndPoint otherEndPoint = otherLogon();
        // 트윗 전송
        otherEndPoint.onSendTwoot(id, TWOOT);

        // Joe 가 John 이 보낸 트윗을 삭제.
        final DeleteStatus status = endPoint.onDeleteTwoot(id);

        // Joe 가 삭제하려고 했던, 트윗을 List 에서 검색.
        assertNotNull(twootRepository.get(id));
        
        // 작성자가 아닌 트윗은 삭제할 수 없다는 상태정보
        assertEquals(DeleteStatus.NOT_YOUR_TWOOT, status);
    }

    private SenderEndPoint otherLogon()
    {
    	// John 이 트위에 로그인
        return logon(TestData.OTHER_USER_ID, mock(ReceiverEndPoint.class));
    }

    private void userFollowsOtherUser()
    {
        logon();

        //Joe 가 John 을 follow(구독) 하려고 함.
        endPoint.onFollow(TestData.OTHER_USER_ID);

        //Joe 를 수신 불가능한 상태로 설정.
        endPoint.onLogoff();
    }

    private void logon()
    {
    	// 기존에 등록된 Joe 라는 트윗 사용자로 로그인을 수행.
    	// 로그인 성공 => Joe 가 구독(follow)중인 트윗 사용자의 트위(메세지)를 수신
    	// 따라서, 두 번째 매개면수가 트위(메세지) 수신용 객체
    	// 로그인 성공하면, Joe 가 작성한 트윗(메세지)를 전송할 수 있는 객체를 받게 됨.
    	// 따라서, 수신과 송신이 모두 가능한 트위 사용자 상태가 됨.
        this.endPoint = logon(TestData.USER_ID, receiverEndPoint);
    }

    private SenderEndPoint logon(final String userId, final ReceiverEndPoint receiverEndPoint)
    {
        final Optional<SenderEndPoint> endPoint = twootr.onLogon(userId, TestData.PASSWORD, receiverEndPoint);
        assertTrue("Failed to logon", endPoint.isPresent());
        return endPoint.get();
    }

}
