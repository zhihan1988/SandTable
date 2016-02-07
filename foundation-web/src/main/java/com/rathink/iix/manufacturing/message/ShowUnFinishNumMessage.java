package com.rathink.iix.manufacturing.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hean on 2016/2/6.
 */
public class ShowUnFinishNumMessage extends Message<Integer> {

    public ShowUnFinishNumMessage(Integer num) {
        super(ShowUnFinishNumMessage.class.getSimpleName(), num);
    }

}
