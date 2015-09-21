package com.rathink.ie.internet.service.impl;

import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.RandomUtil;
import com.rathink.ie.ibase.property.model.CompanyTerm;
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
    public void randomInstruction(CompanyTerm companyTerm) {
        keepPre(companyTerm, EChoiceBaseType.OFFICE.name());
//        keepPre(companyTerm,EChoiceBaseType.PRODUCT_STUDY.name());
       /* randomFee(companyTerm, EChoiceBaseType.PRODUCT_STUDY_FEE.name());
        randomFee(companyTerm, EChoiceBaseType.MARKET_ACTIVITY.name());
        randomFee(companyTerm, EChoiceBaseType.OPERATION.name());
        randomHuman(companyTerm);*/
    }

    public void randomFee(CompanyTerm companyTerm, String baseType) {
        Campaign campaign = companyTerm.getCampaign();
        List<CompanyChoice> companyChoiceList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate(), baseType);
        CompanyChoice companyChoice = companyChoiceList.get(RandomUtil.random(0, companyChoiceList.size()));
        String[] feeArray = companyChoice.getFees().split(",");
        String fee = feeArray[RandomUtil.random(0, feeArray.length)];
        instructionManager.saveOrUpdateInstruction(companyTerm.getCompany(), companyChoice.getId(), fee);
    }

    public void keepPre(CompanyTerm companyTerm, String baseType) {
        Campaign campaign = companyTerm.getCampaign();
        Company company = companyTerm.getCompany();
        CompanyInstruction companyInstruction = instructionManager.getUniqueInstruction(companyTerm, baseType);
        CompanyChoice companyChoice = null;
        List<CompanyChoice> officeList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate(), baseType);
        if (companyInstruction == null) {
            companyChoice = officeList.get(RandomUtil.random(0, officeList.size()));
        } else {
            for (CompanyChoice cc : officeList) {
                if (cc.getName().equals(companyInstruction.getCompanyChoice().getName())) {
                    companyChoice = cc;
                }
            }
        }
        instructionManager.saveOrUpdateInstruction(company, companyChoice.getId(), companyChoice.getValue());
    }


    public void randomHuman(CompanyTerm companyTerm) {
        Campaign campaign = companyTerm.getCampaign();
        Company company = companyTerm.getCompany();
        //已招聘的人
        List<CompanyInstruction> humanInstructionList = instructionManager.listCompanyInstructionByType(company, EChoiceBaseType.HUMAN.name());
        int productNum = 0, operationNum = 0, marketNum = 0;
        if (humanInstructionList != null) {
            for (CompanyInstruction companyInstruction : humanInstructionList) {
                Edept dept = Edept.valueOf(companyInstruction.getDept());
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
        List<CompanyChoice> companyChoiceList = choiceManager.listCompanyChoice(campaign.getId(), campaign.getCurrentCampaignDate(), EChoiceBaseType.HUMAN.name());
        Map<String, List<CompanyChoice>> deptCompanyChoiceMap = new HashMap<>();
        for (CompanyChoice companyChoice : companyChoiceList) {
            String dept = companyChoice.getDept();
            if (deptCompanyChoiceMap.containsKey(dept)) {
                deptCompanyChoiceMap.get(dept).add(companyChoice);
            }else {
                List<CompanyChoice> list = new ArrayList<>();
                list.add(companyChoice);
                deptCompanyChoiceMap.put(dept, list);
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
                if (num < 1) {
                    CompanyChoice companyChoice = list.get(RandomUtil.random(0, list.size()));
                    String[] feeArray = companyChoice.getFees().split(",");
                    String fee = feeArray[RandomUtil.random(0, feeArray.length)];
                    instructionManager.saveOrUpdateInstruction(company, companyChoice.getId(), fee);
                }
            }
        }

    }
}
