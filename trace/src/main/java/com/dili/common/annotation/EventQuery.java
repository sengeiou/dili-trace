package com.dili.common.annotation;

import java.util.List;

public interface EventQuery {
    public List<MessageEvent> queryEvents(Long id);
}
