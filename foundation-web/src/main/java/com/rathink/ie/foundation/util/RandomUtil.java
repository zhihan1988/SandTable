package com.rathink.ie.foundation.util;

import java.util.Random;

/**
 * Created by Hean on 2015/9/3.
 */
public class RandomUtil {
    /**
     * 百分制
     * @param low
     * @param high
     * @return
     */
    public static int random(int low, int high){
        int returnValue = 100;
        Random random = new Random();
        int randomNum = random.nextInt(high - low);
        returnValue = randomNum + low + 1;
        return returnValue;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(RandomUtil.random(60, 80));
        }

    }
}
