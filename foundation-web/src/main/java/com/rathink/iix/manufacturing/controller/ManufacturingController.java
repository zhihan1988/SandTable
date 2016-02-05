package com.rathink.iix.manufacturing.controller;

import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.iix.ibase.component.CampaignServer;
import com.rathink.iix.ibase.component.MemoryCompany;
import com.rathink.iix.ibase.controller.IBaseController;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCampaign;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCompany;
import com.rathink.iix.manufacturing.service.ManufacturingService;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.component.CheckOut;
import com.rathink.ix.ibase.component.Result;
import com.rathink.ix.ibase.property.model.CompanyTerm;
import com.rathink.ix.ibase.service.CampaignCenter;
import com.rathink.ix.ibase.work.model.CompanyTermInstruction;
import com.rathink.ix.ibase.work.model.IndustryResource;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.internet.EInstructionStatus;
import com.rathink.ix.manufacturing.*;
import com.rathink.ix.manufacturing.component.DevoteCycle;
import com.rathink.ix.manufacturing.component.ManufacturingCampContext;
import com.rathink.ix.manufacturing.model.*;
import com.rathink.ix.manufacturing.service.ManufacturingImmediatelyManager;
import com.rathink.ix.manufacturing.service.ProductManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Hean on 2015/10/7.
 */
@Controller
@RequestMapping("/manufacturing")
public class ManufacturingController extends IBaseController {
    private static Logger logger = LoggerFactory.getLogger(ManufacturingController.class);

    @Autowired
    private ProductManager productManager;
    @Autowired
    private ManufacturingImmediatelyManager manufacturingImmediatelyManager;
    @Autowired
    private AutoSerialManager autoSerialManager;
    @Autowired
    private ManufacturingService manufacturingService;

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        manufacturingService.reset(campaignId);
        manufacturingService.begin(campaignId);
        return "success";
    }
    @CheckOut
    @RequestMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");

        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);
        Campaign campaign = memoryCampaign.getCampaign();
        Company company = memoryCompany.getCompany();

        Integer currentCampaignDateRemainder = campaign.getCurrentCampaignDate() % 4;
        Integer currentSeason = currentCampaignDateRemainder == 0 ? 4 : currentCampaignDateRemainder;//当前季度
        model.addAttribute("currentSeason", currentSeason);
        model.addAttribute("company", company);
        model.addAttribute("campaign", campaign);
        model.addAttribute("companyTerm", null);
        model.addAttribute("companyNum", memoryCampaign.getMemoryCompanyMap().size());

        model.addAttribute("produceLineList",  memoryCompany.getProduceLineMap().values());

        Collection<Material> materialList = memoryCompany.getMaterialMap().values();
        model.addAttribute("materialList", materialList);
        for (Material material : materialList) {
            model.addAttribute(material.getType(), material);
        }

        Collection<Product> productList = memoryCompany.getProductMap().values();
        model.addAttribute("productList", memoryCompany.getProductMap().values());
        for (Product product : productList) {
            model.addAttribute(product.getType(), product);
        }

        Collection<Market> marketList = memoryCompany.getMarketMap().values();
        model.addAttribute("marketList", marketList);

        List<Loan> loanList = memoryCompany.getLoanMap().values()
                .stream()
                .filter(loan -> Loan.Status.NORMAL.name().equals(loan.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("loanList", loanList);

        List<MarketOrder> marketOrderList = memoryCompany.getMarketOrderMap().values()
                .stream()
                .filter(marketOrder -> MarketOrder.Status.NORMAL.name().equals(marketOrder.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("marketOrderList", marketOrderList);

        model.addAttribute("longTermLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_LONG_TERM.name()));
        model.addAttribute("shortTermLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_SHORT_TERM.name()));
        model.addAttribute("usuriousLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_USURIOUS.name()));

        if (currentSeason == 1) {
            IndustryResource marketFeeResource = memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.MARKET_FEE.name());
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

            model.addAttribute("marketOrderResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.MARKET_ORDER.name()));

        }

        Integer companyCash = memoryCompany.getCompanyCash();
        model.addAttribute("companyCash", companyCash);
        Integer longTermLoan = memoryCompany.getLoan(EManufacturingAccountEntityType.LOAN_LONG_TERM.name());
        model.addAttribute("longTermLoan", longTermLoan);
        Integer shortTermLoan = memoryCompany.getLoan(EManufacturingAccountEntityType.LOAN_SHORT_TERM.name());
        model.addAttribute("shortTermLoan", shortTermLoan);
        Integer usuriousLoan = memoryCompany.getLoan(EManufacturingAccountEntityType.LOAN_USURIOUS.name());
        model.addAttribute("usuriousLoan", usuriousLoan);

        model.addAttribute("roundType", EManufacturingRoundType.DATE_ROUND.name());
        return "/manufacturing/main";
    }

    @RequestMapping("/finishDevotion")
    @ResponseBody
    public Map finishDevotion(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String companyTermId = request.getParameter("companyTermId");

        ManufacturingCampContext campaignContext = (ManufacturingCampContext) CampaignCenter.getCampaignHandler(campaignId);

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

        ManufacturingCampContext campaignContext = (ManufacturingCampContext) CampaignCenter.getCampaignHandler(campaignId);
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
        marketOrder.setProfit(marketOrderChoice.getProfit());
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


        ManufacturingCampContext campaignContext = (ManufacturingCampContext) CampaignCenter.getCampaignHandler(companyTerm.getCampaign().getId());
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

    @RequestMapping("/currentLineState")
    @ResponseBody
    public Map currentLineState(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String lineId = request.getParameter("lineId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(lineId);
        Map map = new HashMap<>();
        map.put("status", 1);
        map.put("line", produceLine);
        return map;
    }


    @RequestMapping("/buildProduceLine")
    @ResponseBody
    public Result buildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String partId = request.getParameter("partId");
        String produceType = request.getParameter("produceType");
        String lineType = request.getParameter("lineType");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(partId);
        produceLine.setProduceType(produceType);
        produceLine.setProduceLineType(lineType);
        Integer lineBuildNeedCycle = ProduceLine.Type.valueOf(lineType).getInstallCycle();
        if (lineBuildNeedCycle > 0) {
            lineBuildNeedCycle--;
        }
        produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);
        if (lineBuildNeedCycle == 0) {//建造完成
            produceLine.setStatus(ProduceLine.Status.FREE.name());
        } else {
            produceLine.setStatus(ProduceLine.Status.BUILDING.name());
        }

        Integer fee = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);


        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.setAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping("/continueBuildProduceLine")
    @ResponseBody
    public Result continueBuildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String partId = request.getParameter("partId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(partId);
        Integer lineBuildNeedCycle = Integer.valueOf(produceLine.getLineBuildNeedCycle());
        if (lineBuildNeedCycle > 0) {
            lineBuildNeedCycle--;
            produceLine.setLineBuildNeedCycle(lineBuildNeedCycle);
        }
        if (lineBuildNeedCycle == 0) {//建造完成
            produceLine.setStatus(ProduceLine.Status.FREE.name());
        } else {
            produceLine.setStatus(ProduceLine.Status.BUILDING.name());
        }

        Integer fee = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getPerBuildDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_LINE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);


        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.setAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping("/reBuildProduceLine")
    @ResponseBody
    public Map reBuildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String companyTermId = request.getParameter("companyTermId");
        String lineId = request.getParameter("lineId");
        String produceType = request.getParameter("produceType");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), lineId);

        CompanyTermInstruction companyTermInstruction = new CompanyTermInstruction();
        companyTermInstruction.setStatus(EInstructionStatus.UN_PROCESS.getValue());
        companyTermInstruction.setBaseType(EManufacturingInstructionBaseType.PRODUCE_LINE_REBUILD.name());
        companyTermInstruction.setDept(EManufacturingDept.PRODUCT.name());
        companyTermInstruction.setCompanyPart(produceLine);
        companyTermInstruction.setCompanyTerm(companyTerm);
        baseManager.saveOrUpdate(CompanyTermInstruction.class.getName(), companyTermInstruction);

        Integer transferCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getTransferCycle();
        produceLine.setStatus(ProduceLine.Status.REBUILDING.name());
        produceLine.setLineBuildNeedCycle(transferCycle);
        produceLine.setProduceType(produceType);
        baseManager.saveOrUpdate(ProduceLine.class.getName(), produceLine);

        Map result = new HashMap<>();
        result.put("status", 1);
        result.put("line",produceLine);
        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("newReport", newReport);

        return result;
    }

    @RequestMapping(value = "/produce")
    @ResponseBody
    public Map produce(HttpServletRequest request, Model model) throws Exception {

        String companyTermId = request.getParameter("companyTermId");
        String produceLineId = request.getParameter("produceLineId");
        String produceType = request.getParameter("produceType");

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        ProduceLine produceLine = (ProduceLine) baseManager.getObject(ProduceLine.class.getName(), produceLineId);
        if (ProduceLine.Type.FLEXBILITY.name().equals(produceLine.getProduceLineType())
                || ProduceLine.Type.MANUAL.name().equals(produceLine.getProduceLineType())) {
            produceLine.setProduceType(produceType);//processProduce 方法顺利执行的话 会被保存
        }
        Product product = productManager.getProduct(companyTerm.getCompany(), produceLine.getProduceType());
        if (product.getDevelopNeedCycle() > 0) {
            Map result = new HashMap<>();
            result.put("status", "-1");
            result.put("message", "该产品未被研发");
            return result;
        } else {
            Map result = manufacturingImmediatelyManager.processProduce(companyTerm, produceLine);
            return result;
        }
    }

    @RequestMapping("/devoteMarket")
    @ResponseBody
    public Map devoteMarket(HttpServletRequest request, Model model) throws Exception {
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
        Account account = accountManager.packageAccount(String.valueOf(fee)
                , EManufacturingAccountEntityType.MARKET_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), companyTerm);
        baseManager.saveOrUpdate(Account.class.getName(), account);

        NewReport newReport = new NewReport();
        Integer companyCash = accountManager.getCompanyCash(companyTerm.getCompany());
        newReport.setCompanyCash(companyCash);
        result.put("status", 1);
        result.put("newReport", newReport);
        return result;
    }

    @RequestMapping("/purchase")
    @ResponseBody
    public Result purchase(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String materialId = request.getParameter("materialId");
        String materialNum = request.getParameter("materialNum");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        Material material = memoryCompany.getMaterialMap().get(materialId);
        material.setStatus(Material.Status.PURCHASING.name());
        material.setPurchasingAmount(Integer.parseInt(materialNum));

        String fee = materialNum;
        Account account = accountManager.packageAccount(fee, EManufacturingAccountEntityType.FLOATING_CAPITAL_MATERIAL.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setAttribute("material", material);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.setAttribute("newReport", newReport);
        return result;
    }



    @RequestMapping("/devoteProduct")
    @ResponseBody
    public Result devoteProduct(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String partId = request.getParameter("partId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        Product product = memoryCompany.getProductMap().get(partId);
        product.setStatus(Product.Status.DEVELOPING.name());

        Integer fee = Product.Type.valueOf(product.getType()).getPerDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee), EManufacturingAccountEntityType.PRODUCT_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.setAttribute("product", product);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.setAttribute("newReport", newReport);
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

        CompanyTerm companyTerm = (CompanyTerm) baseManager.getObject(CompanyTerm.class.getName(), companyTermId);
        Company company = companyTerm.getCompany();

        Integer maxEnableLoanMoney = manufacturingImmediatelyManager.getMaxLoanMoney(company, type);
        if (Integer.valueOf(value) > maxEnableLoanMoney) {
            Map map = new HashMap<>();
            map.put("status", "-1");
            map.put("message", "贷款失败，最大可贷金额：" + maxEnableLoanMoney + "M");
            return map;
        } else {
            Map result = manufacturingImmediatelyManager.loan(companyTermId, choiceId, value, type);
            return result;
        }
    }



    @RequestMapping("/loanList")
    @ResponseBody
    public Map loanList(HttpServletRequest request, Model model) throws Exception {

        String companyId = request.getParameter("companyId");

        XQuery loanQuery = new XQuery();
        loanQuery.setHql("from Loan where company.id = :companyId and status = :status");
        loanQuery.put("companyId", companyId);
        loanQuery.put("status", Loan.Status.NORMAL.name());
        List<Loan> loanList = baseManager.listObject(loanQuery);

        Map result = new HashMap<>();
        result.put("status", 1);
        result.put("loanList", loanList);
        return result;
    }

}
