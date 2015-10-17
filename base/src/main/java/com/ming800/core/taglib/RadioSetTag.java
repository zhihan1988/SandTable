package com.ming800.core.taglib;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Created by Administrator on 2015/10/10 0010.
 */
public class RadioSetTag extends TagSupport {

    private String valueSet;
    private String[] values;
    private String onclick;
    private String name;
    private String status;
    private String checkedValue;

    public int doStartTag() {
        return TagSupport.EVAL_BODY_INCLUDE;//子标签将查询参数拼接上(qm,conditions)
    }


    public int doEndTag() {

        values = valueSet.split(",");
        String html = "";
        html+="<div class=\"am-btn-toolbar\">\n" +
                "    <div class=\"am-btn-group am-btn-group-xs\">";
        //生成html
        for (String radio : values){
            if (checkedValue!=null && radio.split(":")[1].equals(checkedValue)){

                html+="<button key=\""+radio.split(":")[0]+"\" value=\""+radio.split(":")[1]+"\" status=\"active\" name=\""+name+"\" type=\"button\" class=\"am-btn am-btn-primary am-radius am-active\" onclick=\"buttonGroupAction(this,"+onclick+")\">"+radio.split(":")[1]+"</button>";
            }else {

                html+="<button key=\""+radio.split(":")[0]+"\" value=\""+radio.split(":")[1]+"\" status=\"unActive\" name=\""+name+"\" type=\"button\" class=\"am-btn am-btn-primary am-radius\" onclick=\"buttonGroupAction(this,"+onclick+")\">"+radio.split(":")[1]+"</button>";
            }
        }
        html+= "</div></div>";
        JspWriter out = this.pageContext.getOut();
        try {
            out.print(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TagSupport.EVAL_PAGE;
    }

    public String getValueSet() {
        return valueSet;
    }

    public void setValueSet(String valueSet) {
        this.valueSet = valueSet;
    }


    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckedValue() {
        return checkedValue;
    }

    public void setCheckedValue(String checkedValue) {
        this.checkedValue = checkedValue;
    }
}
