package com.rathink.ie.base.component;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ie.manufacturing.model.MarketOrder;

import java.util.*;

/**
 * Created by Hean on 2015/10/31.
 */
public class DevoteCycle {
    private CampaignContext campaignContext;

    private Integer status = 1;//1.正在投票  2.投票完成，正在选择订单  3选择订单完成

    private Set<String> finishDevoteCompanySet = new HashSet<>();

    private List<String> marketList;
    private Queue<String> marketQueue;//由市场区域以及产品类型组成的队列
    private Map<String, List<IndustryResourceChoice>> marketOrderChoiceMap;
    private Map<String,Queue<String>>     companyIdQueueMap = new HashMap<>();
    private Integer currentLeftOperationNum;
    private String currentMarket;

    public DevoteCycle(CampaignContext campaignContext, List<String> marketList, Map<String, List<IndustryResourceChoice>> marketOrderChoiceMap) {
        this.campaignContext = campaignContext;
        this.marketList = marketList;
        this.marketQueue = new LinkedList<>(marketList);
        this.marketOrderChoiceMap = marketOrderChoiceMap;
    }

    /**
     * 企业完成投标环节
     * @param companyId
     */
    public synchronized void finishDevote(String companyId) {
        finishDevoteCompanySet.add(companyId);
        if (campaignContext.getCompanyTermContextMap().size() == finishDevoteCompanySet.size()) {//全部投标完成时
            status = 2;

            //按市场类型分别获得所有的投标决策并按金额高低排序
            BaseManager baseManager = (BaseManager) ApplicationContextUtil.getBean("baseManager");
            for (String market : marketList) {
                XQuery xQuery = new XQuery();
                String hql = "from CompanyTermInstruction where industryResourceChoice.type=:type and companyTerm.campaignDate = :campaignDate";
                xQuery.setHql(hql);
//                xQuery.put("baseType", EManufacturingChoiceBaseType.MARKET.name());
                xQuery.put("type", market);
                xQuery.put("campaignDate", campaignContext.getCampaign().getCurrentCampaignDate());
                List<CompanyTermInstruction> instructionList = baseManager.listObject(xQuery);
                instructionList.sort(new Comparator<CompanyTermInstruction>() {
                    @Override
                    public int compare(CompanyTermInstruction o1, CompanyTermInstruction o2) {
                        return Integer.valueOf(o1.getValue()) - Integer.valueOf(o2.getValue());
                    }
                });
                Queue<String> companyIdQueue = new LinkedList<>();
                if (instructionList != null) {
                    for (CompanyTermInstruction companyTermInstruction : instructionList) {
                        companyIdQueue.offer(companyTermInstruction.getCompanyTerm().getCompany().getId());
                    }
                }
                companyIdQueueMap.put(market, companyIdQueue);
            }
        }
    }

    /**
     * 企业针对当前环节挑选订单
     * @param orderId
     * @return
     */
    public synchronized String chooseOrder(String orderId) {
        Iterator<IndustryResourceChoice> marketOrderIterator = marketOrderChoiceMap.get(marketQueue.peek()).iterator();
        while (marketOrderIterator.hasNext()) {
            IndustryResourceChoice marketOrderChoice = marketOrderIterator.next();
            if (marketOrderChoice.getId().equals(orderId)) {
                marketOrderIterator.remove();

                currentLeftOperationNum--;

                Queue queue = companyIdQueueMap.get(currentMarket);
                queue.offer(queue.poll());//循环队列
                break;
            }
        }

        if (currentLeftOperationNum == 0) {
            currentMarket = marketQueue.poll();
        }

        return currentMarket;
    }

    /**
     * 企业放弃该环节挑选订单的机会
     * @return
     */
    public synchronized String giveUp() {
        currentLeftOperationNum--;

        Queue queue = companyIdQueueMap.get(currentMarket);
        queue.offer(queue.poll());//循环队列
        if (currentLeftOperationNum == 0) {
            marketQueue.poll();

        }

        return marketQueue.peek();
    }

    /**
     * 当前正在进行的市场环节（例如 本地市场_P1）
     * @return
     */
    public String getCurrentMarket() {
        return currentMarket;
    }

    /**
     * 当前正在操作的企业
     * @return
     */
    public String getCurrentCompany() {
        return companyIdQueueMap.get(currentMarket).peek();
    }
}
