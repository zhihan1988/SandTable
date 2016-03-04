/**
 * Created by pgwt on 12/27/15.
 */
//初始化页面的时候需要传递的参数 1.模版列表 2.需要使用的Model类列表 3.服务端返回的数据（className:dataList）
//首先配置一下当前的页面需要哪些model类
//配置了之后在初始化的时候就可以知道需要初始化哪些model类，主要是对应后端传递过来的参数对应哪些model
//在页面中需要配置clasNameList（当前页面需要使用的model类列表，字符串的形式设置）
//然后调用一下初始化的函数传递参数
//发送指令改变数据


//全局变量缓存 数据格式｛key：［value1，value2，value3］｝
var modelMap = new Object();
var templateMapCatch;

/**
 *初始化model类
 * @param classNameList 当前页面需要用到的model类名称
 * @param jsonObjectMap 服务端传过来的json数据 用来跟前端数据同步
 * @returns {Object}
 */
function initModel(classNameList, jsonObjectMap) {
    //var modelMap = new Object();
    for (var i = 0; i < classNameList.length; i++) {
        var jsonObjectList = jsonObjectMap[classNameList[i]];
        modelMap[classNameList[i]] = eval("new " + classNameList[i] + "().getObjectList(jsonObjectList)");
    }
    return modelMap;
}


function initPage(templateMap, jsonObjectMap, classNameList) {
    //所有Map的key都是model类的类名
    initModel(classNameList, jsonObjectMap)
    templateMapCatch = templateMap;
    var resultMap = new Object();
    for (var i = 0; i < classNameList.length; i++) {
        if (typeof templateMap[classNameList[i]] != "undefined") {
            var templator = doT.template(templateMap[classNameList[i]]);
            var html = templator(modelMap[classNameList[i]]);
            resultMap[classNameList[i]] = html;
        }
    }
    return resultMap;
}

function generateHtml(template,data){
    var templator = doT.template(template);
    var html = templator(data);
    return html;
}


//同步数据需要和服务端交互获得最新的数据或者把最新的数据传给服务端，是否需要一个状态来判断当前的数据在客户端停留的时间，如果超过一定时间就重新获取数据
function updateModel(className, jsonObjectList, isUpdatePage) {
    modelMap[className] = eval("new " + className + "().getObjectList(jsonObjectList)")
    if (isUpdatePage) {

    }
}





