package com.rathink.iix.manufacturing.controller;

import com.ming800.core.does.model.XQuery;
import com.ming800.core.p.service.AutoSerialManager;
import com.rathink.ie.foundation.campaign.model.Campaign;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.foundation.util.GenerateUtil;
import com.rathink.iix.ibase.component.CampaignServer;
import com.rathink.iix.ibase.controller.IBaseController;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCampaign;
import com.rathink.iix.manufacturing.component.ManufacturingMemoryCompany;
import com.rathink.iix.manufacturing.component.MarketBiddingParty;
import com.rathink.iix.manufacturing.component.MarketOrderParty;
import com.rathink.iix.manufacturing.service.ManufacturingService;
import com.rathink.ix.ibase.account.model.Account;
import com.rathink.ix.ibase.component.CheckOut;
import com.rathink.ix.ibase.component.Result;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.manufacturing.*;
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

        //主页
        model.addAttribute("companyCash", memoryCompany.getCompanyCash());
        model.addAttribute("longTermLoan", memoryCompany.getLongTermLoan());
        model.addAttribute("shortTermLoan", memoryCompany.getShortTermLoan());
        model.addAttribute("usuriousLoan", memoryCompany.getUsuriousLoan());

        //生产
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
        model.addAttribute("produceLineList",  memoryCompany.getProduceLineMap().values());


        //市场
        Collection<Market> marketList = memoryCompany.getMarketMap().values();
        model.addAttribute("marketList", marketList);

        if (currentSeason == 1) {    //竞标
            model.addAttribute("marketFeeResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.MARKET_FEE.name()));
            model.addAttribute("marketOrderResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.MARKET_ORDER.name()));
        }

        List<MarketOrder> marketOrderList = memoryCompany.getMarketOrderMap().values()
                .stream()
                .filter(marketOrder -> MarketOrder.Status.NORMAL.name().equals(marketOrder.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("marketOrderList", marketOrderList);


        //财务
        List<Loan> loanList = memoryCompany.getLoanMap().values()
                .stream()
                .filter(loan -> Loan.Status.NORMAL.name().equals(loan.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("loanList", loanList);
        model.addAttribute("longTermLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_LONG_TERM.name()));
        model.addAttribute("shortTermLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_SHORT_TERM.name()));
        model.addAttribute("usuriousLoanResource", memoryCampaign.getIndustryResource(EManufacturingChoiceBaseType.LOAN_USURIOUS.name()));

        return "/manufacturing/main";
    }


    @RequestMapping("/finishDevotion")
    @ResponseBody
    public Result finishDevotion(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String devotions = request.getParameter("devotions");

        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();
        MarketBiddingParty marketBiddingParty = (MarketBiddingParty) memoryCampaign.getCampaignPartyMap().get("BIDDING");

        Integer totalMarketFee = 0;
        try {
            for (String devotion : devotions.split(";")) {
                String[] devotionArray = devotion.split(":");
                String marketType = devotionArray[0];
                Integer fee = Integer.valueOf(devotionArray[1]);
                marketBiddingParty.addBidding(companyId, marketType, fee);

                totalMarketFee += fee;
            }
        } catch (Exception e) {
            logger.error("竞标数据格式异常：" + devotions, e);
            Result result = new Result();
            result.setStatus(Result.FAILED);
            result.setMessage("竞标数据格式异常");
            return result;
        }


        Account account = accountManager.packageAccount(String.valueOf(totalMarketFee), EManufacturingAccountEntityType.MARKET_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);

        marketBiddingParty.join(companyId);


        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping("/listOrderForChoose")
    @ResponseBody
    public Result listOrderForChoose(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        MarketOrderParty marketOrderParty = (MarketOrderParty) memoryCampaign.getCampaignPartyMap().get(MarketOrderParty.TYPE);
        MarketBiddingParty marketBiddingParty = (MarketBiddingParty) memoryCampaign.getCampaignPartyMap().get(MarketBiddingParty.TYPE);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("market", marketOrderParty.getCurrentMarket());
        result.addAttribute("company", marketOrderParty.getCurrentCompany());
        result.addAttribute("marketOrderChoiceList", marketOrderParty.getCurrentMarketOrderChoiceList());
        result.addAttribute("biddingResult",marketBiddingParty.getBiddingResult(marketOrderParty.getCurrentMarket()));
        return result;
    }

    @RequestMapping("/chooseOrder")
    @ResponseBody
    public Result chooseOrder(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String value = request.getParameter("value");

        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);
        Campaign campaign = memoryCampaign.getCampaign();
        Company company = memoryCompany.getCompany();


        IndustryResourceChoice industryResourceChoice = (IndustryResourceChoice) baseManager.getObject(IndustryResourceChoice.class.getName(), choiceId);

        MarketOrderParty marketOrderParty = (MarketOrderParty) memoryCampaign.getCampaignPartyMap().get("ORDER");
        marketOrderParty.chooseOrder(companyId, choiceId);

        MarketOrderChoice marketOrderChoice = new MarketOrderChoice(industryResourceChoice);
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setName(marketOrderChoice.getName());
        marketOrder.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
        marketOrder.setDept(EManufacturingDept.MARKET.name());
        marketOrder.setStatus(MarketOrder.Status.NORMAL.name());
        marketOrder.setCampaign(campaign);
        marketOrder.setCompany(company);
        marketOrder.setUnitPrice(marketOrderChoice.getUnitPrice());
        marketOrder.setAmount(marketOrderChoice.getAmount());
        marketOrder.setTotalPrice(marketOrderChoice.getTotalPrice());
        marketOrder.setProfit(marketOrderChoice.getProfit());
        marketOrder.setNeedAccountCycle(marketOrderChoice.getAccountPeriod());
        marketOrder.setProductType(marketOrderChoice.getProductType());

        memoryCompany.getMarketOrderMap().put(marketOrder.getId(), marketOrder);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        return result;
    }


    @RequestMapping("/listCurrentMarketOrder")
    @ResponseBody
    public Result listCurrentMarketOrder(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");

        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);

        List<MarketOrder> marketOrderList = memoryCompany.getMarketOrderMap().values().stream()
                .filter(marketOrder -> marketOrder.getStatus().equals(MarketOrder.Status.NORMAL.name()))
                .collect(Collectors.toList());

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("marketOrderList", marketOrderList);
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

    @RequestMapping("/devoteMarket")
    @ResponseBody
    public Result devoteMarket(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String partId = request.getParameter("partId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        Market market = memoryCompany.getMarketMap().get(partId);
        market.setStatus(Market.Status.DEVELOPING.name());
        Integer fee = Market.Type.valueOf(market.getType()).getPerDevotion();
        Account account = accountManager.packageAccount(String.valueOf(fee)
                , EManufacturingAccountEntityType.MARKET_DEVOTION_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
        return result;
    }


    @RequestMapping("/currentLineState")
    @ResponseBody
    public Result currentLineState(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String lineId = request.getParameter("lineId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(lineId);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("line", produceLine);
        return result;
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
        result.addAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
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
        result.addAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping("/reBuildProduceLine")
    @ResponseBody
    public Result reBuildProduceLine(HttpServletRequest request, Model model) throws Exception {
        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String lineId = request.getParameter("lineId");
        String produceType = request.getParameter("produceType");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);

        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(lineId);

        Integer transferCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getTransferCycle();
        produceLine.setStatus(ProduceLine.Status.REBUILDING.name());
        produceLine.setLineBuildNeedCycle(transferCycle);
        produceLine.setProduceType(produceType);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        Integer companyCash = memoryCompany.getCompanyCash();
        newReport.setCompanyCash(companyCash);
        result.addAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping(value = "/produce")
    @ResponseBody
    public Result produce(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String produceLineId = request.getParameter("produceLineId");
        String produceType = request.getParameter("produceType");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);
        Company company = memoryCompany.getCompany();

        for (Product product : memoryCompany.getProductMap().values()) {
            if (product.getType().equals(produceType)) {
                if (!product.getStatus().equals(Product.Status.DEVELOPED.name())) {
                    Result result = new Result();
                    result.setStatus(Result.FAILED);
                    result.setMessage("该产品未被研发");
                    return result;
                }
            }
        }


        ProduceLine produceLine = memoryCompany.getProduceLineMap().get(produceLineId);
        if (ProduceLine.Type.HALF_AUTOMATIC.name().equals(produceLine.getProduceLineType())
                || ProduceLine.Type.AUTOMATIC.name().equals(produceLine.getProduceLineType())) {
            produceType = produceLine.getProduceType();
        }

        Material R1 = memoryCompany.getMaterialByType(Material.Type.R1.name());
        Material R2 = memoryCompany.getMaterialByType(Material.Type.R2.name());
        Material R3 = memoryCompany.getMaterialByType(Material.Type.R3.name());
        Material R4 = memoryCompany.getMaterialByType(Material.Type.R4.name());
        Integer R1Amount = R1.getAmount();
        Integer R2Amount = R2.getAmount();
        Integer R3Amount = R3.getAmount();
        Integer R4Amount = R4.getAmount();

        boolean isMaterialEnough = true;
        Product.Type productType = Product.Type.valueOf(produceType);
        switch (productType) {
            case P1:
                if (R1Amount >= 1) {
                    R1Amount = R1Amount - 1;
                } else {
                    isMaterialEnough = false;
                }
                break;
            case P2:
                if (R1Amount >= 1 && R2Amount >= 1) {
                    R1Amount = R1Amount - 1;
                    R2Amount = R2Amount - 1;
                } else {
                    isMaterialEnough = false;
                }
                break;
            case P3:
                if (R2Amount >= 2 && R3Amount >= 1) {
                    R2Amount = R2Amount - 2;
                    R3Amount = R3Amount - 1;
                } else {
                    isMaterialEnough = false;
                }
                break;
            case P4:
                if (R2Amount >= 1 && R3Amount >= 1 && R4Amount >= 2) {
                    R2Amount = R2Amount - 1;
                    R3Amount = R3Amount - 1;
                    R4Amount = R4Amount - 1;
                } else {
                    isMaterialEnough = false;
                }
                break;
            default:
                throw new NoSuchElementException(productType.name());
        }

        if (isMaterialEnough) {
            R1.setAmount(R1Amount);
            R2.setAmount(R2Amount);
            R3.setAmount(R3Amount);
            R4.setAmount(R4Amount);
        } else {
            Result result = new Result();
            result.setStatus(Result.FAILED);
            result.setMessage("原料不足");
            return result;
        }

        produceLine.setProduceType(produceType);
        produceLine.setStatus(ProduceLine.Status.PRODUCING.name());
        Integer produceCycle = ProduceLine.Type.valueOf(produceLine.getProduceLineType()).getProduceCycle();
        produceLine.setProduceNeedCycle(produceCycle);

        String fee = "1";
        Account account = accountManager.packageAccount(fee, EManufacturingAccountEntityType.PRODUCE_FEE.name(), EManufacturingAccountEntityType.COMPANY_CASH.name(), company);
        memoryCompany.addAccount(account);
        Account account2 = accountManager.packageAccount(fee, EManufacturingAccountEntityType.FLOATING_CAPITAL_PRODUCT.name(), EManufacturingAccountEntityType.FLOATING_CAPITAL_MATERIAL.name(), company);
        memoryCompany.addAccount(account2);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("line", produceLine);
        NewReport newReport = new NewReport();
        newReport.setR1Amount(R1Amount);
        newReport.setR2Amount(R2Amount);
        newReport.setR3Amount(R3Amount);
        newReport.setR4Amount(R4Amount);
        Integer companyCash = memoryCompany.getCompanyCash();
        newReport.setCompanyCash(companyCash);
        result.addAttribute("newReport", newReport);

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
        result.addAttribute("material", material);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
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
        result.addAttribute("product", product);
        NewReport newReport = new NewReport();
        newReport.setCompanyCash(memoryCompany.getCompanyCash());
        result.addAttribute("newReport", newReport);
        return result;
    }

    @RequestMapping("/loan")
    @ResponseBody
    public Result loan(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");
        String choiceId = request.getParameter("choiceId");
        String loanMoney = request.getParameter("value");
        String type = request.getParameter("type");

        ManufacturingMemoryCampaign memoryCampaign = (ManufacturingMemoryCampaign) CampaignServer.getMemoryCampaign(campaignId);
        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) memoryCampaign.getMemoryCompany(companyId);
        Campaign campaign = memoryCampaign.getCampaign();
        Company company = memoryCompany.getCompany();

        Integer ratio = 0;
        switch (EManufacturingAccountEntityType.valueOf(type)) {
            case LOAN_LONG_TERM:
            case LOAN_SHORT_TERM: ratio = 2;  break;
            case LOAN_USURIOUS: ratio = 3; break;
            default: throw new NoSuchElementException();
        }

        Integer companyCash = memoryCompany.getCompanyCash();
        Integer allLoanMoney = memoryCompany.getAllLoan();
        Integer floatingCapital = memoryCompany.getFloatingCapital();
        Integer maxEnableLoanMoney = (companyCash + floatingCapital - allLoanMoney) * ratio;

        if (Integer.valueOf(loanMoney) > maxEnableLoanMoney) {
            Result result = new Result();
            result.setStatus(Result.FAILED);
            result.setMessage("贷款失败，最大可贷金额：" + maxEnableLoanMoney + "M");
            return result;
        } else {
            Loan loan = new Loan();
            loan.setId(GenerateUtil.generate());
            loan.setSerial(autoSerialManager.nextSerial(EManufacturingSerialGroup.MANUFACTURING_PART.name()));
            loan.setDept(EManufacturingDept.FINANCE.name());
            loan.setStatus(Loan.Status.NORMAL.name());
            loan.setCampaign(campaign);
            loan.setCompany(company);
            loan.setMoney(Integer.valueOf(loanMoney));
            Loan.Type loanType = Loan.Type.valueOf(type);
            loan.setNeedRepayCycle(loanType.getCycle());
            loan.setType(loanType.name());
            memoryCompany.getLoanMap().put(loan.getId(), loan);

            String loanAccountName = EManufacturingAccountEntityType.valueOf(loanType.name()).name();
            Account account = accountManager.packageAccount(loanMoney, EManufacturingAccountEntityType.COMPANY_CASH.name(), loanAccountName, company);
            memoryCompany.addAccount(account);

            Result result = new Result();
            result.setStatus(Result.SUCCESS);
            NewReport newReport = new NewReport();
            newReport.setCompanyCash(memoryCompany.getCompanyCash());
            switch (loanType) {
                case LOAN_LONG_TERM:
                    newReport.setLongTermLoan(memoryCompany.getLongTermLoan());
                    break;
                case LOAN_SHORT_TERM:
                    newReport.setShortTermLoan(memoryCompany.getShortTermLoan());
                    break;
                case LOAN_USURIOUS:
                    newReport.setUsuriousLoan(memoryCompany.getUsuriousLoan());
                    break;
                default:
                    throw new NoSuchElementException(loanType.name());
            }
            result.addAttribute("newReport", newReport);

            return result;
        }
    }



    @RequestMapping("/loanList")
    @ResponseBody
    public Result loanList(HttpServletRequest request, Model model) throws Exception {

        String campaignId = request.getParameter("campaignId");
        String companyId = request.getParameter("companyId");

        ManufacturingMemoryCompany memoryCompany = (ManufacturingMemoryCompany) CampaignServer.getMemoryCampaign(campaignId).getMemoryCompany(companyId);

        Result result = new Result();
        result.setStatus(Result.SUCCESS);
        result.addAttribute("loanList", memoryCompany.getLoanMap().values());
        return result;
    }

}
