package com.rathink.iix.manufacturing.component;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.taglib.PageEntity;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.iix.ibase.component.CampaignParty;
import com.rathink.iix.ibase.component.MemoryCampaign;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ix.manufacturing.model.Market;
import com.rathink.ix.manufacturing.model.MarketOrder;
import com.rathink.ix.manufacturing.model.MarketOrderChoice;

import java.util.*;

/**
 * Created by Hean on 2016/2/2.
 */
public class MarketOrderParty extends CampaignParty<String> {
    public static String TYPE = "ORDER";
    public enum Status {
        DOING, DONE
    }
    private Queue<String> marketQueue;  //市场队列 例：LOCAL_P1
    private Map<String, Queue<String>> marketCompanyQueueMap;   //key:market;value:companyId组成的queue
    private Map<String, List<MarketOrderChoice>> marketOrderChoiceMap = new HashMap<>();

    public MarketOrderParty(MemoryCampaign memoryCampaign,Queue<String> marketQueue,Map<String,Queue<String>> marketCompanyQueueMap) {
        super(memoryCampaign, TYPE);
        this.marketQueue = marketQueue;
        this.marketCompanyQueueMap = marketCompanyQueueMap;

        BaseManager baseManager = (BaseManager) ApplicationContextUtil.getBean("BaseManagerImpl");
        for (String market : marketCompanyQueueMap.keySet()) {
            Integer companyNum = marketCompanyQueueMap.get(market).size();
            XQuery xQuery = new XQuery();
            String hql = "from IndustryResourceChoice where industryResource.name = :baseType and type = :type";
            xQuery.setHql(hql);
            xQuery.put("baseType", EManufacturingChoiceBaseType.MARKET_ORDER.name());
            xQuery.put("type", market);
            PageEntity pageEntity = new PageEntity();
            pageEntity.setIndex(1);
            pageEntity.setSize(companyNum);
            xQuery.setPageEntity(pageEntity);
            List<IndustryResourceChoice> industryResourceChoiceList = baseManager.listPageInfo(xQuery).getList();
            if (industryResourceChoiceList != null) {
                List<MarketOrderChoice> marketOrderChoiceList = new LinkedList<>();
                for (IndustryResourceChoice industryResourceChoice : industryResourceChoiceList) {
                    marketOrderChoiceList.add(new MarketOrderChoice(industryResourceChoice));
                }
                marketOrderChoiceMap.put(market, marketOrderChoiceList);
            }
        }
        setStatus(Status.DOING.name());
    }

    public void chooseOrder(String companyId, String orderId) {

        Iterator<MarketOrderChoice> marketOrderIterator = marketOrderChoiceMap.get(getCurrentMarket()).iterator();
        while (marketOrderIterator.hasNext()) {
            MarketOrderChoice marketOrderChoice = marketOrderIterator.next();
            if (marketOrderChoice.getIndustryResourceChoice().getId().equals(orderId)) {
                marketOrderChoice.setOwnerCompany(getCurrentCompany());
                break;
            }
        }

        marketCompanyQueueMap.get(getCurrentMarket()).poll();
        super.join(companyId);
    }

    public void giveUp(String companyId) {
        marketCompanyQueueMap.get(getCurrentMarket()).poll();
        super.join(companyId);
    }

    @Override
    public void iNotify() {
        if (marketQueue.isEmpty()) {
            //结束竞单环节
            setStatus(Status.DONE.name());
        } else {
            //进入下一个市场环节
            marketQueue.poll();
        }
    }

    /**
     * 当前正在进行的市场环节（例如 本地市场_P1）
     * @return
     */
    public String getCurrentMarket() {
        return marketQueue.peek();
    }

    /**
     * 当前正在操作的企业
     * @return
     */
    public String getCurrentCompany() {
        return marketCompanyQueueMap.get(getCurrentMarket()).peek();
    }


    public String getCurrentStatus() {
        return getStatus();
    }

    public List<MarketOrderChoice> getCurrentMarketOrderChoiceList() {
        return marketOrderChoiceMap.get(getCurrentMarket());
    }
}
