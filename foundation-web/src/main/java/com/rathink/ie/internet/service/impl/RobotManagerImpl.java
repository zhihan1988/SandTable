package com.rathink.ie.internet.service.impl;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.service.CompanyTermManager;
import com.rathink.ie.ibase.work.model.CompanyChoice;
import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.EChoiceBaseType;
import com.rathink.ie.internet.Edept;
import com.rathink.ie.internet.service.ChoiceManager;
import com.rathink.ie.internet.service.InstructionManager;
import com.rathink.ie.internet.service.RobotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hean on 2015/9/21.
 */
@Service
public class RobotManagerImpl implements RobotManager {
    @Autowired
    private ChoiceManager choiceManager;
    @Autowired
    private InstructionManager instructionManager;
    @Autowired
    private CompanyTermManager companyTermManager;

    @Override
    public void randomInstruction(CompanyTermContext companyTermContext) {
        keepPre(companyTermContext, EChoiceBaseType.OFFICE.name());
        keepPre(companyTermContext, EChoiceBaseType.PRODUCT_STUDY.name());
        randomFee(companyTermContext, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        randomFee(companyTermContext, EChoiceBaseType.MARKET_ACTIVITY.name());
        randomFee(companyTermContext, EChoiceBaseType.OPERATION.name());
        randomHuman(companyTermContext);
    }

    public void randomFee(CompanyTermContext companyTermContext, String baseType) {
        CampaignContext campaignContext = companyTermContext.getCampaignContext();
        List<CompanyChoice> companyChoiceList = campaignContext.listCurrentCompanyChoiceByType(baseType);
        CompanyChoice companyChoice = companyChoiceList.get(RandomUtil.random(0, companyChoiceList.size()));
        String[] feeArray = companyChoice.getFees().split(",");
        String fee = feeArray[RandomUtil.random(0, feeArray.length)];
        instructionManager.saveOrUpdateInstructionByChoice(companyTermContext.getCompanyTerm().getCompany(), companyChoice.getId(), fee);
    }

    public void keepPre(CompanyTermContext companyTermContext, String baseType) {
        CompanyInstruction companyInstruction = instructionManager.getUniqueInstructionByBaseType(companyTermContext.getCompanyTerm(), baseType);
        if (companyInstruction != null) return;

        CompanyInstruction preCompanyInstruction = instructionManager.getUniqueInstructionByBaseType(companyTermContext.getPreCompanyTermContext().getCompanyTerm(), baseType);
        CompanyChoice companyChoice = null;
        List<CompanyChoice> officeList = companyTermContext.getCampaignContext().listCurrentCompanyChoiceByType(baseType);
        if (preCompanyInstruction == null) {
            companyChoice = officeList.get(RandomUtil.random(0, officeList.size()));
        } else {
            for (CompanyChoice cc : officeList) {
                if (cc.getName().equals(preCompanyInstruction.getCompanyChoice().getName())) {
                    companyChoice = cc;
                    break;
                }
            }
        }
        instructionManager.saveOrUpdateInstructionByChoice(companyTermContext.getCompanyTerm().getCompany(), companyChoice.getId(), companyChoice.getValue());
    }


    public void randomHuman(CompanyTermContext companyTermContext) {
        CampaignContext campaignContext = companyTermContext.getCampaignContext();
//        Campaign campaign = companyTerm.getCampaign();
        Company company = companyTermContext.getCompanyTerm().getCompany();
        //已招聘的人
        List<CompanyInstruction> humanInstructionList = instructionManager.listCompanyInstructionByType(company, EChoiceBaseType.HUMAN.name());
        int productNum = 0, operationNum = 0, marketNum = 0;
        if (humanInstructionList != null) {
            for (CompanyInstruction companyInstruction : humanInstructionList) {
                Edept dept = Edept.valueOf(companyInstruction.getCompanyChoice().getType());
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
        List<CompanyChoice> companyChoiceList = campaignContext.listCurrentCompanyChoiceByType(EChoiceBaseType.HUMAN.name());
        Map<String, List<CompanyChoice>> deptCompanyChoiceMap = new HashMap<>();
        for (CompanyChoice companyChoice : companyChoiceList) {
            String humanDept = companyChoice.getType();
            if (deptCompanyChoiceMap.containsKey(humanDept)) {
                deptCompanyChoiceMap.get(humanDept).add(companyChoice);
            }else {
                List<CompanyChoice> list = new ArrayList<>();
                list.add(companyChoice);
                deptCompanyChoiceMap.put(humanDept, list);
            }
        }
        //招聘
        for (String dept : deptCompanyChoiceMap.keySet()) {
            List<CompanyChoice> list = deptCompanyChoiceMap.get(dept);
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
                    CompanyChoice companyChoice = list.get(RandomUtil.random(0, list.size()));
                    String[] feeArray = companyChoice.getFees().split(",");
                    String fee = feeArray[RandomUtil.random(0, feeArray.length)];
                    instructionManager.saveOrUpdateInstructionByChoice(company, companyChoice.getId(), fee);
                }
            }
        }

    }
}
