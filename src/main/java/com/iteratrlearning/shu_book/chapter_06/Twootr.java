package com.iteratrlearning.shu_book.chapter_06;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.iteratrlearning.shu_book.chapter_06.DeleteStatus.NOT_YOUR_TWOOT;
import static com.iteratrlearning.shu_book.chapter_06.DeleteStatus.SUCCESS;
import static com.iteratrlearning.shu_book.chapter_06.DeleteStatus.UNKNOWN_TWOOT;
import static com.iteratrlearning.shu_book.chapter_06.FollowStatus.INVALID_USER;
import static com.iteratrlearning.shu_book.chapter_06.Position.INITIAL_POSITION;

public class Twootr {

    private final TwootRepository twootRepository;
    private final UserRepository userRepository;

    public Twootr(final UserRepository userRepository, final TwootRepository twootRepository) {
        this.userRepository = userRepository;
        this.twootRepository = twootRepository;
    }

    public Optional<SenderEndPoint> onLogon(
        final String userId, final String password, final ReceiverEndPoint receiverEndPoint) {

    	/*
    	 * 
    	 * Objects.requireNonNull 사용 이유
    	 * 
				- 빠른 실패 
				  NPE 발생 원인을 빠르게 파악할 수 있다.
				
				- 가독성 
				  명시적으로 null이 아니어야 함을 표현할 수 있다
				
				- 기타
				  . 항상 같은 시점에 예외를 발생시키는 것은 시스템의 일관성을 높이고, 
				    개발자가 나머지 부분을 더 신경 쓸 수 있도록 해줌
				  . NPE를 명시적으로 던지는 것이 JVM이 발견해서 발생시키는 것 보다 성능상에 이점이 있음​
			
			Optional과 차이점
				- Object.requireNonNull -> 절대로 null이 되어서는 안된다는 것을 보장
				- Optional -> null을 반환하면 NPE가 발생할 가능성이 높은 경우, 이를 안전하게 처리하기 위해 사용
    	 * 
    	 * 참조 : https://blog.naver.com/glgkwls1/223229037477
    	 */
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(password, "password");

        // tag::optional_onLogon[]
        /*
         * InMemoryUserRepository userRepository
         * 
         * 
         * Optional을 최대 1개의 원소를 가지고 있는 특별한 Stream이라고 생각하시면 좋습니다. 
         * Optional 클래스와 Stream 클래스 간에 직접적인 구현이나 상속관계는 없지만 
         * 사용 방법이나 기본 사상이 매우 유사하기 때문입니다. 
         * 
         * Stream 클래스가 가지고 있는 map()이나 flatMap(), filter()와 같은 메소드를 
         * Optional도 가지고 있습니다. 
         * 따라서 Stream을 능숙하게 다루시는 분이시라면 Optional도 어렵지 않게 다루실 수 있으실 겁니다.
         * 
         * 참조 : https://www.daleseo.com/java8-optional-effective/
         * 
         */
        var authenticatedUser = userRepository
            .get(userId)
            .filter(userOfSameId ->
            {
            	/*
            	 * var, 타입 추론
            	 * 타입 추론이란 코드 작성 당시 타입이 정해지지 않았지만, 컴파일러가 그 타입을 유추하는 것이다.
            	 *  
            	 * 개발자가 변수의 타입을 명시적으로 적어주지 않고도, 
            	 * 컴파일러가 알아서 이 변수의 타입을 대입된 리터럴로 추론하는 것
            	 * 
            	 * 즉, hashedPassword를 byte[] 추론
            	 */
                var hashedPassword = KeyGenerator.hash(password, userOfSameId.getSalt());
                /*
                 * hashedPassword : [B@54dcfa5a
                 * userOfSameId.getPassword() : [B@301ec38b
                 */
                //System.out.println("hashedPassword : " + hashedPassword);
                //System.out.println("userOfSameId.getPassword() : " + userOfSameId.getPassword());
                
                return Arrays.equals(hashedPassword, userOfSameId.getPassword());
            });

        /*
         * ifPresent()
         * 
         * 값이 있으면, true, 없으면, false
         * 
         * 
         */
        authenticatedUser.ifPresent(user ->
        {
        	// 유효한 트우트 유저인 경우.
            user.onLogon(receiverEndPoint);
            twootRepository.query(
                new TwootQuery()
                    .inUsers(user.getFollowing())
                    .lastSeenPosition(user.getLastSeenPosition()),
                user::receiveTwoot);
            userRepository.update(user);
        });

        return authenticatedUser.map(user -> new SenderEndPoint(user, this));
        // end::optional_onLogon[]
    }

    public RegistrationStatus onRegisterUser(final String userId, final String password) {
    	/*
    	 * 해쉬(hash)와 솔트(salt)
    	 * https://velog.io/@milkyway/%ED%95%B4%EC%89%AChash%EC%99%80-%EC%86%94%ED%8A%B8salt
    	 */
        var salt = KeyGenerator.newSalt();
        var hashedPassword = KeyGenerator.hash(password, salt);
        var user = new User(userId, hashedPassword, salt, INITIAL_POSITION);
        /*
         * 신규 User이면 SUCCESS, 기존 User 이면 DUPLICATE
         */
        return userRepository.add(user) ? RegistrationStatus.SUCCESS : RegistrationStatus.DUPLICATE;
    }

    FollowStatus onFollow(final User follow, final String userIdToFollow) {
        return userRepository.get(userIdToFollow)
            .map(userToFollow -> userRepository.follow(follow, userToFollow))
            .orElse(INVALID_USER);
    }

    Position onSendTwoot(final String id, final User user, final String content) {
        var userId = user.getId();
        var twoot = twootRepository.add(id, userId, content);
        // tag::stream_onSendTwoot[]
        user.followers()
            .filter(User::isLoggedOn)
            .forEach(follower ->
            {
                follower.receiveTwoot(twoot);
                userRepository.update(follower);
            });
        // end::stream_onSendTwoot[]

        return twoot.getPosition();
    }

    DeleteStatus onDeleteTwoot(final String userId, final String id) {
        return twootRepository
            .get(id)
            .map(twoot ->
            {
                var canDeleteTwoot = twoot.getSenderId().equals(userId);
                if (canDeleteTwoot) {
                    twootRepository.delete(twoot);
                }
                return canDeleteTwoot ? SUCCESS : NOT_YOUR_TWOOT;
            })
            .orElse(UNKNOWN_TWOOT);
    }
}
