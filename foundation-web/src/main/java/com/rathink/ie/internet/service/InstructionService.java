package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;
import com.rathink.ie.internet.choice.model.OperationChoice;
import com.rathink.ie.internet.choice.model.ProductStudy;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.internet.instruction.model.MarketInstruction;
import com.rathink.ie.internet.instruction.model.OperationInstruction;
import com.rathink.ie.internet.instruction.model.ProductStudyInstruction;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/8/26.
 */
@Service
public class InstructionService {
    private static Logger logger = Logger.getLogger(InstructionService.class.getName());

    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceService choiceService;
    @Autowired
    private CompanyStatusService companyStatusService;

    /**
     * 产生人才竞标结果
     * 出价最高的一家中标
     * 如果多家出价一样  最先出最高价的中标
     * @param campaign
     */
    public void produceHumanDiddingResult(Campaign campaign) {
        List<Human> humanList = choiceService.listHuman(campaign);
        if (humanList != null) {
            for (Human human : humanList) {
                XQuery xQuery = new XQuery();
                xQuery.setHql("from HrInstruction where human.id = :humanId order by salary desc, id asc");
                LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
                queryParamMap.put("humanId", human.getId());
                xQuery.setQueryParamMap(queryParamMap);
                List<HrInstruction> hrInstructionList = baseManager.listObject(xQuery);
                if (hrInstructionList != null) {
                    for (int i = 0; i < hrInstructionList.size(); i++) {
                        HrInstruction hrInstruction = hrInstructionList.get(i);
                        hrInstruction.setStatus(i == 0 ? HrInstruction.Status.YXZ.getValue() : HrInstruction.Status.WXZ.getValue());
                        baseManager.saveOrUpdate(HrInstruction.class.getName(), hrInstruction);
                    }
                }
            }
        }
    }

    public Integer getDeptAbilityValue(Company company, String type) {
        Integer deptAbility = 60;
        String hql = "from HrInstruction where status=:status and type = :type and company.id = :companyId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("status", HrInstruction.Status.YXZ.getValue());
        queryParamMap.put("type", type);
        queryParamMap.put("companyId", company.getId());
        XQuery xQuery = new XQuery();
        xQuery.setHql(hql);
        xQuery.setQueryParamMap(queryParamMap);
        List<HrInstruction> hrInstructionList = baseManager.listObject(xQuery);
        if (hrInstructionList != null) {
            for (HrInstruction hrInstruction : hrInstructionList) {
                String level = hrInstruction.getHuman().getAbility();
                deptAbility += Integer.valueOf(level);
            }
        }
        return deptAbility;
    }

    public Integer getNewUserAmount(Company company) {
        Integer newUserAmount = 0;
        XQuery xQuery = new XQuery();
        xQuery.setHql("from MarketInstruction where company.id = :companyId and campaignDate = :campaignDate");
        xQuery.put("companyId", company.getId());
        xQuery.put("campaignDate", company.getCampaign().getCurrentCampaignDate());
        List<MarketInstruction> marketInstructionList = baseManager.listObject(xQuery);
        if (marketInstructionList != null) {
            for (MarketInstruction marketInstruction : marketInstructionList) {
                Integer fee = Integer.valueOf(marketInstruction.getFee());
                Integer cost = Integer.valueOf(marketInstruction.getMarketActivityChoice().getCost());
                newUserAmount += fee / cost;
            }
        }
        return newUserAmount;
    }

    public Integer getSatisfaction(Company company, Integer operationAbility){
        Integer satisfaction = 50;

        Integer operationFee = 0;//资金投入
        XQuery xQuery = new XQuery();
        xQuery.setHql("from OperationInstruction where company.id = :companyId and campaignDate = :campaignDate");
        xQuery.put("companyId", company.getId());
        xQuery.put("campaignDate", company.getCampaign().getCurrentCampaignDate());
        List<OperationInstruction> operationInstructionList = baseManager.listObject(xQuery);
        if (operationInstructionList != null) {
            for (OperationInstruction operationInstruction : operationInstructionList) {
                operationFee += Integer.valueOf(operationInstruction.getFee());
            }
        }

        Integer operationFeeRatio = operationFee / 10000 + 50;
        satisfaction = operationAbility * operationFeeRatio / 100;

        return satisfaction;
    }

    public Integer getOldUserAmount(Company company, Integer satisfaction){
        Integer oldUserAmount = 0;
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        //上一期用户数
        CompanyStatusPropertyValue preUserAmount = companyStatusService
                .getCompanyStatusProperty(EPropertyName.USER_AMOUNT.name(), companyStatus);
        oldUserAmount = Integer.valueOf(preUserAmount.getValue()) * satisfaction / 100;
        return oldUserAmount;
    }

    public ProductStudyInstruction getProductStudyInstruction(Company company, String campaignDate) {
        String hql = "from ProductStudyInstruction where company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaignDate);
        ProductStudyInstruction productStudyInstruction = (ProductStudyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return productStudyInstruction;
    }

    public Integer getProductRadio(Company company,Integer productAbility) {
        Integer productRadio = 10;
        //产品定位
        ProductStudyInstruction productStudyInstruction = getProductStudyInstruction(company, company.getCampaign().getCurrentCampaignDate());
        String fee = productStudyInstruction == null ? "0" : productStudyInstruction.getFee();
        //资金投入系数
        Integer feeRatio = Integer.valueOf(fee) / 1000 + 100;
        //上一期的产品系数
        CompanyStatus companyStatus = companyStatusService.getCompanyStatus(company, company.getCampaign().getCurrentCampaignDate());
        CompanyStatusPropertyValue preProductRatio = companyStatusService
                .getCompanyStatusProperty(EPropertyName.PRODUCT_RATIO.name(), companyStatus);

        productRadio = (productAbility * feeRatio)/100 + Integer.valueOf(preProductRatio.getValue());
        return productRadio;
    }

    public Integer getPerOrderCost(Company company) {
        final String DEFAULT_GRADE = "3";
        Integer perOrderCost = 0;
        //产品定位
        ProductStudyInstruction productStudyInstruction = getProductStudyInstruction(company, company.getCampaign().getCurrentCampaignDate());
        String grade = productStudyInstruction == null ? DEFAULT_GRADE : productStudyInstruction.getProductStudy().getGrade();
        perOrderCost = Integer.valueOf(grade) * 10 + 60;
        return perOrderCost;
    }

    public Integer getCurrentPeriodIncome(Integer userAmount, Integer perOrdreCost) {
        return userAmount * perOrdreCost;
    }
    /**
     * 保存人才选择结果
     * @param company
     * @param human
     * @param map
     */
    public void saveOrUpdateHrInstruction(Company company, Human human, Map<String, String> map) {
        Campaign campaign =  company.getCampaign();
        String hql = "from HrInstruction where human.id = :humanId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("humanId", human.getId());
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        HrInstruction hrInstruction = (HrInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (hrInstruction == null) {
            logger.info("save HrInstruction, choiceId:" + human.getId());
            hrInstruction = new HrInstruction();
            hrInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            hrInstruction.setCampaign(campaign);
            hrInstruction.setCompany(company);
            hrInstruction.setDept(human.getDept());
            hrInstruction.setStatus(HrInstruction.Status.DQD.getValue());
        } else {
            logger.info("update HrInstruction, choiceId:" + human.getId());
        }
        hrInstruction.setHuman(human);
        hrInstruction.setFee(map.get("fee"));
        baseManager.saveOrUpdate(HrInstruction.class.getName(), hrInstruction);
    }

    public void saveOrUpdateMarketInstruction(Company company, MarketActivityChoice marketActivityChoice, Map<String, String> map) {
        Campaign campaign =  company.getCampaign();
        String hql = "from MarketInstruction where marketActivityChoice.id = :marketActivityChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("marketActivityChoiceId", marketActivityChoice.getId());
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        MarketInstruction marketInstruction = (MarketInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (marketInstruction == null) {
            logger.info("save MarketInstruction, choiceId:" + marketActivityChoice.getId());
            marketInstruction = new MarketInstruction();
            marketInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            marketInstruction.setCampaign(campaign);
            marketInstruction.setCompany(company);
            marketInstruction.setDept(marketActivityChoice.getDept());
        } else {
            logger.info("update MarketInstruction, choiceId:" + marketActivityChoice.getId());
        }
        marketInstruction.setMarketActivityChoice(marketActivityChoice);
        marketInstruction.setFee(map.get("fee"));
        baseManager.saveOrUpdate(MarketInstruction.class.getName(), marketInstruction);
    }

    public void saveOrUpdateProductStudyInstruction(Company company, ProductStudy productStudy, Map<String, String> map) {
        Campaign campaign =  company.getCampaign();
        String hql = "from ProductStudyInstruction where productStudy.id = :productStudyId " +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("productStudyId", productStudy.getId());
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        ProductStudyInstruction productStudyInstruction = (ProductStudyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (productStudyInstruction == null) {
            logger.info("save ProductStudyInstruction, choiceId:" + productStudy.getId());
            productStudyInstruction = new ProductStudyInstruction();
            productStudyInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            productStudyInstruction.setCampaign(campaign);
            productStudyInstruction.setCompany(company);
            productStudyInstruction.setDept(productStudy.getDept());
        } else {
            logger.info("update ProductStudyInstruction, choiceId:" + productStudy.getId());
        }
        productStudyInstruction.setProductStudy(productStudy);
        productStudyInstruction.setFee(map.get("fee"));
        baseManager.saveOrUpdate(ProductStudyInstruction.class.getName(), productStudyInstruction);

    }

    public void saveOrUpdateOperationInstruction(Company company, OperationChoice operationChoice, Map<String, String> map) {
        Campaign campaign =  company.getCampaign();
        String hql = "from OperationInstruction where operationChoice.id = :operationChoiceId " +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("operationChoiceId", operationChoice.getId());
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        OperationInstruction operationInstruction = (OperationInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (operationInstruction == null) {
            logger.info("save OperationInstruction, choiceId:" + operationChoice.getId());
            operationInstruction = new OperationInstruction();
            operationInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
            operationInstruction.setCampaign(campaign);
            operationInstruction.setCompany(company);
            operationInstruction.setDept(operationChoice.getDept());
        } else{
            logger.info("save OperationInstruction, choiceId:" + operationChoice.getId());
        }
        operationInstruction.setOperationChoice(operationChoice);
        operationInstruction.setFee(map.get("fee"));
        baseManager.saveOrUpdate(OperationInstruction.class.getName(), operationInstruction);
    }
}
