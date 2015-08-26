package com.rathink.ie.foundation.service;

import com.ming800.core.base.service.BaseManager;
import com.ming800.core.does.model.XQuery;
import com.rathink.ie.campaign.model.Campaign;
import com.rathink.ie.internet.choice.model.Human;
import com.rathink.ie.internet.instruction.model.HrInstruction;
import com.rathink.ie.team.model.Company;
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

    public String getDeptAbilityValue(Company company, String dept) {
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
                String level = hrInstruction.getHuman().getHard();
                switch (level) {
                    case "A":
                        deptAbility += 10;
                        break;
                    case "B":
                        deptAbility += 8;
                        break;
                    case "C":
                        deptAbility += 6;
                        break;
                }
            }
        }
        return String.valueOf(deptAbility);
    }

    public String getNewUserAmount(Company company) {
        return String.valueOf(0);
    }

}
