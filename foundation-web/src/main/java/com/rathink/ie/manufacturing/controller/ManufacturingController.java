package com.rathink.ie.manufacturing.controller;

import com.ming800.core.does.model.XQuery;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.controller.BaseIndustryController;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyPart;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.manufacturing.EManufacturingChoiceBaseType;
import com.rathink.ie.manufacturing.EManufacturingDept;
import com.rathink.ie.manufacturing.EManufacturingInstructionBaseType;
import com.rathink.ie.manufacturing.EManufacturingRoundType;
import com.rathink.ie.manufacturing.model.*;
import com.rathink.ie.manufacturing.service.MarketManager;
import com.rathink.ie.manufacturing.service.MaterialManager;
import com.rathink.ie.manufacturing.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.logging.XMLFormatter;

/**
 * Created by Hean on 2015/10/7.
 */
@Controller
@RequestMapping("/manufacturing")
public class ManufacturingController extends BaseIndustryController {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingController.class);
    @Autowired
    private MaterialManager materialManager;
    @Autowired
    protected MarketManager marketManager;
    @Autowired
    protected ProductManager productManager;

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
        List<ProduceLine> produceLineList = baseManager.listObject("from ProduceLine where company.id =" + companyId);
        model.addAttribute("produceLineList", produceLineList);
        List<Material> materialList = baseManager.listObject("from Material where company.id =" + companyId);
        model.addAttribute("materialList", materialList);
        List<Product> productList = baseManager.listObject("from Product where company.id =" + companyId);
        model.addAttribute("productList", productList);
        List<Market> marketList = baseManager.listObject("from Market where company.id =" + companyId);
        model.addAttribute("marketList", marketList);
//        List<MarketOrder> marketOrderList
        XQuery marketOrderQuery = new XQuery();
        marketOrderQuery.setHql("from MarketOrder where status =:status");
        marketOrderQuery.put("status", MarketOrder.Status.NORMAL.name());
        List<MarketOrder> marketOrderList = baseManager.listObject(marketOrderQuery);
        model.addAttribute("marketOrderList", marketOrderList);
        model.addAttribute("longTermLoanResource", industryResourceMap.get(EManufacturingChoiceBaseType.LONG_TERM_LOAN.name()));
        model.addAttribute("shortTermLoanResource", industryResourceMap.get(EManufacturingChoiceBaseType.SHORT_TERM_LOAN.name()));
        model.addAttribute("usuriousLoanResource", industryResourceMap.get(EManufacturingChoiceBaseType.USURIOUS_LOAN.name()));

        if (currentCampaignDate % 4 == 1) {
            IndustryResource marketFeeResource = industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_FEE.name());
            Map<String, IndustryResourceChoice> resourceChoiceMap = new HashMap<>();
            for (IndustryResourceChoice marketChoice : marketFeeResource.getCurrentIndustryResourceChoiceSet()) {
                resourceChoiceMap.put(marketChoice.getType(), marketChoice);
            }

            Map marketMap = new HashMap<>();
            for (Market market : marketList) {
                if (market.getDevotionNeedCycle() == 0) {
                    Map productMap = new LinkedHashMap<>();
                    for (Product product : productList) {
                        if (product.getDevelopNeedCycle() == 0) {
                            productMap.put(product.getType(), resourceChoiceMap.get(market.getType() + "_" + product.getType()));
                        }
                    }
                    marketMap.put(market.getType(), productMap);
                }
            }
            model.addAttribute("marketMap", marketMap);

            model.addAttribute("marketOrderResource", industryResourceMap.get(EManufacturingChoiceBaseType.MARKET_ORDER.name()));

//            IndustryResourceChoice[][] marketChoiceArray = marketManager.getMarketChoiceArray(marketList, productList, marketFeeResource.getCurrentIndustryResourceChoiceSet());
//            model.addAttribute("marketChoiceArray", marketChoiceArray);
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

        Integer companyCash = accountManager.getCompanyCash(company);
        model.addAttribute("companyCash", companyCash);

        model.addAttribute("roundType", EManufacturingRoundType.DATE_ROUND.name());
        return "/manufacturing/main";
    }


    @RequestMapping("/devoteProduct")
    @ResponseBody
    public Map devoteProduct(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        Product product = (Product) baseManager.getObject(Product.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCT_DEVOTION.name());
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
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setValue(produceType + ";" + lineType);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Integer installCycle = ProduceLine.Type.valueOf(lineType).getInstallCycle();

        Map result = new HashMap<>();
        result.put("status", 1);
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
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE_LINE_BUILD_CONTINUE.name());
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

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE.name());
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
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.MATERIAL_PURCHASE.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(material);
        companyTermInstruction.setValue(materialNum);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        result.put("status", 1);
        return result;
    }

    @RequestMapping("/deliverOrder")
    @ResponseBody
    public Map deliverOrder(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();
        String companyTermId = request.getParameter("companyTermId");
        String orderId = request.getParameter("orderId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        MarketOrder marketOrder = (MarketOrder) baseManager.getObject(MarketOrder.class.getName(), orderId);
        Product product = productManager.getProduct(companyTerm.getCompany(), marketOrder.getProductType());

        if (marketOrder.getAmount() > product.getAmount()) {
            result.put("status", 0);
            result.put("message", "产品库存不足");

        } else {
            CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
            companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
            companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.ORDER_DELIVER.name());
            companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
            companyTermInstruction.setCompanyPart(marketOrder);
            companyTermInstruction.setCompanyTerm(companyTerm);
            baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

            result.put("status", 1);
        }

        return result;
    }

}
