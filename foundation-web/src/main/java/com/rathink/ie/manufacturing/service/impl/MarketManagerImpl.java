package com.rathink.ie.manufacturing.service.impl;

import com.ming800.core.base.service.BaseManager;
import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.manufacturing.model.Market;
import com.rathink.ie.manufacturing.model.Product;
import com.rathink.ie.manufacturing.service.MarketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hean on 2015/10/15.
 */
@Service
public class MarketManagerImpl implements MarketManager {
    @Autowired
    private BaseManager baseManager;

    @Override
    public IndustryResourceChoice[][] getMarketChoiceArray(List<Market> marketList, List<Product> productList, Set<IndustryResourceChoice> marketChoiceList) {

        Map<String, IndustryResourceChoice> resourceChoiceMap = new HashMap<>();
        for (IndustryResourceChoice marketChoice : marketChoiceList) {
            resourceChoiceMap.put(marketChoice.getType(), marketChoice);
        }

        marketList.sort(((o1, o2) -> Market.Type.valueOf(o1.getType()).ordinal() - Market.Type.valueOf(o2.getType()).ordinal()));
        productList.sort((o1, o2) -> Product.Type.valueOf(o1.getType()).ordinal() - Product.Type.valueOf(o2.getType()).ordinal());

        IndustryResourceChoice[][] choiceArray = new IndustryResourceChoice[marketList.size()][productList.size()];
        for (int i = 0; i < marketList.size(); i++) {
            Market market = marketList.get(i);
            for (int j = 0; j < productList.size(); j++) {
                Product product = productList.get(j);
                if (market.getDevotionNeedCycle() == 0 && product.getDevelopNeedCycle() == 0) {
                    choiceArray[i][j] = resourceChoiceMap.get(market.getType() +"_"+ product.getType());
                } else {
                    choiceArray[i][j] = null;
                }
            }
        }

        return choiceArray;
    }
}
