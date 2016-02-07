package com.rathink.iix.manufacturing.message;

/**
 * Created by Hean on 2016/2/6.
 */
public interface IMessage<T> {
    /**
     * 获取消息类型
     * @return
     */
    String getType();

    /**
     * 获得消息内容
     * @return
     */
    T getMessage();
}
