package com.rathink.ie.manufacturing.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.manufacturing.model.Product;

/**
 * Created by Hean on 2015/10/15.
 */
public interface ProductManager {

    Product getProduct(Company company, String type);
}
