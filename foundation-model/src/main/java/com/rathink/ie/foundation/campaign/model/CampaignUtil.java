package com.rathink.ie.foundation.campaign.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

/**
 * Created by Hean on 2015/8/24.
 */
public class CampaignUtil {

    /**
     * 获取上一轮赛季
     * @param campaignDate
     * @return
     */
    public static Integer getPreCampaignDate(Integer campaignDate, Integer term) {
        return campaignDate + term;
    }

    /**
     * 获取下一轮赛季
     * @param campaignDate
     * @return
     */
    public static Integer getNextCampaignDate(Integer campaignDate, Integer term) {
        return campaignDate - term;
    }

    /**
     *
     * @param campaignDate
     * @param term  1：月  3：季度  12:年
     * @return
     */
    public static String formatCampaignDate(Integer campaignDate, Integer term) {
        LocalDate nowDate = LocalDate.now();
        nowDate = nowDate.plus(campaignDate, ChronoUnit.MONTHS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
        String formatNowDate = formatter.format(nowDate);
        String year = formatNowDate.substring(0, 2);
        String month = formatNowDate.substring(2, 4);
        StringBuffer stringBuffer = new StringBuffer();

        if (term == 1) {
            stringBuffer.append("第" + year + "年，第" + month + "月份");
        } else if (term == 3){
            String quarter = CampaignUtil.calculateQuarter(month);
            stringBuffer.append("第" + year + "年，第" + quarter + "季度");
        } else if (term == 12) {
            stringBuffer.append("第" + year + "年");
        } else {
            throw new NoSuchElementException("term:" + term);
        }
        return stringBuffer.toString();
    }

    private static String calculateQuarter(String month) {
        String quarter;
        switch (month) {
            case "01":
            case "02":
            case "03":
                quarter = "01";
                break;
            case "04":
            case "05":
            case "06":
                quarter = "02";
                break;
            case "07":
            case "08":
            case "09":
                quarter = "03";
                break;
            case "10":
            case "11":
            case "12":
                quarter = "04";
                break;
            default:
                throw new NoSuchElementException("month:" + month);
        }
        return quarter;
    }
}
