package com.rathink.ix.manufacturing.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.manufacturing.model.Product;

/**
 * Created by Hean on 2015/10/15.
 */
public interface ProductManager {

    Product getProduct(Company company, String type);
}
