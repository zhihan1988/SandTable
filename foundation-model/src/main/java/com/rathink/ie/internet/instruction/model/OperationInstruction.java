package com.rathink.ie.internet.instruction.model;

import com.rathink.ie.ibase.work.model.CompanyInstruction;
import com.rathink.ie.internet.choice.model.OperationChoice;

import javax.persistence.*;

/**
 * Created by Hean on 2015/8/28.
 */
@Entity
@Table(name = "operation_instruction")
public class OperationInstruction extends CompanyInstruction {
    private OperationChoice operationChoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_choice_id")
    public OperationChoice getOperationChoice() {
        return operationChoice;
    }

    public void setOperationChoice(OperationChoice operationChoice) {
        this.operationChoice = operationChoice;
    }
}
