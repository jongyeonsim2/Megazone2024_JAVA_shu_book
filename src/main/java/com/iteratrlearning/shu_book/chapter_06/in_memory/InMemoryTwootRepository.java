package com.iteratrlearning.shu_book.chapter_06.in_memory;

import com.iteratrlearning.shu_book.chapter_06.*;

import java.util.*;
import java.util.function.Consumer;

// 트윗 사용자가 전송한 트윗(메세지)를 저장 및 관리하는 내부 메모리 저장소.
public class InMemoryTwootRepository implements TwootRepository {
    private final List<Twoot> twoots = new ArrayList<>();

    private Position currentPosition = Position.INITIAL_POSITION;


    // tag::query[]
    @Override
    public void query(final TwootQuery twootQuery, final Consumer<Twoot> callback) {
        if (!twootQuery.hasUsers()) {
            return;
        }

        var lastSeenPosition = twootQuery.getLastSeenPosition();
        var inUsers = twootQuery.getInUsers();

        twoots
            .stream()
            .filter(twoot -> inUsers.contains(twoot.getSenderId()))
            .filter(twoot -> twoot.isAfter(lastSeenPosition))
            .forEach(callback);
    }
    // end::query[]

    // tag::queryLoop[]
    public void queryLoop(final TwootQuery twootQuery, final Consumer<Twoot> callback) {
        if (!twootQuery.hasUsers()) {
            return;
        }

        var lastSeenPosition = twootQuery.getLastSeenPosition();
        var inUsers = twootQuery.getInUsers();

        for (Twoot twoot : twoots) {
            if (inUsers.contains(twoot.getSenderId()) &&
                twoot.isAfter(lastSeenPosition)) {
                callback.accept(twoot);
            }
        }
    }
    // end::queryLoop[]

    @Override
    public Optional<Twoot> get(final String id) {
        return twoots
            .stream()
            .filter(twoot -> twoot.getId().equals(id))
            .findFirst();
    }

    @Override
    public void delete(final Twoot twoot) {
        twoots.remove(twoot);
    }

    @Override
    public Twoot add(final String id, String userId, String content) {
        currentPosition = currentPosition.next();

        var twootPosition = currentPosition;
        var twoot = new Twoot(id, userId, content, twootPosition);
        twoots.add(twoot);
        return twoot;
    }

    @Override
    public void clear() {
        twoots.clear();
    }
}
