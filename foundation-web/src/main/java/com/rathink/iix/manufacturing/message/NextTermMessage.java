package com.rathink.iix.manufacturing.message;

/**
 * Created by Hean on 2016/2/6.
 */
public class NextTermMessage extends Message<Boolean> {
    public NextTermMessage(Boolean message) {
        super(NextTermMessage.class.getSimpleName(), message);
    }
}
