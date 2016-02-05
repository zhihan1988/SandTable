package com.rathink.iix.manufacturing.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.ming800.core.taglib.PageEntity;
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
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.service.CompanyTermContext;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import com.rathink.ix.ibase.work.model.IndustryResource;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.internet.EAccountEntityType;
import com.rathink.ix.internet.EInstructionStatus;
import com.rathink.ix.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ix.manufacturing.EManufacturingInstructionBaseType;
import com.rathink.ix.manufacturing.EManufacturingSerialGroup;
import com.rathink.ix.manufacturing.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2016/2/2.
 */
@Service(value = "manufacturingService")
public class ManufacturingService extends IBaseService<ManufacturingMemoryCampaign> {

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
                    market.setStatus(Market.Status.NORMAL.name());
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
        for (Company company : companyList) {
            ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(company.getId());
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

            Market.Type[] marketTypeArray;
            String[] productTypeArray;
            if (currentCampaignDate == 1) {
                marketTypeArray = new Market.Type[]{Market.Type.LOCAL};
                productTypeArray = new String[]{"P1"};
            } else if (currentCampaignDate == 5) {
                marketTypeArray = new Market.Type[]{Market.Type.LOCAL, Market.Type.AREA};
                productTypeArray = new String[]{"P1", "P2"};
            } else {
                marketTypeArray = Market.Type.values();
                productTypeArray = new String[]{"P1", "P2", "P3", "P4"};
            }
            List<String> marketList = new ArrayList<>();
            for (Market.Type m : marketTypeArray) {
                for (String p : productTypeArray) {
                    marketList.add(m.name() + "_" + p);
                }
            }

            Integer companyNum = memoryCampaign.getMemoryCompanyMap().size();
            Map<String, List<MarketOrderChoice>> marketOrderChoiceMap = new HashMap<>();

            for (String market : marketList) {
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
            //todo   投票环节
//            DevoteCycle devoteCycle = new DevoteCycle(campaignContext, marketList, marketOrderChoiceMap);
//            campaignContext.setDevoteCycle(devoteCycle);
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

        memoryCampaign.addCampaignParty(new TermParty(memoryCampaign));
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

}
