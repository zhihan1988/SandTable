package com.rathink.ie.manufacturing.service;

import com.rathink.ie.foundation.team.model.Company;
import com.rathink.ie.manufacturing.model.Material;

/**
 * Created by Hean on 2015/10/11.
 */
public interface MaterialManager {
    Material getMateral(Company company, String type);

    Integer getMateralAmount(Company company, String type);
}
