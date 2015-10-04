package com.rathink.ie.foundation.campaign.model;

import java.util.NoSuchElementException;

/**
 * Created by Hean on 2015/8/24.
 */
public class CampaignDateUtil {

    public static Integer getPreCampaignDate(Integer campaignDate) {
        return campaignDate - 1;
    }

    public static Integer getNextCampaignDate(Integer campaignDate) {
        return campaignDate + 1;
    }
    /**
     *
     * @param campaignDate
     * @param term  1：月  3：季度  12:年
     * @return
     */
    public static String formatCampaignDate(Integer campaignDate, Integer term) {
        Integer total = campaignDate * term;
        int year = (total - 1) / 12;
        int month = total - 12 * year;

        int showYear = year + 1;
        int showMonth = month;

        StringBuffer stringBuffer = new StringBuffer();

        if (term == 1) {
            stringBuffer.append("第" + showYear + "年，第" + showMonth + "月份");
        } else if (term == 3) {
            int quarter = (month - 1) / 3 + 1;
            stringBuffer.append("第" + showYear + "年，第" + quarter + "季度");
        } else if (term == 12) {
            stringBuffer.append("第" + showYear + "年");
        } else {
            throw new NoSuchElementException("term:" + term);
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(CampaignDateUtil.formatCampaignDate(1, 12));
        System.out.println(CampaignDateUtil.formatCampaignDate(8, 12));
        System.out.println(CampaignDateUtil.formatCampaignDate(12, 12));
        System.out.println(CampaignDateUtil.formatCampaignDate(13, 12));
    }

    /*private static String calculateQuarter(String month) {
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
    }*/
}
