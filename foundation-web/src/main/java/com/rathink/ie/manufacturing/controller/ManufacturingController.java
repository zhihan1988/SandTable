package com.rathink.ie.manufacturing.controller;

import com.ming800.core.p.service.AutoSerialManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.service.RoundEndObserable;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.controller.BaseIndustryController;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.*;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.manufacturing.*;
import com.rathink.ie.manufacturing.model.Market;
import com.rathink.ie.manufacturing.model.Material;
import com.rathink.ie.manufacturing.model.ProduceLine;
import com.rathink.ie.manufacturing.model.Product;
import com.rathink.ie.manufacturing.service.MaterialManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Hean on 2015/10/7.
 */
@Controller
@RequestMapping("/manufacturing")
public class ManufacturingController extends BaseIndustryController {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingController.class);
    @Autowired
    private MaterialManager materialManager;

    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);
        Campaign campaign = campaignContext.getCampaign();
        CompanyTermContext companyTermContext = campaignContext.getCompanyTermContextMap().get(companyId);
        CompanyTerm companyTerm = companyTermContext.getCompanyTerm();
        Company company = companyTerm.getCompany();
        CompanyTerm preCompanyTerm = companyTermContext.getPreCompanyTermContext().getCompanyTerm();
        Integer currentCampaignDate = campaign.getCurrentCampaignDate();
        Map<String, IndustryResource> industryResourceMap = campaignContext.getCurrentTypeIndustryResourceMap();

        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("companyTerm", companyTerm);
        model.addAttribute("companyNum", campaignContext.getCompanyTermContextMap().size());
        if (currentCampaignDate % 4 == 1) {

            model.addAttribute("marketFeeResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_FEE.name()));
           /* Map<String, Observable> observableMap = campaignContext.getObservableMap();
            RoundEndObserable marketFeeObervable = (RoundEndObserable)observableMap.get(currentCampaignDate + ":" + EManufacturingRoundType.MARKET_PAY_ROUND.name());
            if (marketFeeObervable.getUnFinishedNum() != 0) {
                //进入投标环节
                model.addAttribute("marketFeeResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_FEE.name()));
                model.addAttribute("roundType", EManufacturingRoundType.MARKET_PAY_ROUND.name());
                return "/manufacturing/marketPay";
            }
            RoundEndObserable marketOrderObervable = (RoundEndObserable)observableMap.get(currentCampaignDate + ":" + EManufacturingRoundType.ORDER_ROUND.name());
            if (marketOrderObervable.getUnFinishedNum() != 0) {
                //进入抢单环节
                List<CompanyTermInstruction> marketFeeInstructionList = instructionManager.listCompanyInstruction(companyTerm.getCampaign()
                        , companyTerm.getCampaignDate(), EManufacturingChoiceBaseType.MARKET_FEE.name());
                marketFeeInstructionList.sort((o1, o2) -> Integer.valueOf(o1.getValue()) - Integer.valueOf(o2.getValue()));
                model.addAttribute("marketFeeInstructionList", marketFeeInstructionList);
                model.addAttribute("marketOrderResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_ORDER.name()));
                model.addAttribute("roundType", EManufacturingRoundType.ORDER_ROUND.name());
                return "/manufacturing/order";
            }*/
        }

        List<ProduceLine> produceLineList = baseManager.listObject("from ProduceLine where company.id =" + companyId);
        model.addAttribute("produceLineList", produceLineList);
        List<Material> materialList = baseManager.listObject("from Material where company.id =" + companyId);
        model.addAttribute("materialList", materialList);
        List<Product> productList = baseManager.listObject("from Product where company.id =" + companyId);
        model.addAttribute("productList", productList);
        model.addAttribute("roundType", EManufacturingRoundType.DATE_ROUND.name());
        return "/manufacturing/main";
    }


    @RequestMapping("/develop")
    @ResponseBody
    public Map develop(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        Product product = (Product) baseManager.getObject(Product.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingChoiceBaseType.PRODUCT.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(product);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Map result = new HashMap<>();
        result.put("status", 1);
        return result;
    }


    @RequestMapping("/buildProduceLine")
    @ResponseBody
    public Map processInstruction(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");
        String produceType = request.getParameter("produceType");
        String lineType = request.getParameter("lineType");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        companyTermInstruction.setBaseType(EManufacturingChoiceBaseType.PRODUCE_LINE.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setValue(produceType + ";" + lineType);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        produceLine.setProduceLineType(lineType);
        produceLine.setProduceType(produceType);
        if (lineType.equals(ProduceLine.Type.MANUAL.name())) {
            produceLine.setStatus(ProduceLine.Status.FREE.name());
        } else {
            produceLine.setStatus(ProduceLine.Status.BUILDING.name());
        }
        Integer installCycle = ProduceLine.Type.valueOf(lineType).getInstallCycle();
        produceLine.setProduceNeedCycle(installCycle);

        baseManager.saveOrUpdate(CompanyPart.class.getName(), produceLine);

        Map result = new HashMap<>();
        result.put("installCycle", installCycle);
        return result;
    }

    @RequestMapping("/continueBuildProduceLine")
    @ResponseBody
    public Map continueBuildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");
        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingChoiceBaseType.PRODUCE_LINE.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Map result = new HashMap<>();
        result.put("status", 1);
        return result;
    }

    @RequestMapping(value = "/produce")
    @ResponseBody
    public Map produce(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();

        String companyTermId = request.getParameter("companyTermId");
        String produceLineId = request.getParameter("produceLineId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineId);
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
                    R2Amount = R2Amount-1;
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
                    R3Amount = R3Amount-1;
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
                    R3Amount = R3Amount-1;
                    R3.setAmount(R3Amount);
                    baseManager.saveOrUpdate(Material.class.getName(), R3);
                    R4Amount = R4Amount-1;
                    R4.setAmount(R4Amount);
                    baseManager.saveOrUpdate(Material.class.getName(), R4);
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

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        companyTermInstruction.setBaseType(EManufacturingChoiceBaseType.PRODUCT.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setValue(produceLine.getProduceType());
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        produceLine.setStatus(ProduceLine.Status.PRODUCING.name());
        Integer produceCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getProduceCycle();
        produceLine.setProduceNeedCycle(produceCycle);
        baseManager.saveOrUpdate(CompanyPart.class.getName(), produceLine);



        result.put("status", 1);
        result.put("message", "生产中");
        return result;
    }

    @RequestMapping("/purchase")
    @ResponseBody
    public Map purchase(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();
        String companyTermId = request.getParameter("companyTermId");
        String materialId = request.getParameter("materialId");
        String materialNum = request.getParameter("materialNum");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        Material material = (Material) baseManager.getObject(Material.class.getName(), materialId);
        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingChoiceBaseType.MATERIAL.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(material);
        companyTermInstruction.setValue(materialNum);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        result.put("status", 1);
        return result;
    }


}
