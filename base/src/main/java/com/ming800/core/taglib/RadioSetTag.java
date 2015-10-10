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
            html+="<button  type=\"button\" class=\"am-btn am-btn-primary am-radius\" onclick=\""+onclick+"\">"+radio+"</button>";
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
}
