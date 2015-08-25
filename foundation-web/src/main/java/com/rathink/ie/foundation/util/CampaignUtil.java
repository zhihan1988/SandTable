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
     * 获得当前比赛时间
     * @return
     */
    public static String getCurrentCampaignDate(){
        LocalDate nowDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMM");
        return joinSeason(nowDate.format(format));
    }

    /**
     * 获得上一轮比赛的时间
     * @param campaignDate
     * @return
     */
    public static String getPreCampaignDate(String campaignDate) {
        return getPreCampaignDate(campaignDate, -1);
    }

    /**
     * 获得下一轮比赛的时间
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
        Integer intMonth = Integer.parseInt(month);
        if (intMonth >= 1 && intMonth <= 3) {
            season = "01";
        } else if (intMonth >= 4 && intMonth <= 6) {
            season = "02";
        } else if (intMonth >= 7 && intMonth <= 9) {
            season = "03";
        } else if (intMonth >= 10 && intMonth <= 12) {
            season = "04";
        } else {
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
        System.out.println(CampaignUtil.getCurrentCampaignDate());
    }

}
