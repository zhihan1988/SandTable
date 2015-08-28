package com.rathink.ie.internet.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.foundation.team.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hean on 2015/8/26.
 */
@Service
public class InstructionService {
    @Autowired
    private BaseManager baseManager;
    @Autowired
    private ChoiceService choiceService;

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

    public Integer getDeptAbilityValue(Company company, String dept) {
        Integer deptAbility = 60;
        String hql = "from HrInstruction where status=:status and dept = :dept and company.id = :companyId";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("status", HrInstruction.Status.YXZ.getValue());
        queryParamMap.put("dept", dept);
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
        return 0;
    }

    /**
     * 保存人才选择结果
     * @param company
     * @param human
     * @param fee
     */
    public void saveOrUpdateHrInstruction(Company company, Human human, String fee) {
        Campaign campaign = company.getCampaign();
        HrInstruction hrInstruction = new HrInstruction();
        hrInstruction.setCampaignDate(campaign.getCurrentCampaignDate());
        hrInstruction.setCampaign(campaign);
        hrInstruction.setCompany(company);
        hrInstruction.setDept(human.getDept());
        hrInstruction.setHuman(human);
        hrInstruction.setStatus(HrInstruction.Status.DQD.getValue());
        hrInstruction.setFee(fee);
        baseManager.saveOrUpdate(HrInstruction.class.getName(), hrInstruction);
    }
}
