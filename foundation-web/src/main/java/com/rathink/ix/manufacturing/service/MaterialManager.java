package com.rathink.ix.manufacturing.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ix.manufacturing.model.Material;

/**
 * Created by Hean on 2015/10/11.
 */
public interface MaterialManager {
    Material getMateral(Company company, String type);

    Integer getMateralAmount(Company company, String type);
}
