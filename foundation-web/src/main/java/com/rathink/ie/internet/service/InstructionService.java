package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.util.CampaignUtil;
import com.rathink.ie.ibase.property.model.CompanyStatus;
import com.rathink.ie.ibase.property.model.CompanyStatusPropertyValue;
import com.rathink.ie.ibase.service.CompanyStatusService;
import com.rathink.ie.internet.EPropertyName;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.choice.model.*;
import com.rathink.ie.internet.instruction.model.*;
import com.rathink.ie.foundation.team.model.Company;
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
                xQuery.setHql("from HrInstruction where human.id = :humanId order by fee desc, id asc");
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

    public List<OfficeInstruction> listOfficeInstruction(Company company) {
        String hql = "from OfficeInstruction where company.id = :companyId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        XQuery xQuery = new XQuery();
        xQuery.setHql(hql);
        xQuery.setQueryParamMap(queryParamMap);
        List<OfficeInstruction> officeInstructionList = baseManager.listObject(xQuery);
        return officeInstructionList;
    }

    public List<HrInstruction> listHrInstruction(Company company) {
        String hql = "from HrInstruction where status=:status and company.id = :companyId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("status", HrInstruction.Status.YXZ.getValue());
        queryParamMap.put("companyId", company.getId());
        XQuery xQuery = new XQuery();
        xQuery.setHql(hql);
        xQuery.setQueryParamMap(queryParamMap);
        List<HrInstruction> hrInstructionList = baseManager.listObject(xQuery);
        return hrInstructionList;
    }

    public List<MarketInstruction> listMarketInstruction(Company company, String campaignDate) {
        XQuery xQuery = new XQuery();
        xQuery.setHql("from MarketInstruction where company.id = :companyId and campaignDate = :campaignDate");
        xQuery.put("companyId", company.getId());
        xQuery.put("campaignDate", campaignDate);
        List<MarketInstruction> marketInstructionList = baseManager.listObject(xQuery);
        return marketInstructionList;
    }

    public List<OperationInstruction> listOperationInstruction(Company company, String campaignDate){
        XQuery xQuery = new XQuery();
        xQuery.setHql("from OperationInstruction where company.id = :companyId and campaignDate = :campaignDate");
        xQuery.put("companyId", company.getId());
        xQuery.put("campaignDate", campaignDate);
        List<OperationInstruction> operationInstructionList = baseManager.listObject(xQuery);
        return operationInstructionList;
    }

    public ProductStudyInstruction getProductStudyInstruction(Company company, String campaignDate) {
        String hql = "from ProductStudyInstruction where company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaignDate);
        ProductStudyInstruction productStudyInstruction = (ProductStudyInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return productStudyInstruction;
    }

    public void saveOrUpdateOfficeInstruction(Company company, OfficeChoice officeChoice, Map<String, String> map) {
        Campaign campaign =  company.getCampaign();
        String hql = "from OfficeInstruction where officeChoice.id = :officeChoiceId" +
                " and company.id = :companyId and campaignDate = :campaignDate";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("officeChoiceId", officeChoice.getId());
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("campaignDate", campaign.getCurrentCampaignDate());
        OfficeInstruction officeInstruction = (OfficeInstruction) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if ("-1".equals(map.get("fee"))) {
            if (officeInstruction != null) {
                baseManager.delete(OfficeInstruction.class.getName(), officeInstruction.getId());
            }
        } else {
            if (officeInstruction == null) {
                officeInstruction = new OfficeInstruction();
                officeInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
                officeInstruction.setCampaign(campaign);
                officeInstruction.setCompany(company);
            } else {
            }
            officeInstruction.setOfficeChoice(officeChoice);
            officeInstruction.setFee(map.get("fee"));
            baseManager.saveOrUpdate(OfficeInstruction.class.getName(), officeInstruction);
        }
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
        if ("-1".equals(map.get("fee"))) {
            if (hrInstruction != null) {
                baseManager.delete(HrInstruction.class.getName(), hrInstruction.getId());
            }
        } else {
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
        if ("-1".equals(map.get("fee"))) {
            if (marketInstruction != null) {
                baseManager.delete(MarketInstruction.class.getName(), marketInstruction.getId());
            }
        } else {
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
        if ("-1".equals(map.get("fee"))) {
            if (productStudyInstruction != null) {
                baseManager.delete(ProductStudyInstruction.class.getName(), productStudyInstruction.getId());
            }
        } else {
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
        if ("-1".equals(map.get("fee"))) {
            if (operationInstruction != null) {
                baseManager.delete(OperationInstruction.class.getName(), operationInstruction.getId());
            }
        } else {
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

    public Integer productGradeConflict(ProductStudyInstruction productStudyInstruction) {
        Integer productGradeConflictRatio = 100;
        if (productStudyInstruction == null) return productGradeConflictRatio;

        XQuery xQuery = new XQuery();
        String hql = "from ProductStudyInstruction where campaignDate = :campaignDate and productStudy.grade = :grade";
        xQuery.setHql(hql);
        xQuery.put("campaignDate", productStudyInstruction.getCampaignDate());
        xQuery.put("grade", productStudyInstruction.getProductStudy().getGrade());
        List<ProductStudyInstruction> productStudyInstructionList = baseManager.listObject(xQuery);
        if (productStudyInstructionList != null) {
            int size = productStudyInstructionList.size();
            if (size > 1) {
                productGradeConflictRatio = 80;
            }
        }
        return productGradeConflictRatio;
    }

    public Integer getProductGradeChangeRatio(ProductStudyInstruction productStudyInstruction) {
        Integer ratio = 100;
        if(productStudyInstruction==null) return ratio;

        String currentGrade = productStudyInstruction.getProductStudy().getGrade();
        String campaignDate = productStudyInstruction.getCampaignDate();
        String preCampaignDate = CampaignUtil.getPreCampaignDate(campaignDate);
        ProductStudyInstruction preProductStudyInstruction = getProductStudyInstruction(productStudyInstruction.getCompany(), preCampaignDate);
        if (preProductStudyInstruction != null) {
            String preGrade = preProductStudyInstruction.getProductStudy().getGrade();
            if (!preGrade.equals(currentGrade)) {
                ratio = 80;
            }
        }
        return ratio;
    }
}
