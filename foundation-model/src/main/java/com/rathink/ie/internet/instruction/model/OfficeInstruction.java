package com.rathink.ie.internet.instruction.model;

import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.choice.model.OfficeChoice;

import javax.persistence.*;

@Entity
@Table(name = "office_instruction")
public class OfficeInstruction extends CompanyInstruction {
    private OfficeChoice officeChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_choice_id")
    public OfficeChoice getOfficeChoice() {
        return officeChoice;
    }

    public void setOfficeChoice(OfficeChoice officeChoice) {
        this.officeChoice = officeChoice;
    }
}
