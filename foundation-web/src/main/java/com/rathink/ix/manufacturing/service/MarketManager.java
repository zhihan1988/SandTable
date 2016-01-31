package com.rathink.ix.manufacturing.service;

import com.rathink.ix.ibase.work.model.IndustryResourceChoice;
import com.rathink.ix.manufacturing.model.Market;
import com.rathink.ix.manufacturing.model.Product;

import java.util.List;
import java.util.Set;

/**
 * Created by Hean on 2015/10/17.
 */
public interface MarketManager {
    IndustryResourceChoice[][] getMarketChoiceArray(List<Market> marketList, List<Product> productList, Set<IndustryResourceChoice> marketChoiceList);
}
