package com.rathink.ie.foundation.util;

/**
 * Created by Hean on 2016/2/3.
 */
public class GenerateUtil {

    public static String generate(){
        String dateId = Long.toString(System.currentTimeMillis(), 36);


        Double numIds = Math.random();
        String numIds2 = numIds.toString();
        numIds2 = numIds2.substring(numIds2.indexOf(".")+1,numIds2.length()-1);
        if (numIds2.length()<=17){
            numIds2 = fillWithZero(numIds2,17);
        }
        String numIds3 = numIds2.substring(1, 15);
        String numId = Long.toString(Long.parseLong(numIds3), 36);

        StringBuilder stringBuilder = new StringBuilder(16);

        stringBuilder
                //.append(fillWithZero(branchId, 4))
                .append(fillWithZero(dateId, 8))
                .append(fillWithZero(numId, 8));

        return stringBuilder.toString();
    }

    private static String fillWithZero(String str, Integer length) {

        StringBuilder stringBuilder = new StringBuilder(length);

        stringBuilder.append(str);
        if (str.length() > length) {
            stringBuilder.deleteCharAt(length);
        } else if (str.length() < length) {
            int tempLength = length - str.length();
            for (int i = 0; i < tempLength; i++) {
                stringBuilder.insert(0, "0");
            }
        }

        return stringBuilder.toString();
    }
}
