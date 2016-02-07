package com.rathink.iix.manufacturing.message;

/**
 * Created by Hean on 2016/2/6.
 */
public abstract class Message<T> implements IMessage<T> {
    private String type;
    private T message;
    public Message(String type,T message) {
        this.type = type;
        this.message = message;
    }

    public String getType(){
        return type;
    }

    public T getMessage(){
        return message;
    }

}
