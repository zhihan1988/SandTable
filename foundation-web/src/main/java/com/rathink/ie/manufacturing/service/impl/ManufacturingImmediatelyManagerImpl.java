package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.p.service.AutoSerialManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.AccountManager;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.manufacturing.*;
import com.rathink.ie.manufacturing.model.*;
import com.rathink.ie.manufacturing.service.ManufacturingImmediatelyManager;
import com.rathink.ie.manufacturing.service.MaterialManager;
import com.rathink.ie.manufacturing.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Hean on 2015/10/25.
 */
@Service
public class ManufacturingImmediatelyManagerImpl implements ManufacturingImmediatelyManager {

    private static Logger logger = LoggerFactory.getLogger(ManufacturingImmediatelyManagerImpl.class);
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private AutoSerialManager autoSerialManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private MaterialManager materialManager;

    /**
     * 生产线建造
     *
     */
    @Deprecated
    public Map processProductLineBuild(String companyTermId, String partId, String produceType, String lineType) {
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), partId);

        CompanyTermInstruction produceLineInstruction = new CompanyTermInstruction();
        produceLineInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        produceLineInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD.name());
        produceLineInstruction.setDept(EManufacturingDept.PRODUCT.name());
        produceLineInstruction.setCompanyPart(produceLine);
        produceLineInstruction.setValue(produceType + ";" + lineType);
        produceLineInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceLineInstruction);

        produceLine.setProduceType(produceType);
        produceLine.setProduceLineType(lineType);

        Integer lineBuildNeedCycle = ProduceLine.Type.valueOf(lineType).getInstallCycle();
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

        Integer fee = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        Integer installCycle = ProduceLine.Type.valueOf(lineType).getInstallCycle();

        Map result = new HashMap<>();
        result.put("status", 1);
        result.put("installCycle", installCycle);
        result.put("line",produceLine);
        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);
        return result;
    }

    /**
     * 继续修建生产线
     */
    @Deprecated
    public Map processProductLineContinueBuild(String companyTermId, String partId) {
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD_CONTINUE.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Integer lineBuildNeedCycle = Integer.valueOf(produceLine.getLineBuildNeedCycle());
        if (lineBuildNeedCycle > 0) {
            lineBuildNeedCycle--;
            produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);
        }
        if (lineBuildNeedCycle == 0) {//建造完成
            produceLine.setStatus(ProduceLine.Status.FREE.name());
        }
        baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);


        Integer fee = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();

        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        Map result = new HashMap<>();
        result.put("status", 1);
        result.put("line", produceLine);
        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);
        return result;
    }

    //开始生产
    @Override
    public Map processProduce(CompanyTerm companyTerm, ProduceLine produceLine) {
        Map result = new HashMap<>();
        Company company = companyTerm.getCompany();
        Material R1 = materialManager.getMateral(company, Material.Type.R1.name());
        Material R2 = materialManager.getMateral(company, Material.Type.R2.name());
        Material R3 = materialManager.getMateral(company, Material.Type.R3.name());
        Material R4 = materialManager.getMateral(company, Material.Type.R4.name());
        Integer R1Amount = R1.getAmount();
        Integer R2Amount = R2.getAmount();
        Integer R3Amount = R3.getAmount();
        Integer R4Amount = R4.getAmount();

        boolean isMaterialAmountEnough = false;
        Product.Type productType = Product.Type.valueOf(produceLine.getProduceType());
        switch (productType){
            case P1:
                if (R1Amount >=1) {
                    isMaterialAmountEnough = true;
                }
                break;
            case P2:
                if (R1Amount >= 1 && R2Amount >= 1) {
                    isMaterialAmountEnough = true;
                }
                break;
            case P3:
                if (R2Amount >= 2 && R3Amount >= 1) {
                    isMaterialAmountEnough = true;
                }
                break;
            case P4:
                if (R2Amount >= 1 && R3Amount >= 1 && R4Amount >= 2) {
                    isMaterialAmountEnough = true;
                }
                break;
            default:
                throw new NoSuchElementException(productType.name());
        }
        if (!isMaterialAmountEnough) {
            result.put("status", 0);
            result.put("message", "原料不足");
            return result;
        }

        CompanyTermInstruction produceInstruction = new CompanyTermInstruction();
        produceInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        produceInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE.name());
        produceInstruction.setDept(EManufacturingDept.PRODUCT.name());
        produceInstruction.setCompanyPart(produceLine);
        produceInstruction.setValue(produceLine.getProduceType());
        produceInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceInstruction);


        switch (productType) {
            case P1:
                if (R1Amount >= 1) {
                    R1Amount = R1Amount - 1;
                    R1.setAmount(R1Amount);
                    baseManager.saveOrUpdate(Material.class.getName(), R1);
                }
                break;
            case P2:
                if (R1Amount >= 1 && R2Amount >= 1) {
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


        produceLine.setStatus(ProduceLine.Status.PRODUCING.name());
        Integer produceCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getProduceCycle();
        produceLine.setProduceNeedCycle(produceCycle);
        baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

        produceInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), produceInstruction);

        String fee = "1";
        Account account = accountManager.packageAccount(fee, EManufacturingAccountEntityType.PRODUCE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        result.put("status", 1);
        result.put("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setR1Amount(R1Amount);
        newReport.setR2Amount(R2Amount);
        newReport.setR3Amount(R3Amount);
        newReport.setR4Amount(R4Amount);
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);
        result.put("newReport", newReport);

        return result;
    }

    //交付订单
    public Map processDeliveredOrder(String companyTermId, String orderId) {
        Map result = new HashMap<>();

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        MarketOrder marketOrder = (MarketOrder) baseManager.getObject(MarketOrder.class.getName(), orderId);
        Product product = productManager.getProduct(companyTerm.getCompany(), marketOrder.getProductType());

        if (marketOrder.getAmount() > product.getAmount()) {
            result.put("status", 0);
            result.put("message", "产品库存不足");
            return result;
        }

        CompanyTermInstruction orderDeliverInstruction = new CompanyTermInstruction();
        orderDeliverInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        orderDeliverInstruction.setBaseType(EManufacturingInstructionBaseType.ORDER_DELIVER.name());
        orderDeliverInstruction.setDept(EManufacturingDept.PRODUCT.name());
        orderDeliverInstruction.setCompanyPart(marketOrder);
        orderDeliverInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), orderDeliverInstruction);

        marketOrder.setStatus(MarketOrder.Status.DELIVERED.name());
        baseManager.saveOrUpdate(MarketOrder.class.getName(), marketOrder);

        product.setAmount(product.getAmount() - 1);
        baseManager.saveOrUpdate(Product.class.getName(), product);

        NewReport newReport = new NewReport();
        Product.Type productType = Product.Type.valueOf(product.getType());
        switch (productType) {
            case P1:
                newReport.setP1Amount(product.getAmount());
                break;
            case P2:
                newReport.setP2Amount(product.getAmount());
                break;
            case P3:
                newReport.setP3Amount(product.getAmount());
                break;
            case P4:
                newReport.setP4Amount(product.getAmount());
                break;
            default:
                throw new NoSuchElementException(productType+"");
        }
        result.put("status", 1);
        result.put("newReport", newReport);
        return result;
    }


    public Map loan(String companyTermId, String choiceId, String fee, String type) {

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        IndustryResourceChoice industryResourceChoice = (IndustryResourceChoice) baseManager.getObject(IndustryResourceChoice.class.getName(), choiceId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.MATERIAL_PURCHASE.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setIndustryResourceChoice(industryResourceChoice);
        companyTermInstruction.setCompanyTerm(companyTerm);
        companyTermInstruction.setValue(fee);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Loan loan = new Loan();
        loan.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
        loan.setDept(EManufacturingDept.FINANCE.name());
        loan.setStatus(Loan.Status.NORMAL.name());
        loan.setCampaign(companyTerm.getCampaign());
        loan.setCompany(companyTerm.getCompany());
//                usuriousLoan.setName();
        loan.setMoney(Integer.valueOf(fee));
        Loan.Type loanType = Loan.Type.valueOf(type);
        loan.setNeedRepayCycle(loanType.getCycle());
        loan.setType(loanType.name());
        baseManager.saveOrUpdate(Loan.class.getName(), loan);

        String loanAccountName = EManufacturingAccountEntityType.valueOf(loanType.name()).name();
        Account account = accountManager.packageAccount(fee, EManufacturingAccountEntityType.COMPANY_CASH.name(), loanAccountName, companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        Map result = new HashMap<>();
        result.put("status", 1);
        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        Integer totalLoan = accountManager.sumLoan(companyTerm.getCompany(), loanType.name());
        switch (loanType) {
            case LONG_TERM_LOAN:
                newReport.setLongTermLoan(totalLoan);
                break;
            case SHORT_TERM_LOAN:
                newReport.setShortTermLoan(totalLoan);
                break;
            case USURIOUS_LOAN:
                newReport.setUsuriousLoan(totalLoan);
                break;
            default:
                throw new NoSuchElementException(loanType.name());
        }
        result.put("newReport", newReport);

        return result;
    }
}
