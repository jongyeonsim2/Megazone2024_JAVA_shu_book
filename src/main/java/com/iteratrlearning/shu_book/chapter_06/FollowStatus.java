package com.iteratrlearning.shu_book.chapter_06;

// tag::FollowStatus[]
public enum FollowStatus {
    SUCCESS, // follow(구독) 가 성공.
    INVALID_USER, // follow 할 수 없는 사용자.
    ALREADY_FOLLOWING // 벌써 follow(구독) 을 하고 있는 사용자.
}
// end::FollowStatus[]
