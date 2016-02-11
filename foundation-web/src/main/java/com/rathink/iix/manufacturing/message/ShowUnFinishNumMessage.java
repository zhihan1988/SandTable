package com.rathink.iix.manufacturing.message;

/**
 * Created by Hean on 2016/2/6.
 */
public class ShowUnFinishNumMessage extends Message<Integer> {

    public ShowUnFinishNumMessage(Integer num) {
        super(ShowUnFinishNumMessage.class.getSimpleName(), num);
    }

}
