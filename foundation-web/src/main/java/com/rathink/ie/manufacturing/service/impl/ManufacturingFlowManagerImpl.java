package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.ming800.core.taglib.PageEntity;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.base.component.CyclePublisher;
import com.rathink.ie.base.component.DevoteCycle;
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
import com.rathink.ie.manufacturing.model.*;
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

            Integer companyNum = campaignContext.getCompanyTermContextMap().size();
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
            DevoteCycle devoteCycle = new DevoteCycle(campaignContext, marketList, marketOrderChoiceMap);
            campaignContext.setDevoteCycle(devoteCycle);
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


    //处理订单账款
    private void processOrderAccount(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        XQuery xQuery = new XQuery();
        String hql = "from MarketOrder where status=:status";
        xQuery.setHql(hql);
        xQuery.put("status", MarketOrder.Status.DELIVERED.name());
        List<MarketOrder> marketOrderList = baseManager.listObject(xQuery);
        Integer fee = 0;
        if (marketOrderList != null) {
            for (MarketOrder marketOrder : marketOrderList) {
                Integer needAccountCycle = marketOrder.getNeedAccountCycle();
                needAccountCycle--;
                marketOrder.setNeedAccountCycle(needAccountCycle);
                if (needAccountCycle == 0) {
                    marketOrder.setStatus(MarketOrder.Status.FINISH.name());
                    fee += marketOrder.getTotalPrice();
                }
                baseManager.saveOrUpdate(MarketOrder.class.getName(),marketOrder);
            }
        }
        if (fee != 0) {
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(),
                    EManufacturingAccountEntityType.ORDER_FEE.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }


    //处理未交付的订单 扣费等
    private void processUnDeliveredOrder(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        XQuery xQuery = new XQuery();
        String hql = "from MarketOrder where status=:status";
        xQuery.setHql(hql);
        xQuery.put("status", MarketOrder.Status.NORMAL.name());
        List<MarketOrder> marketOrderList = baseManager.listObject(xQuery);
        Integer fee = 0;
        if (marketOrderList != null) {
            for (MarketOrder marketOrder : marketOrderList) {
                fee += Integer.valueOf(marketOrder.getTotalPrice());
            }
            fee = fee * 10 / 100 + 1;
        }
        if (fee != 0) {
            Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.ORDER_DELAY_FEE.name()
                    , EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }
    }

    //原材料采购
    private void processMaterial(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> materialInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.MATERIAL_PURCHASE.name());
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
    }

    //产品研发投入
    private void processProductDevotion(CompanyTermContext companyTermContext){
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> productInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCT_DEVOTION.name());
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

    //市场区域开发费用
    private void processMarketAreaDevotion(CompanyTermContext companyTermContext){
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> marketInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.MARKET_DEVOTION.name());
        if (marketInstructionList != null) {
            for (CompanyTermInstruction marketInstruction : marketInstructionList) {
                Market market = (Market) baseManager.getObject(Market.class.getName(), marketInstruction.getCompanyPart().getId());
                Integer devotionNeedCycle = market.getDevotionNeedCycle();
                market.setDevotionNeedCycle(--devotionNeedCycle);
                baseManager.saveOrUpdate(Market.class.getName(), market);

                marketInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), marketInstruction);
            }
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

    protected void processRebuild(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        List<CompanyTermInstruction> marketInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.PRODUCE_LINE_REBUILD.name());
        if (marketInstructionList != null) {
            for (CompanyTermInstruction marketInstruction : marketInstructionList) {
                ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), marketInstruction.getCompanyPart().getId());

                Integer lineBuildNeedCycle = produceLine.getLineBuildNeedCycle();
                --lineBuildNeedCycle;
                produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);
                if (lineBuildNeedCycle == 0) {
                    produceLine.setStatus(ProduceLine.Status.FREE.name());
                }
                baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

                marketInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), marketInstruction);
            }
        }
    }

    protected void processLoan(CompanyTermContext companyTermContext) {
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Integer campaignDate = companyTerm.getCampaignDate();

        XQuery xQuery = new XQuery();
        String hql = "from Loan where status=:status and company.id = :companyId";
        xQuery.setHql(hql);
        xQuery.put("status", Loan.Status.NORMAL.name());
        xQuery.put("companyId", companyTerm.getCompany().getId());
        List<Loan> loanList = baseManager.listObject(xQuery);
        if (loanList != null) {
            for (Loan loan : loanList) {
                Integer needRepayCycle = loan.getNeedRepayCycle();
                needRepayCycle--;
                loan.setNeedRepayCycle(needRepayCycle);

                Loan.Type loanType = Loan.Type.valueOf(loan.getType());
                Integer fee = 0;
                Integer rateFee = 0;
                if (campaignDate % 4 == 0) {//年底付息
                    rateFee += loan.getMoney() * loanType.getYearRate() / 100;
                }
                if (needRepayCycle == 0) {//到期还款
                    loan.setStatus(Loan.Status.FINISH.name());
                    fee += loan.getMoney();
                }
                if (fee != 0) {
                    //需要保证Loan.Type.name跟EManufacturingAccountEntityType中的贷款的名字一直
                    Account account = accountManager.packageAccount(String.valueOf(fee), loanType.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
                    baseManager.saveOrUpdate(Account.class.getName(), account);
                }
                if (rateFee != 0) {
                    Account account = accountManager.packageAccount(String.valueOf(rateFee), EManufacturingAccountEntityType.LOAN_INTEREST.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
                    baseManager.saveOrUpdate(Account.class.getName(), account);
                }
                baseManager.saveOrUpdate(Loan.class.getName(), loan);
            }
        }


        List<CompanyTermInstruction> usuriousLoanInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingInstructionBaseType.USURIOUS_LOAN.name());
        if (usuriousLoanInstructionList != null) {
            Integer fee = 0;
            for (CompanyTermInstruction usuriousLoanInstruction : usuriousLoanInstructionList) {
                usuriousLoanInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), usuriousLoanInstruction);

                fee+= Integer.valueOf(usuriousLoanInstruction.getValue());
            }
            if (fee != 0) {
                Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.USURIOUS_LOAN.name(), companyTerm);
                baseManager.saveOrUpdate(Account.class.getName(), account);
            }
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
            if (fee != 0) {
                Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.SHORT_TERM_LOAN.name(), companyTerm);
                baseManager.saveOrUpdate(Account.class.getName(), account);
            }
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
            if (fee != 0) {
                Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.COMPANY_CASH.name(), EManufacturingAccountEntityType.LONG_TERM_LOAN.name(), companyTerm);
                baseManager.saveOrUpdate(Account.class.getName(), account);
            }
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

            //原材料入库
            processMaterial(companyTermContext);
            //产品投入周期
            processProductDevotion(companyTermContext);
            //转产
            processRebuild(companyTermContext);
            //市场投入周期
            processMarketAreaDevotion(companyTermContext);
            //更新生产完成入库
            processUpdateProduce(companyTermContext);
            //销售产品所得费用
            processOrderAccount(companyTermContext);
            if (companyTermContext.getCompanyTerm().getCampaignDate() % 4 == 0) {
                //延迟交付
                processUnDeliveredOrder(companyTermContext);
            }
            //杂费
            processOthers(companyTermContext);
            //贷款
            processLoan(companyTermContext);
        }

    }


}
