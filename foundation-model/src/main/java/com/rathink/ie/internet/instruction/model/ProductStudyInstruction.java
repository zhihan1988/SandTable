package com.rathink.ie.internet.instruction.model;

import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.choice.model.ProductStudy;

import javax.persistence.*;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "product_study_instruction")
public class ProductStudyInstruction extends CompanyInstruction {
    private ProductStudy productStudy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_study_id")
    public ProductStudy getProductStudy() {
        return productStudy;
    }

    public void setProductStudy(ProductStudy productStudy) {
        this.productStudy = productStudy;
    }
}
