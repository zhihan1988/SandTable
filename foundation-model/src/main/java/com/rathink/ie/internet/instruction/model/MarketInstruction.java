package com.rathink.ie.internet.instruction.model;

import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.choice.model.MarketActivityChoice;

import javax.persistence.*;

/**
 * Created by Hean on 2015/8/27.
 */
@Entity
@Table(name = "market_instruction")
public class MarketInstruction extends CompanyInstruction {
    private MarketActivityChoice marketActivityChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_activity_choice_id")
    public MarketActivityChoice getMarketActivityChoice() {
        return marketActivityChoice;
    }

    public void setMarketActivityChoice(MarketActivityChoice marketActivityChoice) {
        this.marketActivityChoice = marketActivityChoice;
    }
}
