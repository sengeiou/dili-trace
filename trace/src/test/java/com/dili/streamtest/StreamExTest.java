package com.dili.streamtest;

import java.util.List;

import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;

import one.util.streamex.StreamEx;

public class StreamExTest {
    @Test
    public void test() {
        List<Integer> l = StreamEx.ofNullable((Integer)null).flatCollection(itemId -> {
            return Lists.newArrayList(itemId, 2, 3);
        }).toList();
        System.out.println(l);
    }

}