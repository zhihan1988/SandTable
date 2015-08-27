package com.rathink.ie.foundation.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by Hean on 2015/8/24.
 */
public class CampaignUtil {

    /**
     * ��õ�ǰ����ʱ��
     * @return
     */
    public static String getCurrentCampaignDate(){
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMM");
        return joinSeason(nowDate.format(format));
    }

    /**
     * �����һ�ֱ�����ʱ��
     * @param campaignDate
     * @return
     */
    public static String getPreCampaignDate(String campaignDate) {
        return getPreCampaignDate(campaignDate, -1);
    }

    /**
     * �����һ�ֱ�����ʱ��
     * @param campaignDate
     * @return
     */
    public static String getNextCampaignDate(String campaignDate) {
        return getPreCampaignDate(campaignDate, 1);
    }

    private static String removeSeason(String campaignDate) {
        String year = campaignDate.substring(0, 2);
        String month = campaignDate.substring(4, 6);
        campaignDate = year + month;
        return campaignDate;
    }

    private static String joinSeason(String campaignDate) {
        String year = campaignDate.substring(0, 2);
        String month = campaignDate.substring(2, 4);
        String season;
        switch (month) {
            case "01":
            case "02":
            case "03":
                season = "01";
                break;
            case "04":
            case "05":
            case "06":
                season = "02";
                break;
            case "07":
            case "08":
            case "09":
                season = "03";
                break;
            case "10":
            case "11":
            case "12":
                season = "04";
                break;
            default:
                season = "00";
        }
        campaignDate = year + season + month;
        return campaignDate;
    }

    private static String getPreCampaignDate(String campaignDate, Integer num) {
        String noSeasonCampaignDate = removeSeason(campaignDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
        YearMonth date = YearMonth.parse(noSeasonCampaignDate, formatter);
        date = date.plus(num, ChronoUnit.MONTHS);
        return joinSeason(date.format(formatter));
    }

    public static void main(String[] args) {
       /* String date = "150308";
        for (int i = 0; i < 20; i++) {
            date = CampaignUtil.getPreCampaignDate(date);
            System.out.println(date);
        }*/
        System.out.println(CampaignUtil.joinSeason("1501"));
    }

}