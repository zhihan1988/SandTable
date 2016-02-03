package com.rathink.iix.manufacturing.service;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.manufacturing.model.Market;
import com.rathink.ix.manufacturing.model.Material;
import com.rathink.ix.manufacturing.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hean on 2016/2/2.
 */
@Service
public class ManufacturingPartService {

    @Autowired
    private BaseManager baseManager;


    public IndustryResourceChoice[][] getMarketChoiceArray(List<Market> marketList, List<Product> productList, Set<IndustryResourceChoice> marketChoiceList) {

        Map<String, IndustryResourceChoice> resourceChoiceMap = new HashMap<>();
        for (IndustryResourceChoice marketChoice : marketChoiceList) {
            resourceChoiceMap.put(marketChoice.getType(), marketChoice);
        }

        marketList.sort(((o1, o2) -> Market.Type.valueOf(o1.getType()).ordinal() - Market.Type.valueOf(o2.getType()).ordinal()));
        productList.sort((o1, o2) -> Product.Type.valueOf(o1.getType()).ordinal() - Product.Type.valueOf(o2.getType()).ordinal());

        IndustryResourceChoice[][] choiceArray = new IndustryResourceChoice[productList.size()][marketList.size()];
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            for (int j = 0; j < marketList.size(); j++) {
                Market market = marketList.get(j);
                if (market.getDevotionNeedCycle() == 0 && product.getDevelopNeedCycle() == 0) {
                    choiceArray[i][j] = resourceChoiceMap.get(market.getType() +"_"+ product.getType());
                } else {
                    choiceArray[i][j] = null;
                }
            }
        }

        return choiceArray;
    }

    public Material getMateral(Company company, String type) {
        String hql = "from Material where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Material material = (Material) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return material;
    }

    public Integer getMateralAmount(Company company, String type) {
        Integer amount = 0;
        String hql = "from Material where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Material material = (Material) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        if (material != null) {
            amount = material.getAmount();
        }
        return amount;
    }

    public Product getProduct(Company company, String type) {
        String hql = "from Product where company.id=:companyId and type = :type";
        LinkedHashMap<String, Object> queryParamMap = new LinkedHashMap<>();
        queryParamMap.put("companyId", company.getId());
        queryParamMap.put("type", type);
        Product product = (Product) baseManager.getUniqueObjectByConditions(hql, queryParamMap);
        return product;
    }
}
