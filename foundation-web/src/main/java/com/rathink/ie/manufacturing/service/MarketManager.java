package com.rathink.ie.manufacturing.service;

import com.rathink.ie.ibase.work.model.IndustryResourceChoice;
import com.rathink.ie.manufacturing.model.Market;
import com.rathink.ie.manufacturing.model.Product;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/10/17.
 */
public interface MarketManager {
    IndustryResourceChoice[][] getMarketChoiceArray(List<Market> marketList, List<Product> productList, Set<IndustryResourceChoice> marketChoiceList);
}
