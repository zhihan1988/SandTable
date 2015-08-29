package com.rathink.ie.internet.choice.model;

import com.rathink.ie.ibase.work.model.CompanyChoice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by Hean on 2015/8/24.
 */
@Entity
@Table(name = "product_study")
public class ProductStudy extends CompanyChoice {
    private String grade;//定位

    @Column(name = "grade")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    private enum Grade{
        low("1","低端"), hign("2", "高端"),;
        private String value;
        private String label;

        private Grade(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    @Transient
    public String getGradeLabel(){
        String label = "";
        for (Grade grade: Grade.values()) {
            if (grade.getValue().equals(getGrade())) {
                label = grade.getLabel();
            }
        }
        return label;
    }
}
