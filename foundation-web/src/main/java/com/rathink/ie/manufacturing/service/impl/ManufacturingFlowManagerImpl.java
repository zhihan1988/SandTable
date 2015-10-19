package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.RoundEndObserable;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.property.model.CompanyTermProperty;
import com.rathink.ie.ibase.service.AbstractFlowManager;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EAccountEntityType;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.manufacturing.*;
import com.rathink.ie.manufacturing.model.Market;
import com.rathink.ie.manufacturing.model.Material;
import com.rathink.ie.manufacturing.model.ProduceLine;
import com.rathink.ie.manufacturing.model.Product;
import com.rathink.ie.manufacturing.service.MaterialManager;
import com.rathink.ie.manufacturing.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Service(value = "manufacturingFlowManagerImpl")
public class ManufacturingFlowManagerImpl extends AbstractFlowManager {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingFlowManagerImpl.class);
    @Autowired
    private AutoSerialManager autoSerialManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private MaterialManager materialManager;

    @Override
    protected void initPartList() {

        String industryId = campaignContext.getCampaign().getIndustry().getId();
        //产品线
        IndustryResource produceLineResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
        List<IndustryResourceChoice> produceLineList = industryResourceChoiceManager.listIndustryResourceChoice(produceLineResource.getId());
        //原料
        IndustryResource materialResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MATERIAL.name());
        List<IndustryResourceChoice> materialList = industryResourceChoiceManager.listIndustryResourceChoice(materialResource.getId());
        //产品
        IndustryResource productResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.PRODUCT.name());
        List<IndustryResourceChoice> productList = industryResourceChoiceManager.listIndustryResourceChoice(productResource.getId());
        //市场
        IndustryResource marketResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET.name());
        List<IndustryResourceChoice> marketList = industryResourceChoiceManager.listIndustryResourceChoice(marketResource.getId());

        Map<String, List<CompanyPart>> companyPartMap = campaignContext.getCompanyPartMap();
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            Company company = companyTermContext.getCompanyTerm().getCompany();

            List<CompanyPart> companyPartList = new ArrayList<>();
            for (IndustryResourceChoice produceLineChoice : produceLineList) {
                ProduceLine produceLine = new ProduceLine();
                produceLine.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                produceLine.setDept(produceLineResource.getDept());
                produceLine.setStatus(ProduceLine.Status.UN_BUILD.name());
                produceLine.setCampaign(campaignContext.getCampaign());
                produceLine.setCompany(company);
                produceLine.setName(produceLineChoice.getName());
//                produceLine.setProduceType();
//                produceLine.setProduceLineType();
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
//                companyPartList.add(produceLine);
            }
            for (IndustryResourceChoice materialChoice : materialList) {
                Material material = new Material();
                material.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                material.setName(materialChoice.getName());
                material.setType(materialChoice.getType());
                material.setAmount("0");
                material.setCampaign(campaignContext.getCampaign());
                material.setCompany(company);
                material.setStatus(Material.Status.NORMAL.name());
                baseManager.saveOrUpdate(Material.class.getName(), material);
            }
            for (IndustryResourceChoice productChoice : productList) {
                Product product = new Product();
                product.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                product.setAmount(0);
                product.setName(productChoice.getName());
                product.setType(productChoice.getType());
                product.setAmount(0);
                Integer developNeedCycle = Product.Type.valueOf(productChoice.getType()).getDevelopNeedCycle();
                product.setDevelopNeedCycle(developNeedCycle);
                product.setCampaign(campaignContext.getCampaign());
                product.setCompany(company);
                product.setStatus(Product.Status.NORMAL.name());
                baseManager.saveOrUpdate(Product.class.getName(), product);
            }
            for (IndustryResourceChoice marketChoice : marketList) {
                Market market = new Market();
                market.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
                market.setName(marketChoice.getName());
                market.setType(marketChoice.getType());
                Integer devotionNeedCycle = Market.Type.valueOf(marketChoice.getType()).getDevotionNeedCycle();
                market.setDevotionNeedCycle(devotionNeedCycle);
                market.setCompany(company);
                market.setCampaign(campaignContext.getCampaign());
                market.setStatus(Market.Status.NORMAL.name());
                baseManager.saveOrUpdate(Market.class.getName(), market);
            }

            companyPartMap.put(company.getId(), companyPartList);
        }

    }

    @Override
    protected void initPropertyList() {
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EPropertyName ePropertyName : EPropertyName.values()) {
                if (!ePropertyName.equals(EPropertyName.CURRENT_PERIOD_INCOME)) {
                    companyTermContext.put(ePropertyName.name(), 0);
                    companyTermPropertyList.add(new CompanyTermProperty(ePropertyName.name(), ePropertyName.getDept(), 0, companyTerm));
                } else {
                    companyTermContext.put(EPropertyName.CURRENT_PERIOD_INCOME.name(), 100);
                    companyTermPropertyList.add(new CompanyTermProperty(EPropertyName.CURRENT_PERIOD_INCOME.name(), EPropertyName.CURRENT_PERIOD_INCOME.getDept(), 100, companyTerm));
                }
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }

    }

    @Override
    protected void initAccountList() {
        for (CompanyTermContext companyTermContext : campaignContext.getCompanyTermContextMap().values()) {
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<Account> accountList = new ArrayList<>();
            Account humanAccount = accountManager.packageAccount("0", EAccountEntityType.HR_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(humanAccount);
            Account adAccount = accountManager.packageAccount("0", EAccountEntityType.AD_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(adAccount);
            Account productFeeAccount = accountManager.packageAccount("0", EAccountEntityType.PRODUCT_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(productFeeAccount);
            Account marketFeeAccount = accountManager.packageAccount("0", EAccountEntityType.MARKET_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(marketFeeAccount);
            Account operationFeeAccount = accountManager.packageAccount("0", EAccountEntityType.OPERATION_FEE.name(), EAccountEntityType.COMPANY_CASH.name(), companyTerm);
            accountList.add(operationFeeAccount);
            Integer currentPeriodIncome = companyTermContext.get(EPropertyName.CURRENT_PERIOD_INCOME.name());
            Account incomeAccount = accountManager.packageAccount(String.valueOf(currentPeriodIncome), EAccountEntityType.COMPANY_CASH.name(), EAccountEntityType.OTHER.name(), companyTerm);
            accountList.add(incomeAccount);
            companyTermContext.setAccountList(accountList);
        }

    }


    protected void randomChoice() {

        Map<String, IndustryResource> currentTypeIndustryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();
        String industryId = campaignContext.getCampaign().getIndustry().getId();

        Integer currentCampaignDate = campaignContext.getCampaign().getCurrentCampaignDate();
        if (currentCampaignDate % 4 == 1) {
            //市场投放
            IndustryResource marketFeeResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET_FEE.name());
            marketFeeResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketFeeResource.getId())));
            currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.MARKET_FEE.name(), marketFeeResource);

            //订单
            IndustryResource marketOrderResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.MARKET_ORDER.name());
            marketOrderResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(marketOrderResource.getId())));
            currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.MARKET_ORDER.name(), marketOrderResource);
        }

        IndustryResource longTermLoanResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.LONG_TERM_LOAN.name());
        longTermLoanResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(longTermLoanResource.getId())));
        currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.LONG_TERM_LOAN.name(), longTermLoanResource);

        IndustryResource shortTermLoanResource = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.SHORT_TERM_LOAN.name());
        shortTermLoanResource.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(shortTermLoanResource.getId())));
        currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.SHORT_TERM_LOAN.name(), shortTermLoanResource);

        IndustryResource usuriousLoan = industryResourceManager.getUniqueIndustryResource(industryId, EManufacturingChoiceBaseType.USURIOUS_LOAN.name());
        usuriousLoan.setCurrentIndustryResourceChoiceSet(new LinkedHashSet<>(industryResourceChoiceManager.listIndustryResourceChoice(usuriousLoan.getId())));
        currentTypeIndustryResourceMap.put(EManufacturingChoiceBaseType.USURIOUS_LOAN.name(), usuriousLoan);

        campaignContext.setCurrentTypeIndustryResourceMap(currentTypeIndustryResourceMap);
    }

    @Override
    public void initNextObserable() {
        Campaign campaign = campaignContext.getCampaign();
        Integer campaignDate = campaign.getCurrentCampaignDate();
        Set<String> companyIdSet = campaignContext.getCompanyTermContextMap().keySet();
        Map<String, Observable> observableMap = campaignContext.getObservableMap();

        RoundEndObserable dateRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
        dateRoundObserable.addObserver((o, arg) -> next(arg.toString()));
        observableMap.put(campaignDate + ":" + "DATE_ROUND", dateRoundObserable);


        if (campaign.getCurrentCampaignDate() % 4 == 1) {
            RoundEndObserable marketPayRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
            marketPayRoundObserable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), arg.toString());
                    List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());

                    Map<String, List> choiceInstructionMap = new HashMap();
                    for (CompanyTerm companyTerm : companyTermList) {
                        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingInstructionBaseType.MARKET_ORDER.name());
                        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
                        String choiceId = companyTermInstruction.getIndustryResourceChoice().getId();
                        if(choiceInstructionMap.containsKey(choiceId)) {
                            choiceInstructionMap.get(choiceId).add(companyTermInstruction);
                        } else {
                            List<CompanyTermInstruction> companyTermInstructionList = new ArrayList<>();
                            companyTermInstructionList.add(companyTermInstruction);
                            choiceInstructionMap.put(choiceId, companyTermInstructionList);
                        }
                    }

                }
            });
            observableMap.put(campaignDate + ":" + "MARKET_PAY_ROUND", marketPayRoundObserable);
/*
            RoundEndObserable orderRoundObserable = new RoundEndObserable(campaign.getId(), companyIdSet);
            orderRoundObserable.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    Campaign campaign = (Campaign) baseManager.getObject(Campaign.class.getName(), arg.toString());
                    List<CompanyTerm> companyTermList = companyTermManager.listCompanyTerm(campaign.getId(), campaign.getCurrentCampaignDate());
                    for (CompanyTerm companyTerm : companyTermList) {
                        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTerm, EManufacturingChoiceBaseType.MARKET_ORDER.name());
                        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);
                    }
                }
            });
            observableMap.put(campaignDate + ":" + "ORDER_ROUND", orderRoundObserable);*/
        }

    }

    //原材料采购
    private void processMaterial(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> materialInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.MATERIAL_PURCHASE.name());
        if (materialInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction materialInstruction : materialInstructionList) {
                Material material = (Material) baseManager.getObject(Material.class.getName(), materialInstruction.getCompanyPart().getId());
                Integer amount = Integer.valueOf(materialInstruction.getValue());
                material.setAmount(material.getAmount() + amount);
                baseManager.saveOrUpdate(Material.class.getName(), material);

                materialInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), materialInstruction);

                fee += amount;
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.MATERIAL_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //产品研发投入
    private void processProductDevotion(CompanyTermContext companyTermContext){
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> productInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCT_DEVOTION.name());
        if (productInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction productInstruction : productInstructionList) {
                Product product = (Product) baseManager.getObject(Product.class.getName(), productInstruction.getCompanyPart().getId());
                Integer developNeedCycle = product.getDevelopNeedCycle();
                product.setDevelopNeedCycle(--developNeedCycle);
                baseManager.saveOrUpdate(Product.class.getName(), product);

                productInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), productInstruction);

                fee += Product.Type.valueOf(product.getType()).getPerDevotion();
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //市场区域开发费用
    private void processMarketAreaDevotion(CompanyTermContext companyTermContext){
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> marketInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.MARKET_DEVOTION.name());
        if (marketInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction marketInstruction : marketInstructionList) {
                Market market = (Market) baseManager.getObject(Market.class.getName(), marketInstruction.getCompanyPart().getId());
                Integer devotionNeedCycle = market.getDevotionNeedCycle();
                market.setDevotionNeedCycle(--devotionNeedCycle);
                baseManager.saveOrUpdate(Market.class.getName(), market);

                marketInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), marketInstruction);

                fee += Market.Type.valueOf(market.getType()).getPerDevotion();
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.MARKET_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //生产线建造
    private void processProductLineBuild(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

        List<CompanyTermInstruction> produceLineInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD.name());
        if (produceLineInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction produceLineInstruction : produceLineInstructionList) {
                ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineInstruction.getCompanyPart().getId());

                String[] valueArray = produceLineInstruction.getValue().split(";");
                String produceType = valueArray[0];
                String produceLineType = valueArray[1];
                produceLine.setProduceType(produceType);
                produceLine.setProduceLineType(produceLineType);

                Integer lineBuildNeedCycle = ProduceLine.Type.valueOf(produceLineType).getInstallCycle();
                if (lineBuildNeedCycle > 0) {
                    lineBuildNeedCycle--;
                }
                produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);

                if (lineBuildNeedCycle == 0) {//建造完成
                    produceLine.setStatus(ProduceLine.Status.FREE.name());
                } else {
                    produceLine.setStatus(ProduceLine.Status.BUILDING.name());
                }
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

                produceLineInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceLineInstruction);

                fee += ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //生产线建造
    private void processProductLineContinueBuild(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

        List<CompanyTermInstruction> produceLineInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD_CONTINUE.name());
        if (produceLineInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction produceLineInstruction : produceLineInstructionList) {
                ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineInstruction.getCompanyPart().getId());
                Integer lineBuildNeedCycle = Integer.valueOf(produceLine.getLineBuildNeedCycle());
                if (lineBuildNeedCycle > 0) {
                    lineBuildNeedCycle--;
                }
                if (lineBuildNeedCycle == 0) {//建造完成
                    produceLine.setStatus(ProduceLine.Status.FREE.name());
                }
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

                produceLineInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceLineInstruction);

                fee += ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //开始生产
    private void processProduce(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Company company = companyTerm.getCompany();
        Material R1 = materialManager.getMateral(company, Material.Type.R1.name());
        Material R2 = materialManager.getMateral(company, Material.Type.R2.name());
        Material R3 = materialManager.getMateral(company, Material.Type.R3.name());
        Material R4 = materialManager.getMateral(company, Material.Type.R4.name());
        Integer R1Amount = R1.getAmount();
        Integer R2Amount = R2.getAmount();
        Integer R3Amount = R3.getAmount();
        Integer R4Amount = R4.getAmount();

        List<CompanyTermInstruction> produceLineInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCE.name());
        if (produceLineInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction produceInstruction : produceLineInstructionList) {
                ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceInstruction.getCompanyPart().getId());
                boolean isMaterialAmountEnough = false;
                Product.Type productType = Product.Type.valueOf(produceLine.getProduceType());
                switch (productType) {
                    case P1:
                        if (R1Amount >= 1) {
                            isMaterialAmountEnough = true;
                            R1Amount = R1Amount - 1;
                            R1.setAmount(R1Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R1);
                        }
                        break;
                    case P2:
                        if (R1Amount >= 1 && R2Amount >= 1) {
                            isMaterialAmountEnough = true;
                            R1Amount = R1Amount - 1;
                            R1.setAmount(R1Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R1);
                            R2Amount = R2Amount - 1;
                            R2.setAmount(R2Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R2);
                        }
                        break;
                    case P3:
                        if (R2Amount >= 2 && R3Amount >= 1) {
                            isMaterialAmountEnough = true;
                            R2Amount = R2Amount - 2;
                            R2.setAmount(R2Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R2);
                            R3Amount = R3Amount - 1;
                            R3.setAmount(R3Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R3);
                        }
                        break;
                    case P4:
                        if (R2Amount >= 1 && R3Amount >= 1 && R4Amount >= 2) {
                            isMaterialAmountEnough = true;
                            R2Amount = R2Amount - 1;
                            R2.setAmount(R2Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R2);
                            R3Amount = R3Amount - 1;
                            R3.setAmount(R3Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R3);
                            R4Amount = R4Amount - 1;
                            R4.setAmount(R4Amount);
                            baseManager.saveOrUpdate(Material.class.getName(), R4);
                        }
                        break;
                    default:
                        throw new NoSuchElementException(productType.name());
                }
                if (isMaterialAmountEnough) {
                    fee += 1;
                }

                Integer produceCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getProduceCycle();
                produceLine.setProduceNeedCycle(produceCycle);
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

                produceInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceInstruction);
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //更新生产 完成入库
    protected void processUpdateProduce(CompanyTermContext companyTermContext) {
        Company company = companyTermContext.getCompanyTerm().getCompany();

        XQuery xQuery = new XQuery();
        xQuery.setHql("from ProduceLine where status=:status");
        xQuery.put("status", ProduceLine.Status.PRODUCING.name());
        List<ProduceLine> produceLineList = baseManager.listObject(xQuery);
        if (produceLineList != null) {
            for (ProduceLine produceLine : produceLineList) {
                Integer produceNeedCycle = produceLine.getProduceNeedCycle();
                --produceNeedCycle;
                produceLine.setProduceNeedCycle(produceNeedCycle);
                if (produceNeedCycle == 0) {
                    produceLine.setStatus(ProduceLine.Status.FREE.name());

                    String productType = Product.Type.valueOf(produceLine.getProduceType()).name();
                    Product product = productManager.getProduct(company, productType);
                    product.setAmount(product.getAmount() + 1);
                    baseManager.saveOrUpdate(Product.class.getName(), product);

                }
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
            }
        }
    }

    protected void processUsuriousLoan(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

        List<CompanyTermInstruction> usuriousLoanInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.USURIOUS_LOAN.name());
        if (usuriousLoanInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction usuriousLoanInstruction : usuriousLoanInstructionList) {
                usuriousLoanInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), usuriousLoanInstruction);

                fee+= Integer.valueOf(usuriousLoanInstruction.getValue());
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.USURIOUS_LOAN.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    protected void processShortTermLoan(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

        List<CompanyTermInstruction> shortTermLoanInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.LONG_TERM_LOAN.name());
        if (shortTermLoanInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction shortTermLoanInstruction : shortTermLoanInstructionList) {
                shortTermLoanInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), shortTermLoanInstruction);

                fee+= Integer.valueOf(shortTermLoanInstruction.getValue());
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.SHORT_TERM_LOAN.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    protected void processLongTermLoan(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

        List<CompanyTermInstruction> longTermLoanInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.LONG_TERM_LOAN.name());
        if (longTermLoanInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction longTermLoanInstruction : longTermLoanInstructionList) {
                longTermLoanInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), longTermLoanInstruction);

                fee+= Integer.valueOf(longTermLoanInstruction.getValue());
            }
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.LONG_TERM_LOAN.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //杂费
    protected void processOthers(CompanyTermContext companyTermContext){
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Integer fee = 5;
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.OTHER.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);
    }

    protected void process() {
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);

            //1.广告投入
            //2.
            processMaterial(companyTermContext);
            //3.
            processProductDevotion(companyTermContext);
            //4.
            processMarketAreaDevotion(companyTermContext);
            //5.建造生产线
            processProductLineBuild(companyTermContext);
            processProductLineContinueBuild(companyTermContext);
            //开始生产
            processProduce(companyTermContext);
            //更新生产完成入库
            processUpdateProduce(companyTermContext);
            //销售产品所得费用

            //贷款
            processUsuriousLoan(companyTermContext);
            processShortTermLoan(companyTermContext);
            processLongTermLoan(companyTermContext);
            //杂费
            processOthers(companyTermContext);
        }

    }

    protected void processInstruction() {
/*
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();

            //生产线建造
            List<CompanyTermInstruction> produceLineInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.PRODUCE_LINE.name());
            if (produceLineInstructionList != null) {
                for (CompanyTermInstruction produceLineInstruction : produceLineInstructionList) {
                    ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineInstruction.getCompanyPart().getId());
//                    Integer instructionDate = produceLineInstruction.getCompanyTerm().getCampaignDate();
//                    ProduceLine.Type eProduceLineType = ProduceLine.Type.valueOf(produceLineInstruction.getValue());
                    Integer lineBuildNeedCycle = Integer.valueOf(produceLine.getLineBuildNeedCycle());
                    if (lineBuildNeedCycle > 0) {
                        lineBuildNeedCycle--;
                    }
                    if (lineBuildNeedCycle == 0) {//建造完成
                        produceLine.setStatus(ProduceLine.Status.FREE.name());
                        baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
                    }

                    produceLineInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceLineInstruction);
                }
            }

            //原材料采购
            List<CompanyTermInstruction> materialInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.MATERIAL.name());
            if (materialInstructionList != null) {
                for (CompanyTermInstruction materialInstruction : materialInstructionList) {
                    Material material = (Material) baseManager.getObject(Material.class.getName(), materialInstruction.getCompanyPart().getId());
                    Integer amount = Integer.valueOf(materialInstruction.getValue());
                    material.setAmount(material.getAmount() + amount);
                    baseManager.saveOrUpdate(Material.class.getName(), material);

                    materialInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), materialInstruction);
                }
            }

            //产品研发
            List<CompanyTermInstruction> productInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.PRODUCT.name());
            if (productInstructionList != null) {
                for (CompanyTermInstruction productInstruction : productInstructionList) {
                    Product product = (Product) baseManager.getObject(Product.class.getName(), productInstruction.getCompanyPart().getId());
                    Integer developNeedCycle = product.getDevelopNeedCycle();
                    product.setDevelopNeedCycle(--developNeedCycle);
                    baseManager.saveOrUpdate(Product.class.getName(), product);

                    productInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                    baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), productInstruction);
                }
            }
        }
*/
    }

    protected void processPart() {
/*
        Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            Company company = companyTerm.getCompany();

            XQuery produceLineQuery = new XQuery();
            produceLineQuery.setHql("from ProduceLine where company.id = :companyId and status = :status");
            produceLineQuery.put("companyId", company.getId());
            produceLineQuery.put("status",ProduceLine.Status.PRODUCING.name());
            List<ProduceLine> produceLineList = baseManager.listObject(produceLineQuery);
            for (ProduceLine produceLine : produceLineList) {
                Integer produceNeedCycle = produceLine.getProduceNeedCycle();
                produceNeedCycle = produceNeedCycle - 1;
                produceLine.setProduceNeedCycle(produceNeedCycle);

                if (produceNeedCycle == 0) {//生产完成
                    Product product = productManager.getProduct(company, produceLine.getProduceType());
                    Integer productAmount = product.getAmount();
                    product.setAmount(++productAmount);
                    baseManager.saveOrUpdate(Product.class.getName(), product);

                    produceLine.setStatus(ProduceLine.Status.FREE.name());
                }

                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);
            }
        }
*/


    }


    protected void calculateProperty() {

        /*Map<String, CompanyTermContext> companyTermHandlerMap = campaignContext.getCompanyTermContextMap();
        for (String companyId : companyTermHandlerMap.keySet()) {
            CompanyTermContext companyTermContext = companyTermHandlerMap.get(companyId);
            CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
            List<CompanyTermProperty> companyTermPropertyList = new ArrayList<>();
            for (EManufacturingPropertyName ePropertyName : EManufacturingPropertyName.values()) {
                String key = ePropertyName.name();
                Integer value = companyTermContext.get(key);
                companyTermPropertyList.add(new CompanyTermProperty(ePropertyName.name(), ePropertyName.getDept(), value, companyTerm));
            }
            companyTermContext.setCompanyTermPropertyList(companyTermPropertyList);
        }*/
    }

    protected void calculateAccount() {

    }



}
