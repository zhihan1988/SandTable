package com.rathink.ix.manufacturing.component;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ix.ibase.component.CampContext;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import com.rathink.ix.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ix.manufacturing.model.MarketOrderChoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by Hean on 2015/10/31.
 */
public class DevoteCycle {
    private static Logger logger = LoggerFactory.getLogger(DevoteCycle.class);

    private CampContext campaignContext;

    private Integer status = 1;//1.正在投票  2.投票完成，正在选择订单  3选择订单完成

    private Set<String> finishDevoteCompanySet = new HashSet<>();

    private List<String> marketList;
    private Queue<String> marketQueue;//由市场区域以及产品类型组成的队列
    private Map<String, List<MarketOrderChoice>> marketOrderChoiceMap;
    private Integer currentLeftOperationNum;
    private String currentMarket;
    private Map<String,Queue<String>> companyIdQueueMap = new HashMap<>();
    Map<String, List<String>> companyOrderMap = new HashMap<>();//市场公司竞标排名 local_P1:company1,3000;company2,2000

    public DevoteCycle(CampContext campaignContext, List<String> marketList, Map<String, List<MarketOrderChoice>> marketOrderChoiceMap) {
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
        if (status == 1 && campaignContext.getCompanyTermContextMap().size() == finishDevoteCompanySet.size()) {//全部投标完成时
            status = 2;

            //按市场类型分别获得所有的投标决策并按金额高低排序
            BaseManager baseManager = (BaseManager) ApplicationContextUtil.getBean("baseManagerImpl");
            for (String market : marketList) {
                XQuery xQuery = new XQuery();
                String hql = "from CompanyTermInstruction where baseType=:baseType and industryResourceChoice.type=:type and companyTerm.campaignDate = :campaignDate";
                xQuery.setHql(hql);
                xQuery.put("baseType", EManufacturingChoiceBaseType.MARKET_FEE.name());
                xQuery.put("type", market);
                xQuery.put("campaignDate", campaignContext.getCampaign().getCurrentCampaignDate());
                List<CompanyTermInstruction> instructionList = baseManager.listObject(xQuery);
                instructionList.sort(new Comparator<CompanyTermInstruction>() {
                    @Override
                    public int compare(CompanyTermInstruction o1, CompanyTermInstruction o2) {
                        return Integer.valueOf(o2.getValue()) - Integer.valueOf(o1.getValue());
                    }
                });
                if (instructionList != null && instructionList.size() > 0) {
                    Queue<String> companyIdQueue = new LinkedList<>();
                    List<String> companyFeeList = new ArrayList<>();
                    for (CompanyTermInstruction companyTermInstruction : instructionList) {
                        companyIdQueue.offer(companyTermInstruction.getCompanyTerm().getCompany().getId());

                        companyFeeList.add(companyTermInstruction.getCompanyTerm().getCompany().getName()
                                + "(" + companyTermInstruction.getValue() + ")");
                    }
                    companyIdQueueMap.put(market, companyIdQueue);
                    companyOrderMap.put(market, companyFeeList);
                } else {
                    marketQueue.remove(market);
                }

            }

            //
            currentMarket = marketQueue.peek();
            this.currentLeftOperationNum = marketOrderChoiceMap.get(currentMarket).size();
        }
    }

    /**
     * 企业针对当前环节挑选订单
     * @param orderId
     * @return
     */
    public synchronized String chooseOrder(String orderId) {
        Iterator<MarketOrderChoice> marketOrderIterator = marketOrderChoiceMap.get(marketQueue.peek()).iterator();
        while (marketOrderIterator.hasNext()) {
            MarketOrderChoice marketOrderChoice = marketOrderIterator.next();
            if (marketOrderChoice.getIndustryResourceChoice().getId().equals(orderId)) {
                marketOrderChoice.setOwnerCompany(getCurrentCompany());
                currentLeftOperationNum--;

                Queue queue = companyIdQueueMap.get(currentMarket);
                queue.offer(queue.poll());//循环队列
                break;
            }
        }

        if (currentLeftOperationNum == 0) {
            //进入下一回合
            marketQueue.poll();
            currentMarket = marketQueue.peek();

            currentLeftOperationNum = currentMarket == null ? 0 : marketOrderChoiceMap.get(currentMarket).size();
        }
        if (currentMarket == null) {
            //不存在下一回合  选单环节 结束
            status = 3;
        }

        logger.info("currentLeftOperationNum:{};currentMarket:{};status:{}", currentLeftOperationNum, currentMarket, status);
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
        if (currentMarket == null) {
            status = 3;
        }
        return currentMarket;
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

    public List<MarketOrderChoice> getCurrentMarketOrdeChoiceList() {
        return marketOrderChoiceMap.get(currentMarket);
    }

    public Integer getCurrentStatus() {
        return status;
    }

    public List<String> getCompanyOrderMapByMarket() {
        return companyOrderMap.get(currentMarket);
    }
}
