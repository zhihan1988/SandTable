package com.rathink.iix.manufacturing.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.team.model.ECompanyStatus;
import com.rathink.ie.foundation.util.GenerateUtil;
import com.rathink.iix.ibase.component.CampaignServer;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.iix.ibase.component.TermParty;
import com.rathink.iix.ibase.service.IBaseService;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCampaign;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCompany;
import com.rathink.iix.manufacturing.component.MarketBiddingParty;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.work.model.IndustryResource;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.internet.EAccountEntityType;
import com.rathink.ix.manufacturing.EManufacturingAccountEntityType;
import com.rathink.ix.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ix.manufacturing.EManufacturingSerialGroup;
import com.rathink.ix.manufacturing.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Hean on 2016/2/2.
 */
@Service(value = "manufacturingService")
public class ManufacturingService extends IBaseService<ManufacturingMemoryCampaign> {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingService.class);

    @Autowired
    protected ManufacturingPartService manufacturingPartService;

    @Override
    protected void init(String campaignId){
        final Integer INIT_CAMPAIGN_DATE = 0;

        BaseManager baseManager = (BaseManager) ApplicationContextUtil.getBean("baseManagerImpl");
        Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), campaignId);
        campaign.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
        campaign.setStatus(Campaign.Status.RUN.getValue());
        baseManager.saveOrUpdate(Campaign.class.getName(), campaign);

        XQuery xQuery = new XQuery();
        xQuery.setHql("from Company where campaign.id = :campaignId");
        xQuery.put("campaignId", campaignId);
        List<Company> companyList = baseManager.listObject(xQuery);
        if (companyList != null) {
            for (Company company : companyList) {
                company.setStatus(ECompanyStatus.NORMAL.name());
                company.setCurrentCampaignDate(INIT_CAMPAIGN_DATE);
                baseManager.saveOrUpdate(Company.class.getName(), company);
            }
        }


        //初始化内存对象
        ManufacturingMemoryCampaign memoryCampaign = new ManufacturingMemoryCampaign(campaign);
        CampaignServer.putMemoryCampaign(campaignId, memoryCampaign);
        for (Company company : companyList) {
            ManufacturingMemoryCompany memoryCompany = new ManufacturingMemoryCompany(company, memoryCampaign);
            memoryCampaign.putMemoryCompany(company.getId(), memoryCompany);
        }


        //----------------初始化part begin
        String industryId = campaign.getIndustry().getId();
        //产品线
        IndustryResource produceLineResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
        List<IndustryResourceChoice> produceLineChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(produceLineResource.getId());
        //原料
        IndustryResource materialResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MATERIAL.name());
        List<IndustryResourceChoice> materialChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(materialResource.getId());
        //产品
        IndustryResource productResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCT.name());
        List<IndustryResourceChoice> productChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(productResource.getId());
        //市场
        IndustryResource marketResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET.name());
        List<IndustryResourceChoice> marketChoiceList = industryResourceChoiceManager.listIndustryResourceChoice(marketResource.getId());


        if (companyList != null) {
            for (Company company : companyList) {
                ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(company.getId());

                for (IndustryResourceChoice produceLineChoice : produceLineChoiceList) {
                    ProduceLine produceLine = new ProduceLine();
                    produceLine.setId(GenerateUtil.generate());
                    produceLine.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                    produceLine.setDept(produceLineResource.getDept());
                    produceLine.setStatus(ProduceLine.Status.UN_BUILD.name());
                    produceLine.setCampaign(campaign);
                    produceLine.setCompany(company);
                    produceLine.setName(produceLineChoice.getName());
                    memoryCompany.getProduceLineMap().put(produceLine.getId(), produceLine);
                }
                for (IndustryResourceChoice materialChoice : materialChoiceList) {
                    Material material = new Material();
                    material.setId(GenerateUtil.generate());
                    material.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                    material.setName(materialChoice.getName());
                    material.setType(materialChoice.getType());
                    material.setAmount(0);
                    material.setCampaign(campaign);
                    material.setCompany(company);
                    material.setStatus(Material.Status.NORMAL.name());
                    memoryCompany.getMaterialMap().put(material.getId(), material);
                }
                for (IndustryResourceChoice productChoice : productChoiceList) {
                    Product product = new Product();
                    product.setId(GenerateUtil.generate());
                    product.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                    product.setAmount(0);
                    product.setName(productChoice.getName());
                    product.setType(productChoice.getType());
                    product.setAmount(0);
                    Integer developNeedCycle = Product.Type.valueOf(productChoice.getType()).getDevelopNeedCycle();
                    product.setDevelopNeedCycle(developNeedCycle);
                    product.setCampaign(campaign);
                    product.setCompany(company);
                    if (productChoice.getType().equals("P1")) {
                        product.setStatus(Product.Status.DEVELOPED.name());
                    } else {
                        product.setStatus(Product.Status.UNDEVELOPED.name());
                    }
                    memoryCompany.getProductMap().put(product.getId(), product);
                }
                for (IndustryResourceChoice marketChoice : marketChoiceList) {
                    Market market = new Market();
                    market.setId(GenerateUtil.generate());
                    market.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                    market.setName(marketChoice.getName());
                    market.setType(marketChoice.getType());
                    Integer devotionNeedCycle = Market.Type.valueOf(marketChoice.getType()).getDevotionNeedCycle();
                    market.setDevotionNeedCycle(devotionNeedCycle);
                    market.setCompany(company);
                    market.setCampaign(campaign);
                    if (market.getType().equals(Market.Type.LOCAL.name())) {
                        market.setStatus(Market.Status.DEVELOPED.name());
                    } else {
                        market.setStatus(Market.Status.UNDEVELOPED.name());
                    }
                    memoryCompany.getMarketMap().put(market.getId(), market);
                }


            }
        }

        //----------------初始化account begin
        for (MemoryCompany memoryCompany : memoryCampaign.getMemoryCompanyMap().values()) {
            Integer currentPeriodIncome = 100;
            Account incomeAccount = accountManager.packageAccount(String.valueOf(currentPeriodIncome), EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), memoryCompany.getCompany());
            memoryCompany.addAccount(incomeAccount);
        }
    }

    @Override
    protected void beforeProcess() {
        Integer currentCampaignDate = memoryCampaign.getCampaign().getCurrentCampaignDate();
        for (Company company : companyList) {
            ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(company.getId());
            //杂费
            processOthers(memoryCompany);
            //贷款
            processLoan(memoryCompany);

            if (currentCampaignDate % 4 == 0) {
                //延迟交付
                processUnDeliveredOrder(memoryCompany);
            }
        }
    }

    @Override
    protected void afterProcess() {

        for (Company company : companyList) {
            ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(company.getId());
            //原材料入库
            processMaterial(memoryCompany);
            //产品研发投入
            processProductDevotion(memoryCompany);
            //更新生产线状态
            processUpdateBuilding(memoryCompany);
            //更新生产完成入库
            processUpdateProduce(memoryCompany);
            //转产
            processRebuild(memoryCompany);
            //市场投入周期
            processMarketAreaDevotion(memoryCompany);
            //销售产品所得费用
            processOrderAccount(memoryCompany);

        }
    }

    @Override
    protected void randomChoice() {

        String industryId = campaign.getIndustry().getId();

        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        if (currentCampaignDate % 4 == 1) {
            //市场投放
            IndustryResource marketFeeResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET_FEE.name());
            marketFeeResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketFeeResource.getId())));
            memoryCampaign.putIndustryResource(EManufacturingChoiceBaseType.MARKET_FEE.name(), marketFeeResource);

            memoryCampaign.getCampaignPartyMap().put(MarketBiddingParty.TYPE, new MarketBiddingParty(memoryCampaign));
        }

        IndustryResource longTermLoanResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.LOAN_LONG_TERM.name());
        longTermLoanResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(longTermLoanResource.getId())));
        memoryCampaign.putIndustryResource(EManufacturingChoiceBaseType.LOAN_LONG_TERM.name(), longTermLoanResource);

        IndustryResource shortTermLoanResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.LOAN_SHORT_TERM.name());
        shortTermLoanResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(shortTermLoanResource.getId())));
        memoryCampaign.putIndustryResource(EManufacturingChoiceBaseType.LOAN_SHORT_TERM.name(), shortTermLoanResource);

        IndustryResource usuriousLoan = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.LOAN_USURIOUS.name());
        usuriousLoan.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(usuriousLoan.getId())));
        memoryCampaign.putIndustryResource(EManufacturingChoiceBaseType.LOAN_USURIOUS.name(), usuriousLoan);

        memoryCampaign.getCampaignPartyMap().put(TermParty.TYPE, new TermParty(memoryCampaign));
    }

    //原材料采购
    protected void processMaterial(ManufacturingMemoryCompany memoryCompany) {
        memoryCompany.getMaterialMap().values().stream()
                .filter(material -> material.getStatus().equals(Material.Status.PURCHASING.name()))
                .forEach(material -> {
                    Integer amount = material.getAmount();
                    material.setAmount(amount + material.getPurchasingAmount());
                    material.setStatus(Material.Status.NORMAL.name());
                });
    }

    //产品研发投入
    protected void processProductDevotion(ManufacturingMemoryCompany memoryCompany){
        memoryCompany.getProductMap().values().stream()
                .filter(product -> product.getStatus().equals(Product.Status.DEVELOPING.name()))
                .forEach(product -> {
                    Integer developNeedCycle = product.getDevelopNeedCycle();
                    developNeedCycle--;
                    product.setDevelopNeedCycle(developNeedCycle);
                    if (developNeedCycle == 0) {
                        product.setStatus(Product.Status.DEVELOPED.name());
                    } else {
                        product.setStatus(Product.Status.UNDEVELOPED.name());
                    }

                });
    }

    //更新生产线状态
    protected void processUpdateBuilding(ManufacturingMemoryCompany memoryCompany){
        memoryCompany.getProduceLineMap().values().stream()
                .filter(produceLine -> produceLine.getStatus().equals(ProduceLine.Status.BUILDING.name()))
                .forEach(produceLine -> {
                    produceLine.setStatus(ProduceLine.Status.BUILT.name());
                });
    }

    //更新生产 完成入库
    protected void processUpdateProduce(ManufacturingMemoryCompany memoryCompany){
        memoryCompany.getProduceLineMap().values().stream()
                .filter(produceLine -> produceLine.getStatus().equals(ProduceLine.Status.PRODUCING.name()))
                .forEach(produceLine -> {
                    Integer produceNeedCycle = produceLine.getProduceNeedCycle();
                    --produceNeedCycle;
                    produceLine.setProduceNeedCycle(produceNeedCycle);
                    if (produceNeedCycle == 0) {
                        produceLine.setStatus(ProduceLine.Status.FREE.name());

                        Product product = memoryCompany.getProductByType(produceLine.getProduceType());
                        product.setAmount(product.getAmount() + 1);

                    }
                });
    }

    protected void processRebuild(ManufacturingMemoryCompany memoryCompany) {
        memoryCompany.getProduceLineMap().values().stream()
                .filter(produceLine -> produceLine.getStatus().equals(ProduceLine.Status.REBUILDING.name()))
                .forEach(produceLine -> {
                    Integer lineBuildNeedCycle = produceLine.getLineBuildNeedCycle();
                    --lineBuildNeedCycle;
                    produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);
                    if (lineBuildNeedCycle == 0) {
                        produceLine.setStatus(ProduceLine.Status.FREE.name());
                    }
                });
    }

    //市场区域开发费用
    protected void processMarketAreaDevotion(ManufacturingMemoryCompany memoryCompany) {
        memoryCompany.getMarketMap().values().stream()
                .filter(market -> market.getStatus().equals(Market.Status.DEVELOPING.name()))
                .forEach(market -> {
                    Integer devotionNeedCycle = market.getDevotionNeedCycle();
                    market.setDevotionNeedCycle(--devotionNeedCycle);
                    if (devotionNeedCycle == 0) {
                        market.setStatus(Market.Status.DEVELOPED.name());
                    } else {
                        market.setStatus(Market.Status.UNDEVELOPED.name());
                    }
                });
    }

    //处理订单账款
    private void processOrderAccount(ManufacturingMemoryCompany memoryCompany) {
        Company company = memoryCompany.getCompany();
        memoryCompany.getMarketOrderMap().values().stream()
                .filter(marketOrder -> marketOrder.getStatus().equals(MarketOrder.Status.DELIVERED.name()))
                .forEach(marketOrder -> {
                    Integer needAccountCycle = marketOrder.getNeedAccountCycle();
                    needAccountCycle--;
                    marketOrder.setNeedAccountCycle(needAccountCycle);
                    if (needAccountCycle == 0) {
                        marketOrder.setStatus(MarketOrder.Status.FINISH.name());
                        Integer fee = marketOrder.getTotalPrice();
                        Integer profit = marketOrder.getProfit();
                        Integer productFee = fee - profit;//产品费用（成本费+加工费）

                        Account account = accountManager.packageAccount(String.valueOf(profit), EManufacturingAccountEntityType.COMPANY_CASH.name(),
                                EManufacturingAccountEntityType.ORDER_FEE.name(), company);
                        memoryCompany.addAccount(account);
                        Account account2 = accountManager.packageAccount(String.valueOf(productFee), EManufacturingAccountEntityType.COMPANY_CASH.name(),
                                EManufacturingAccountEntityType.FLOATING_CAPITAL_PRODUCT.name(), company);
                        memoryCompany.addAccount(account2);
                    }
                });
    }

    //杂费
    protected void processOthers(ManufacturingMemoryCompany memoryCompany){
        Company company = memoryCompany.getCompany();

        //行政管理费
        Integer administrationFee = 1;
        Account administrationAccount = accountManager.packageAccount(String.valueOf(administrationFee), EManufacturingAccountEntityType.OTHER.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(administrationAccount);
        logger.info("行政管理费：第{}期-{}-{}", company.getCurrentCampaignDate(), company.getName(), administrationFee);



        if (company.getCurrentCampaignDate() % 4 == 0) {

            //生产线维护费
            List<ProduceLine> produceLineList = memoryCompany.getProduceLineMap().values().stream()
                    .filter(produceLine -> !produceLine.getStatus().equals(ProduceLine.Status.UN_BUILD.name()))
                    .collect(Collectors.toList());
            if (produceLineList != null && !produceLineList.isEmpty()) {
                Integer produceLineMaintenanceFee = produceLineList.size() * 1;
                Account maintenanceAccount = accountManager.packageAccount(String.valueOf(produceLineMaintenanceFee), EManufacturingAccountEntityType.OTHER.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
                memoryCompany.addAccount(maintenanceAccount);
                logger.info("生产线维护费：第{}期-{}-{}", company.getCurrentCampaignDate(), company.getName(), produceLineMaintenanceFee);
            }


            //厂房租赁费
            Integer lineFactoryRent = 0;
            Set<String> lineNameSet = new HashSet<>();
            for (ProduceLine produceLine : produceLineList) {
                lineNameSet.add(produceLine.getName());
            }
            if (lineNameSet.contains("生产线1") || lineNameSet.contains("生产线2") || lineNameSet.contains("生产线3") || lineNameSet.contains("生产线4")) {
                lineFactoryRent += 4;
            } else if (lineNameSet.contains("生产线5") || lineNameSet.contains("生产线6") || lineNameSet.contains("生产线7")) {
                lineFactoryRent += 3;
            } else if (lineNameSet.contains("生产线8")) {
                lineFactoryRent += 2;
            }
            Account lineFactoryRentAccount = accountManager.packageAccount(String.valueOf(lineFactoryRent), EManufacturingAccountEntityType.LINE_FACTORY_RENT_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
            memoryCompany.addAccount(lineFactoryRentAccount);
            logger.info("厂房租赁费：第{}期-{}-{}", company.getCurrentCampaignDate(), company.getName(), lineFactoryRent);

        }
    }

    //贷款
    protected void processLoan(ManufacturingMemoryCompany memoryCompany) {
        Company company = memoryCompany.getCompany();
        memoryCompany.getLoanMap().values().stream()
                .filter(loan -> loan.getStatus().equals(Loan.Status.NORMAL))
                .forEach(loan -> {
                    Integer needRepayCycle = loan.getNeedRepayCycle();
                    needRepayCycle--;
                    loan.setNeedRepayCycle(needRepayCycle);

                    Loan.Type loanType = Loan.Type.valueOf(loan.getType());
                    Integer fee = 0;
                    Integer rateFee = 0;
                    if (company.getCurrentCampaignDate() % 4 == 0) {//年底付息
                        rateFee += loan.getMoney() * loanType.getYearRate() / 100;
                    }
                    if (needRepayCycle == 0) {//到期还款
                        loan.setStatus(Loan.Status.FINISH.name());
                        fee += loan.getMoney();
                    }
                    if (fee != 0) {
                        //需要保证Loan.Type.name跟EManufacturingAccountEntityType中的贷款的名字一直
                        Account account = accountManager.packageAccount(String.valueOf(fee), loanType.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
                        memoryCompany.addAccount(account);
                    }
                    if (rateFee != 0) {
                        Account account = accountManager.packageAccount(String.valueOf(rateFee), EManufacturingAccountEntityType.INTEREST.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
                        memoryCompany.addAccount(account);
                    }
                });
    }

    //处理未交付的订单 扣费等
    protected void processUnDeliveredOrder(ManufacturingMemoryCompany memoryCompany) {
        Integer fee = 0;
        List<MarketOrder> marketOrderList = memoryCompany.getMarketOrderMap().values().stream()
                .filter(marketOrder -> marketOrder.getStatus().equals(MarketOrder.Status.NORMAL.name()))
                .collect(Collectors.toList());

        if (marketOrderList != null && !marketOrderList.isEmpty()) {
            for (MarketOrder marketOrder : marketOrderList) {
                fee += Integer.valueOf(marketOrder.getTotalPrice());
            }
            fee = fee * 10 / 100 + 1;

            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.ORDER_DELAY_FEE.name()
                    , EManufacturingAccountEntityType.COMPANY_CASH.name(), memoryCompany.getCompany());
            memoryCompany.addAccount(account);
        }
    }
}
