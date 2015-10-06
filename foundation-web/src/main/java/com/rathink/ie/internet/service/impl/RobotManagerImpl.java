package com.rathink.ie.internet.service.impl;

import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.InstructionManager;
import com.rathink.ie.internet.service.RobotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Hean on 2015/9/21.
 */
@Service
public class RobotManagerImpl implements RobotManager {
    @Autowired
    private InstructionManager instructionManager;

    @Override
    public void randomInstruction(CompanyTermContext companyTermContext) {
       /* keepPre(companyTermContext, EChoiceBaseType.PRODUCT_STUDY.name());
        randomFee(companyTermContext, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        randomFee(companyTermContext, EChoiceBaseType.MARKET_ACTIVITY.name());
        randomFee(companyTermContext, EChoiceBaseType.OPERATION.name());
        randomHuman(companyTermContext);*/
    }

    /*public void randomFee(CompanyTermContext companyTermContext, String baseType) {
        CampaignContext campaignContext = companyTermContext.getCampaignContext();
        List<CampaignTermChoice> campaignTermChoiceList = campaignContext.listCurrentCompanyChoiceByType(baseType);
        CampaignTermChoice campaignTermChoice = campaignTermChoiceList.get(RandomUtil.random(0, campaignTermChoiceList.size()));
        String[] feeArray = campaignTermChoice.getFees().split(",");
        String fee = feeArray[RandomUtil.random(0, feeArray.length - 1)];
        instructionManager.saveOrUpdateInstructionByChoice(companyTermContext.getCompanyTerm().getCompany(), campaignTermChoice.getId(), fee);
    }

    public void keepPre(CompanyTermContext companyTermContext, String baseType) {
        CompanyTermInstruction companyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTermContext.getCompanyTerm(), baseType);
        if (companyTermInstruction != null) return;

        CompanyTermInstruction preCompanyTermInstruction = instructionManager.getUniqueInstructionByBaseType(companyTermContext.getPreCompanyTermContext().getCompanyTerm(), baseType);
        CampaignTermChoice campaignTermChoice = null;
        List<CampaignTermChoice> officeList = companyTermContext.getCampaignContext().listCurrentCompanyChoiceByType(baseType);
        if (preCompanyTermInstruction == null) {
            campaignTermChoice = officeList.get(RandomUtil.random(0, officeList.size()));
        } else {
            for (CampaignTermChoice cc : officeList) {
                if (cc.getName().equals(preCompanyTermInstruction.getCampaignTermChoice().getName())) {
                    campaignTermChoice = cc;
                    break;
                }
            }
        }
        instructionManager.saveOrUpdateInstructionByChoice(companyTermContext.getCompanyTerm().getCompany(), campaignTermChoice.getId(), campaignTermChoice.getValue());
    }


    public void randomHuman(CompanyTermContext companyTermContext) {
        CampaignContext campaignContext = companyTermContext.getCampaignContext();
//        Campaign campaign = companyTerm.getCampaign();
        Company company = companyTermContext.getCompanyTerm().getCompany();
        //已招聘的人
        List<CompanyTermInstruction> humanInstructionList = instructionManager.listCompanyInstruction(company, EChoiceBaseType.HUMAN.name());
        int productNum = 0, operationNum = 0, marketNum = 0;
        if (humanInstructionList != null) {
            for (CompanyTermInstruction companyTermInstruction : humanInstructionList) {
                Edept dept = Edept.valueOf(companyTermInstruction.getCampaignTermChoice().getType());
                switch (dept) {
                    case PRODUCT:
                        productNum++;
                        break;
                    case OPERATION:
                        operationNum++;
                        break;
                    case MARKET:
                        marketNum++;
                        break;
                }
            }
        }
        //招聘选择
        List<CampaignTermChoice> campaignTermChoiceList = campaignContext.listCurrentCompanyChoiceByType(EChoiceBaseType.HUMAN.name());
        Map<String, List<CampaignTermChoice>> deptCompanyChoiceMap = new HashMap<>();
        for (CampaignTermChoice campaignTermChoice : campaignTermChoiceList) {
            String humanDept = campaignTermChoice.getType();
            if (deptCompanyChoiceMap.containsKey(humanDept)) {
                deptCompanyChoiceMap.get(humanDept).add(campaignTermChoice);
            }else {
                List<CampaignTermChoice> list = new ArrayList<>();
                list.add(campaignTermChoice);
                deptCompanyChoiceMap.put(humanDept, list);
            }
        }
        //招聘
        for (String dept : deptCompanyChoiceMap.keySet()) {
            List<CampaignTermChoice> list = deptCompanyChoiceMap.get(dept);
            if (list.size() > 0) {
                int num = 1;
                switch (dept) {
                    case "PRODUCT":
                        num = productNum;
                        break;
                    case "OPERATION":
                        num = operationNum;
                        break;
                    case "MARKET":
                        num = marketNum;
                }
                if (num < 2) {
                    CampaignTermChoice campaignTermChoice = list.get(RandomUtil.random(0, list.size()));
                    String[] feeArray = campaignTermChoice.getFees().split(",");
                    String fee = feeArray[RandomUtil.random(0, feeArray.length)];
                    instructionManager.saveOrUpdateInstructionByChoice(company, campaignTermChoice.getId(), fee);
                }
            }
        }

    }*/
}
