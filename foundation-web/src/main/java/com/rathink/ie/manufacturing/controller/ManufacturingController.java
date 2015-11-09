package com.rathink.ie.manufacturing.controller;

import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.ming800.core.util.ApplicationContextUtil;
import com.rathink.ie.base.component.CheckOut;
import com.rathink.ie.base.component.DevoteCycle;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.ibase.account.model.Account;
import com.rathink.ie.ibase.controller.BaseIndustryController;
import com.rathink.ie.ibase.property.model.CompanyTerm;
import com.rathink.ie.ibase.service.CampaignCenter;
import com.rathink.ie.ibase.service.CampaignContext;
import com.rathink.ie.ibase.service.CompanyTermContext;
import com.rathink.ie.ibase.work.model.CompanyTermInstruction;
import com.rathink.ie.ibase.work.model.IndustryResource;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.internet.EInstructionStatus;
import com.rathink.ie.manufacturing.*;
import com.rathink.ie.manufacturing.model.*;
import com.rathink.ie.manufacturing.service.ManufacturingImmediatelyManager;
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
    private MarketManager marketManager;
    @Autowired
    private ProductManager productManager;
    @Autowired
    private ManufacturingImmediatelyManager manufacturingImmediatelyManager;
    @Autowired
    private AutoSerialManager autoSerialManager;

    @CheckOut
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

    @RequestMapping("/finishDevotion")
    @ResponseBody
    public Map finishDevotion(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String companyTermId = request.getParameter("companyTermId");

        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);

        DevoteCycle devoteCycle = campaignContext.getDevoteCycle();
        devoteCycle.finishDevote(companyId);

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        List<CompanyTermInstruction> marketFeeInstructionList = instructionManager.listCompanyInstruction(companyTerm, EManufacturingChoiceBaseType.MARKET_FEE.name());
        if (marketFeeInstructionList != null) {
            Integer totalMoney = 0;
            for (CompanyTermInstruction marketFeeInstruction : marketFeeInstructionList) {
                marketFeeInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
                baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), marketFeeInstruction);
                totalMoney += Integer.valueOf(marketFeeInstruction.getValue());
            }
            Account account = accountManager.packageAccount(String.valueOf(totalMoney), EManufacturingAccountEntityType.MARKET_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
            baseManager.saveOrUpdate(Account.class.getName(), account);
        }

        Map map = new HashMap<>();
        map.put("status", 1);
        map.put("cycleStatus", devoteCycle.getCurrentStatus());

        NewReport newReport = new NewReport();
        Company company = (Company) baseManager.getObject(Company.class.getName(), companyId);
        Integer companyCash = accountManager.getCompanyCash(company);
        newReport.setCompanyCash(companyCash);
        map.put("newReport", newReport);
        return map;
    }

    @RequestMapping("/refreshDevoteCycleStatus")
    @ResponseBody
    public Map refreshDevoteCycleStatus(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");

        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(campaignId);
        DevoteCycle devoteCycle = campaignContext.getDevoteCycle();

        Map map = new HashMap<>();
        map.put("status", 1);
        map.put("cycleStatus", devoteCycle.getCurrentStatus());
        if (devoteCycle.getCurrentStatus() == 2) {
            map.put("market", devoteCycle.getCurrentMarket());
            map.put("company", devoteCycle.getCurrentCompany());
            map.put("marketOrderChoiceList", devoteCycle.getCurrentMarketOrdeChoiceList());
            map.put("companyOrderList", devoteCycle.getCompanyOrderMapByMarket());
        }

        return map;
    }


    @RequestMapping("/chooseOrder")
    @ResponseBody
    public Map chooseOrder(HttpServletRequest request, Model model) throws Exception {

        String companyTermId = request.getParameter("companyTermId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        IndustryResourceChoice industryResourceChoice = (IndustryResourceChoice) baseManager.getObject(IndustryResourceChoice.class.getName(), choiceId);

        MarketOrderChoice marketOrderChoice = new MarketOrderChoice(industryResourceChoice);
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setName(marketOrderChoice.getName());
        marketOrder.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
        marketOrder.setDept(EManufacturingDept.MARKET.name());
        marketOrder.setStatus(MarketOrder.Status.NORMAL.name());
        marketOrder.setCampaign(companyTerm.getCampaign());
        marketOrder.setCompany(companyTerm.getCompany());
//                marketOrder.setName();
        marketOrder.setUnitPrice(marketOrderChoice.getUnitPrice());
        marketOrder.setAmount(marketOrderChoice.getAmount());
        marketOrder.setTotalPrice(marketOrderChoice.getTotalPrice());
        marketOrder.setNeedAccountCycle(marketOrderChoice.getAccountPeriod());
        marketOrder.setProductType(marketOrderChoice.getProductType());
        baseManager.saveOrUpdate(MarketOrder.class.getName(), marketOrder);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.PROCESSED.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.MARKET_ORDER.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(marketOrder);
        companyTermInstruction.setCompanyTerm(companyTerm);
        companyTermInstruction.setValue(value);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);


        CampaignContext campaignContext = CampaignCenter.getCampaignHandler(companyTerm.getCampaign().getId());
        DevoteCycle devoteCycle = campaignContext.getDevoteCycle();
        devoteCycle.chooseOrder(choiceId);

        Map result = new HashMap<>();
        result.put("status", 1);
        return result;
    }


    @RequestMapping("/listCurrentMarketOrder")
    @ResponseBody
    public Map listCurrentMarketOrder(HttpServletRequest request, Model model) throws Exception {
        String companyId = request.getParameter("companyId");
        XQuery marketOrderQuery = new XQuery();
        marketOrderQuery.setHql("from MarketOrder where status =:status and company.id = :companyId");
        marketOrderQuery.put("status", MarketOrder.Status.NORMAL.name());
        marketOrderQuery.put("companyId", companyId);
        List<MarketOrder> marketOrderList = baseManager.listObject(marketOrderQuery);

        Map result = new HashMap<>();
        result.put("status", 1);
        result.put("marketOrderList", marketOrderList);

        return result;
    }


    @RequestMapping("/buildProduceLine")
    @ResponseBody
    public Map buildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");
        String produceType = request.getParameter("produceType");
        String lineType = request.getParameter("lineType");

        Map result = manufacturingImmediatelyManager.processProductLineBuild(companyTermId, partId, produceType, lineType);

        return result;
    }

    @RequestMapping("/continueBuildProduceLine")
    @ResponseBody
    public Map continueBuildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");

        Map result = manufacturingImmediatelyManager.processProductLineContinueBuild(companyTermId, partId);
        return result;
    }

    @RequestMapping(value = "/produce")
    @ResponseBody
    public Map produce(HttpServletRequest request, Model model) throws Exception {

        String companyTermId = request.getParameter("companyTermId");
        String produceLineId = request.getParameter("produceLineId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineId);

        Map result = manufacturingImmediatelyManager.processProduce(companyTerm, produceLine);

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

        String fee = materialNum;
        Account account = accountManager.packageAccount(fee, EManufacturingAccountEntityType.PRODUCE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        result.put("status", 1);
        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);
        return result;
    }



    @RequestMapping("/devoteProduct")
    @ResponseBody
    public Map devoteProduct(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();
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

        Integer fee = Product.Type.valueOf(product.getType()).getPerDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("status", 1);
        result.put("newReport", newReport);
        return result;
    }

    @RequestMapping("/devoteMarketArea")
    @ResponseBody
    public Map devoteMarketArea(HttpServletRequest request, Model model) throws Exception {
        Map result = new HashMap<>();
        String companyTermId = request.getParameter("companyTermId");
        String partId = request.getParameter("partId");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        Market market = (Market) baseManager.getObject(Market.class.getName(), partId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.MARKET_DEVOTION.name());
        companyTermInstruction.setDept(EManufacturingDept.MARKET.name());
        companyTermInstruction.setCompanyPart(market);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Integer fee = Market.Type.valueOf(market.getType()).getPerDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.MARKET_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);
        return result;
    }

    @RequestMapping("/deliverOrder")
    @ResponseBody
    public Map deliverOrder(HttpServletRequest request, Model model) throws Exception {

        String companyTermId = request.getParameter("companyTermId");
        String orderId = request.getParameter("orderId");

        Map result = manufacturingImmediatelyManager.processDeliveredOrder(companyTermId, orderId);

        return result;
    }

    @RequestMapping("/loan")
    @ResponseBody
    public Map loan(HttpServletRequest request, Model model) throws Exception {

        String companyTermId = request.getParameter("companyTermId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");
        String type = request.getParameter("type");

        Map result = manufacturingImmediatelyManager.loan(companyTermId, choiceId, value, type);
        return result;
    }

}
