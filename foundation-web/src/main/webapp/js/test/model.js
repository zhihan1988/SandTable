/**
 * Created by pgwt on 12/27/15.
 */
//与后台对应的model类

/**
 * 测试使用的jsModel类
 * @param id
 * @param serial
 * @param dept
 * @param campaign
 * @param company
 * @param name
 * @param status
 * @constructor
 */
var CompanyPart = function( id, serial, dept, campaign, company, name, status,templateMap) {
    this.id = id;
    this.serial = serial;
    this.dept = dept;
    this.campaign = campaign;
    this.company = company;
    this.name = name;
    this.status = status;
    this.templateMap = templateMap;

    this.getObjectList = function (jsonObjectList) {
        var objectList = new Array();
        for (var i = 0; i < jsonObjectList.length; i++) {
            var jsonObject = jsonObjectList[i];
            var object = new CompanyPart(jsonObject.id,jsonObject.serial,jsonObject.dept,jsonObject.campaign,jsonObject.company,jsonObject.name,jsonObject.status);
            objectList.push(object);
        }
        return objectList;
    }
}



