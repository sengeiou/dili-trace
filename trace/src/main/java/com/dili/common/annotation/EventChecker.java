package com.dili.common.annotation;

public @interface EventChecker {
    MessageEvent type();
    String id();
    Class<? extends  EventQuery>eventQuery();
}
