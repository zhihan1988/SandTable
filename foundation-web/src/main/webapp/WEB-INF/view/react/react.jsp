
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: pgwt
  Date: 10/25/15
  Time: 3:44 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta name="viewport"
        content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
  <title>Amaze UI React</title>
  <!-- No Baidu Siteapp-->
  <link rel="stylesheet" href="http://cdn.amazeui.org/amazeui/2.4.0/css/amazeui.min.css"/>
  <script src="<c:url value='/js/test/jquery-1.11.1.min.js'/>"></script>
  <script src="/js/react/build/react.js"></script>
  <script src="/js/react/build/react-dom.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/babel-core/5.8.23/browser.min.js"></script>
  <script type="text/javascript" src="/js/pubsubjs/pubsub.js"></script>


<style type="text/css">
        .navbar-button-selected{
            /*background: orange;*/
            background: orangered;
        }
        .panel {
            display: none;
        }
        .introduce {
            color: #999999;
            font-size:9px;
            margin-bottom: 10px;
        }
        .introduce p {
            margin: 0;
            padding: 0;
        }
        .main-panel .am-panel-group .am-panel{
            margin-bottom: 20px;
        }
        .main-panel .am-panel-group .am-panel .am-panel-hd{
            background: #EEE;
        }
        .door {
            position: relative;
            padding-left: 10px;
        }
        .door-enter {
            position: absolute;
            top: 0px;
            right: 10px;
            color:#0e90d2;
        }
        .repertory ul li{
            list-style-type:none;
        }

        .am-nav-tabs>li>a {
            padding:.4em 0.5em
        }

        .line-panel {
            border-bottom: 1px solid #DDDDDD;
            padding: 20px;
        }
        .line-innerDiv1 {
            padding: 10px 0;
        }
        .line-innerDiv2 {
            padding: 10px 0;
        }
        .line-innerDiv3 {
            padding: 10px 0;
        }
        .button-padding {
            margin: 5px;
        }
        .line-important {
            font-weight: bold;
        }
        .am-panel-hd {
            font-weight: bold;
        }
        .am-table th{
            font-weight: normal;
        }
        .homePage_ul {
            margin: 0;
            padding: 0;
        }
        .homePage_ul li{
            list-style-type:none;
        }
        #marketOrderChoicePanel_message{
            color: orangered;
        }
    </style>

</head>
<body>

<script type="text/javascript">


/*
  为了组件之间的交互，所有事件的触发都适用pubsub的方式来实现，对于组件来说不用管使用哪种方式，会在组件内部定义一个onClick事件，然后在函数体中调用外部的业务路基相关的函数，这个时候，在触发事件的时候就需要发布一个事件，告诉pubsub事件容器，当前有一个新的事件发布，这个时候之前订阅了这个事件的对象就会按照之前的定义从新渲染组件，所有订阅事件的操作是在组件内部完成，

  在 componentDidMount 事件中，如果组件挂载（mounted）完成，再订阅事件；当组件卸载（unmounted）的时候，在 componentWillUnmount 事件中取消事件的订阅。

  没给组件当中都有对应的事件点击的函数，如果有一个需要定义事件的地方就一个函数，有两个需要定义事件的地方就两个函数，时根据每个组件的逻辑来定的


  如果react的页面东西太多，那么就需要把react的渲染工作挪到后台去做，东西太多 browser会timeout
*/


</script>

<div id="content"></div>
<div id="content2"></div>

<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－test.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->

<script type="text/babel">

  //下拉菜单的通用组件 (通用组件)
  var MySelect = React.createClass({

    render: function(){

      var createOption = function(option){
          return <option value={option.value}>{option.label}</option> ;
      }
      return (<select id={this.props.id} value={this.props.active} name={this.props.name} onChange={this.props.selectOnChange}>{this.props.options.map(createOption)}</select>);
    }
  });

  //单个生产线组件
  var ProductLineItem = React.createClass({


    getInitialState : function(){
      return {data:this.props.data};
    },

    chooseLineType : function(e){ //选择生产线的类型
      var value = e.target.value;
      var dataTemp = chooseType(this.props.data.id,value);
      //发布一个选择生产线类型的事件
      PubSub.publish('onLineTypeChoose'+this.props.data.id, dataTemp.line);
    },

      chooseProductType : function(e){ //选择生产线的类型
          var value = e.target.value;
          var dataTemp = chooseProduceType(this.props.data.id,value);
          //发布一个选择生产线类型的事件
          PubSub.publish('onProduceTypeChoose'+this.props.data.id, dataTemp.line);
      },

    submitProduce: function(){  //提交生产商品的按钮
      //@TODO 状态改为 PRODUCING
      submitNewProduce(this.props.data.id);
    },

    submitLine:function(){    //提交建造生产线的按钮
        //@TODO 状态改为 BUILDING
      var dataTemp = submitLineBuild(this.props.data.id,this.props.data.status);
      //发布一个选择生产线类型的事件
      PubSub.publish('onSubmitLine'+this.props.data.id, dataTemp.line);
    },



    componentDidMount: function () {
      //onLineTypeChoose 的token
      //用来实现组件之间的通信
      this.onLineTypeChoose_token = PubSub.subscribe('onLineTypeChoose'+this.props.data.id, function (topic, lineData) {
        //收到所订阅的事件之后重新渲染当前组件
        this.setState({
          data: lineData
        });
      }.bind(this));

        this.onProduceTypeChoose_token = PubSub.subscribe('onProduceTypeChoose'+this.props.data.id, function (topic, lineData) {
            //收到所订阅的事件之后重新渲染当前组件
            this.setState({
                data: lineData
            });
        }.bind(this));

      this.onSubmitLine_token = PubSub.subscribe('onSubmitLine'+this.props.data.id, function (topic, lineData) {
        //收到所订阅的事件之后重新渲染当前组件
          console.log("触发事件成功");
          console.log(lineData);
        this.setState({
          data: lineData
        });
      }.bind(this));

      this.onSubmitProduce_token = PubSub.subscribe('onSubmitProduce'+this.props.data.id, function (topic, lineData) {
        //收到所订阅的事件之后重新渲染当前组件
        this.setState({
          data: lineData
        });
      }.bind(this));

      //@TODO 其他事件的token

    },
    componentWillUnmount: function () {
      PubSub.unsubscribe(this.onLineTypeChoose_token);
    },
    //生产类型，UNBUILD 手工和柔性那么就是所有许可产品，如果是半自动和自动就是指定类型的产品，
    //FREE , 如果是手工和柔性生产线，那么就在生产商品的时候手动选择需要生产的商品（下拉） 如果是自动和半自动，就只显示指定的产品 然后是生产按钮

    //FREE
    //@TODO 需要修改类型的值，可能有变动 目前只判断代表性的两个 ，之后再 把半自动加上
    render: function(){

      var text = this.state.data;
      var lineTypeOptions = [{"value":"","label":"请选择"},{"value":"AUTOMATIC","label":"全自动"},{"value":"MANUAL","label":"手工"}];
      var produceTypeOptions = [{"value":"P1","label":"P1"},{"value":"P2","label":"P2"},{"value":"P3","label":"P3"},{"value":"P4","label":"P4"}];


      return (
      <div className="am-panel-bd line-panel">
        <div id="line_iistm2qf2sjj8uec">
          <div className="line-innerDiv1 line-important">{text.name}：


          {(()=>{
            if (text.status=='UN_BUILD') {
              return "未建造";
            }else if (text.status=='BUILDING'|| text.status=='BUILT'){
              return '建造中';
            }else if (text.status=="FREE") {
              return "空闲"
            }else if (text.status=="PRODUCING") {
              return "生产中"
            };

          })()}



          <span></span></div>
          <div className="line-innerDiv2">

              {(() =>
                {
                  if (text.status=='UN_BUILD') {
                    return <span className="line-innerSpan2">生产线类型：<MySelect active={text.produceLineType} options={lineTypeOptions} selectOnChange={this.chooseLineType}/></span>;
                  }else if(text.status=='BUILDING'){
                      return <span className="line-innerSpan2">生产线类型：{text.produceLineType=="AUTOMATIC"?"全自动":"手工"}</span>;
                  }else if (text.status=='BUILT'){
                      return <span className="line-innerSpan2">剩余生产周期:{text.lineBuildNeedCycle}</span>;
                  }else if(text.status=='PRODUCING'){
                      return <span className="line-innerSpan2">剩余生产周期:{text.produceNeedCycle}</span>;
                  }else{
                    return <span className="line-innerSpan2">生产线类型:{text.produceLineType=="AUTOMATIC"?"全自动":"手工"}</span>; //..当前生产线的类型
                  };
                }
              )()}



              <span style={{margin: "10px"}}></span><span className="line-innerSpan3" id="produceTypeSpan_iistm2qf2sjj8uec">生产类型：

              {(() =>
                {
                  if (text.status == 'UN_BUILD') {

                    //@TODO 需要修改类型的值，可能有变动
                    if (text.produceLineType=='AUTOMATIC') {
                      return <MySelect  options={produceTypeOptions} selectOnChange={this.chooseProductType}/> ;
                    }else{
                      return "所有许可产品";
                    };
                  }else if (text.status == 'FREE'){

                    if (text.produceLineType == 'AUTOMATIC') {
                      return text.produceType;
                    }else{
                      return <MySelect  options={produceTypeOptions} selectOnChange={this.chooseProductType}/>;
                    };

                  }else if (text.status == 'BUILDING'||text.status=='BUILT') {
                    //@TODO 需要修改类型的值，可能有变动
                    if (text.produceLineType=='AUTOMATIC') {
                      return text.produceType; ;
                    }else{
                      return "所有许可产品";
                    };
                  }else if (text.status == "PRODUCING") {
                    return text.produceType;
                  };
                }
              )()}
              </span>
          </div>
          <div id="lineButtonDiv_iistm2qf2sjj8uec">


            {(()=>{
              if (text.status=='UN_BUILD') {
                return <button className="am-btn am-btn-secondary am-btn-once" onClick={this.submitLine}>投资新生产线</button>;
              }else if (text.status=='BUILDING') {
                return "";
              }else if (text.status == 'BUILT') {
                return <button className="am-btn am-btn-secondary am-btn-once" onClick={this.submitLine}>再投生产线</button>;
              }else if (text.status == 'PRODUCING') {
                return "";
              }else if (text.status == 'FREE') {
                return <button className="am-btn am-btn-secondary am-btn-once" onClick={this.submitProduce}>生产</button>;
              };;
            })()}
          </div>
        </div>
      </div>
      );
    }});

  //尾部导航的组件
  var MyBottomNavBar = React.createClass({

    getInitialState: function(){
      return {active:"button-1"};
    },

    buttonHandleClick: function(e){
      var targetId = $(e.target).attr("name");
      $(".panel").hide();
      $("#"+targetId).show();
      var buttonId = $("a[name="+targetId+"]").attr("id");
      this.setState({active:buttonId});
    },

    render: function(){
      return (
      <div data-am-widget="navbar" className="am-navbar am-cf am-navbar-default " id="">
        <ul className="am-navbar-nav am-cf am-avg-sm-4">
            <MyBottomNavBar.ButtonItem {...lis[0]} handleClick={this.buttonHandleClick} active={this.state.active}/>
            <MyBottomNavBar.ButtonItem {...lis[1]} handleClick={this.buttonHandleClick} active={this.state.active}/>
            <MyBottomNavBar.ButtonItem {...lis[2]} handleClick={this.buttonHandleClick} active={this.state.active}/>
            <MyBottomNavBar.ButtonItem {...lis[3]} handleClick={this.buttonHandleClick} active={this.state.active}/>
        </ul>
      </div>);

    }});
  //尾部导航的按钮组件
  MyBottomNavBar.ButtonItem = React.createClass({
    //按钮需要定义状态，是否选中，在初始化的时候通过lis中获得
    getInitialState: function(){
      return {active:this.props.active};
    },

    render: function(){
      console.log(this.state.active+"=>"+this.props.id);
      return (
          <li key={this.props.id} name={this.props.targetId}>
            <a id={this.props.id} href={this.props.href} onClick={this.props.handleClick} name={this.props.targetId}  className={this.props.active==this.props.id?"navbar-button-selected":""}>
              <span name={this.props.targetId} className={this.props.iconClassName}></span>
              <span name={this.props.targetId} className="am-navbar-label">{this.props.label}</span>
            </a>
          </li>
        )
    }});

  //尾部导航的内容区域组件
  var MyPanel = React.createClass({
      render : function(){
        return (
          <div id={this.props.id} className="panel">
            <div className="am-panel am-panel-default">
              {
                React.Children.map(this.props.children,function(child){
                  console.log(child);
                  console.log("123456");
                  return <div className="am-panel-bd">{child}</div>
                })
              }
            </div>
          </div>
          );
      }});

  //生产线列表组件（厂房）
  var WorkShopBox = React.createClass({

    //props: 1.name
    //state: 1.data 厂房当中的生产线列表数据

    getInitialState:function(){
      return {data:this.props.data};
    },




    render:function(){
    var createItem = function(item) {
      return <ProductLineItem key={item.id} data={item} />;
    };
    return (
              <div className="am-panel am-panel-default" id="produceLinesDiv">
                <div className="am-panel-hd am-panel-hd-module" >{this.props.name}</div>
                {this.state.data.map(createItem)}
              </div>
        );
    }});


  //主页的组件 @TODO 待拆分
  var MainPage = React.createClass({

    getInitialState:function(){
      return {data:this.props.data};
    },

    componentDidMount:function(){

    },

    componentWillUnmount:function(){

    },

    render:function(){
      return (
              <div className="am-panel am-panel-default">

                  <div className="am-panel-bd">
                      <h3>财务</h3>
                      <ul className="homePage_ul">
                          <li>公司现金：<span id="homePage_companyCash">-6</span>M</li>
                          <li>高利贷：<span id="homePage_usuriousLoan">0</span>M</li>
                          <li>短期贷款：<span id="homePage_shortTermLoan">0</span>M</li>
                          <li>长期贷款：<span id="homePage_longTermLoan">0</span>M</li>
                      </ul>
                  </div>

                  <div className="am-panel-bd">
                      <h3>市场</h3>

                          <a id="homePage_intoMarket" className="am-btn am-btn-warning" href="#">进入市场竞标</a>
                          <br/>
                          <br/>


                      未交付订单数量: <span id="unDeliveredOrderSize">0</span>
                  </div>

                  <div className="am-panel-bd">
                      <h3>生产</h3>

                      原料库存<br/>
                      R1:<span id="homePage_materialAmount_R1">0</span>
                      R2:<span id="homePage_materialAmount_R2">0</span>
                      R3:<span id="homePage_materialAmount_R3">0</span>
                      R4:<span id="homePage_materialAmount_R4">0</span>
                      <br/>
                      <br/>
                      产品库存<br/>
                      P1:<span id="homePage_materialAmount_P1">0</span>
                      P2:<span id="homePage_materialAmount_P2">0</span>
                      P3:<span id="homePage_materialAmount_P3">0</span>
                      P4:<span id="homePage_materialAmount_P4">0</span>
                  </div>

              </div>
        )
    }
  })


  //广告投放 @TODO 业务逻辑
  var AdvertisementBox = React.createClass({

    // props: 1.handleOnChange(下拉菜单选择事件) 2.products 商品列表 3.handleOnClick 完成投放按钮事件
    // state: 1.products 商品列表

    getInitialState:function(){
      return {products:this.props.products};
    },

    componentDidMount:function(){
        //绑定subpub事件
        //@TODO 1.订阅完成投放事件 2.订阅投放产品数量的事件（时间名称使用商品id区分）
    },

    componentWillUnmount:function(){
        //注销subpub事件
        //@TODO 1.注销完成投放事件 2.注销投放产品数量的事件（时间名称使用商品id区分）
    },

    render:function(){

      var handleOnChange = this.props.handleOnChange;

      var createTr = function(product){

          var options = new Array();
          var startOption = new Object();
          startOption["value"] = product.id+"#-1";
          startOption["label"] = "未投入";
          options.push(startOption);
          for (var i = 0; i < 8; i++) {
            var option = new Object();
            option["value"] = product.id+"#"+(i+1);
            option["label"] = i+1;
            options.push(option);
          }

          return (
            <tr>
                <td>{product.type}</td>
                <td>
                    <MySelect options={options} id={"marketFee_"+product.id} selectOnChange={handleOnChange}/>
                </td>
            </tr>
          )
      }

      return (
        <div className="am-panel am-panel-default" id="devotePanel">
              <div className="am-panel-hd">广告投放</div>
              <div className="am-panel-bd">
                  <div className="am-tabs" data-am-tabs="">
                      <ul className="am-tabs-nav am-nav am-nav-tabs">
                          <li className="am-active"><a href="#tab1">本地</a></li>
                      </ul>
                      <div className="am-tabs-bd" style={{"touch-action": "pan-y", "-webkit-user-select": "none", "-webkit-user-drag": "none", "-webkit-tap-highlight-color": "rgba(0, 0, 0, 0)"}}>

                              <div className="am-tab-panel am-fade am-in am-active" id="tab1">
                                  <table className="am-table">
                                      <tbody><tr>
                                          <th>产品类型</th>
                                          <th>投放金额(M)</th>
                                      </tr>
                                      {this.state.products.map(createTr)}
                                  </tbody></table>
                              </div>

                      </div>
                      <button id="finishDevotion" onClick={this.props.handleOnClick} type="button" className="am-btn am-btn-secondary am-btn-once">完成投放</button>
                  </div>
              </div>
          </div>

      );
    }


  });

  //@TODO 公司排名 未完成 业务逻辑
  var CompanyNumberBox = React.createClass({

    getInitialState:function(){
      return {products:this.props.products};
    },

    componentDidMount:function(){
        //绑定subpub事件
    },

    componentWillUnmount:function(){
        //注销subpub事件
    },

    render: function(){

        return (
          <div className="am-panel am-panel-default" id="companyOrderPanel">
                <div className="am-panel-hd">公司排名</div>
                <div class="am-panel-bd">
                    <table class="am-table">
                        <thead>
                        <tr>
                            <th>排名</th>
                            <th>公司(投入)</th>
                        </tr>
                        </thead>
                        <tbody id="companyOrderTbody">
                        </tbody>
                    </table>
                </div>
            </div>
        )

    }
  })

  //@TODO 选单 未完成 业务逻辑
  var ChooseOrderBox = React.createClass({

      getInitialState:function(){
        return {};
      },

      componentDidMount:function(){
          //绑定subpub事件
      },

      componentWillUnmount:function(){
          //注销subpub事件
      },

      render:function(){

          return (
            <div className="am-panel am-panel-default" id="marketOrderChoicePanel">
                <div className="am-panel-hd">选单</div>
                <div className="am-panel-bd">
                <h3><span id="marketOrderChoicePanel_message">正在选单</span></h3>
                <table className="am-table">
                    <thead>
                    <tr>
                        <th>订单</th>
                        <th>产品</th>
                        <th>数量/单价(M)/金额(M)/利润(M)/账期</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody id="marketOrderTbody">
                    </tbody>
                </table>
                <button id="confirmOrder" type="button" className="am-btn am-btn-secondary am-disabled am-btn-once" disabled="disabled">等待选单</button>
                </div>
            </div>
          )

      }

  })

  //@TODO 未交付订单 未完成 业务逻辑
  var UndeliveryOrderBox = React.createClass({

      getInitialState:function(){
        return {};
      },

      componentDidMount:function(){
          //绑定subpub事件
      },

      componentWillUnmount:function(){
          //注销subpub事件
      },

      render:function(){

          return (
            <div className="am-panel am-panel-default">
                <div className="am-panel-hd">未交付订单</div>
                <div className="am-panel-bd">
                    <table className="am-table" id="marketOrderListTable">
                        <thead>
                        <tr>
                            <th>订单</th>
                            <th>产品</th>
                            <th>数量/金额(M)/账期</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody id="marketOrderListTbody">

                        </tbody>
                    </table>
                </div>
            </div>
          )

      }

  })


  //@TODO 市场开拓 未完成 业务逻辑
  var MarketExploitation = React.createClass({

      getInitialState:function(){
        return {};
      },

      componentDidMount:function(){
          //绑定subpub事件
      },

      componentWillUnmount:function(){
          //注销subpub事件
      },

      render:function(){

          return (
            <div className="am-panel am-panel-default">
                <div className="am-panel-hd">市场开拓</div>
                <div className="am-panel-bd">
                    <table className="am-table">
                        <thead>
                        <tr>
                            <th>市场</th>
                            <th colspan="2">剩余开发周期</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>本地</td>
                                <td>0</td>
                                <td>
                                            已开发完成
                                </td>
                            </tr>
                            <tr>
                                <td>区域</td>
                                <td>1</td>
                                <td>
                                            第四季度可开发
                                </td>
                            </tr>
                            <tr>
                                <td>亚洲</td>
                                <td>3</td>
                                <td>
                                            第四季度可开发
                                </td>
                            </tr>
                            <tr>
                                <td>国内</td>
                                <td>2</td>
                                <td>
                                            第四季度可开发
                                </td>
                            </tr>
                            <tr>
                                <td>国际</td>
                                <td>4</td>
                                <td>
                                            第四季度可开发
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
          )

      }

  })

  //资料库组件
  var MaterialStorageBox = React.createClass({

    //props : 1.resource rList 2.handleOnChange 3.id（唯一标识）;
    //state : 1.resource

    getInitialState: function(){
      return {resource:this.props.resource};
    },

    componentDidMount: function () {
      //onLineTypeChoose 的token
      //用来实现组件之间的通信
      this.onSelectMaterialCount = PubSub.subscribe('onSelectMaterialCount'+this.props.id, function (topic, resourceData) {
        //收到所订阅的事件之后重新渲染当前组件
        this.setState({
          resource:resourceData
        });
      }.bind(this));

    },

    componentWillUnmount: function () {
      PubSub.unsubscribe(this.onSelectMaterialCount);
    },

    render : function(){

        var handleOnChange = this.props.handleOnChange;

        var createTr = function(resource){

        var rOption = new Array();
        for (var i = 0 ; i < 8 ; i++){
          var option = new Object();
          option["value"] = resource.id+"#"+(i+1);
          option["label"] = i+1;
          rOption.push(option);
        }

        return (
                <tr>
                    <td>{resource.type}</td>
                    <td><span id={"materialAmount_"+resource.type}>0</span></td>
                    <td>
                        <MySelect options={rOption} id={resource.id} selectOnChange={handleOnChange}/>
                    </td>
                </tr>
        )
      }

      return (
          <div className="am-panel am-panel-default">
                <div className="am-panel-hd am-panel-hd-module">原料库</div>
                <div className="am-panel-bd">
                    <table className="am-table">
                        <thead>
                        <tr>
                            <th>原料类型</th>
                            <th>库存</th>
                            <th>采购数量</th>
                        </tr>
                        </thead>
                        <tbody>
                          {this.state.resource.map(createTr)}
                        </tbody>
                    </table>
                </div>
            </div>
      )
    }
  })

  //产品库组建
  var ProductStorageBox = React.createClass({

    // props: 1.handleOnClick 2.product 3.id（产品的唯一标识）
    // state：1.product

    getInitialState: function(){
      return {product:this.props.product};
    },

    componentDidMount: function () {
      //onLineTypeChoose 的token
      //用来实现组件之间的通信
      this.exploitationProduct = PubSub.subscribe('exploitationProduct'+this.props.id, function (topic, productData) {
        //收到所订阅的事件之后重新渲染当前组件
        this.setState({
          product:productData
        });
      }.bind(this));

    },

    componentWillUnmount: function () {
      PubSub.unsubscribe(this.exploitationProduct);
    },


    render : function(){

      var handleOnChange = this.props.handleOnChange;

      var createTr = function(product){
          return (
                      <tr>
                          <td>{product.type}</td>
                          <td><span id={"productAmount_"+product.type}>{product.amount}</span></td>
                          <td>
                          {product.developNeedCycle}<button onClick={handleOnChange} id="devoteProduct_ik9rg70p24d0dh3w" type="button" className="am-btn am-btn-secondary am-btn-once">研发</button>
                          </td>
                      </tr>
            )
      }

      return (<div className="am-panel am-panel-default">
                  <div className="am-panel-hd am-panel-hd-module">产品库</div>
                  <div className="am-panel-bd">
                      <table className="am-table">
                          <thead>
                          <tr>
                              <th>产品类型</th>
                              <th>库存</th>
                              <th>剩余研发周期</th>
                              <th></th>
                          </tr>
                          </thead>
                          <tbody>
                          {this.state.product.map(createTr)}
                          </tbody>
                      </table>
                  </div>
              </div>)
    }

  })

// ----------------------------------------------------------------------


  //使用jquery ajax工具发送异步请求
  function ajaxRequest(url, param, success, error,requestType) {
      if(typeof requestType == "undefined"){

      }
      $.ajax({
          type: requestType,
          url: url,
          cache: false,
          dataType: "JSON",
          data: param,
          success: success,
          error: error,
      });
  }


  //选择生产线类型 这里时业务逻辑部分
  function chooseType(id,value){
    pageLineMap[id]["produceLineType"]=value;
    var result = new Object();
    result["line"]=pageLineMap[id];
    return result;}

  //选择产品类型
  function chooseProduceType(id,value){
      pageLineMap[id]["produceType"]=value;
      var result = new Object();
      result["line"]=pageLineMap[id];
      return result;}

  //投资新生产线
  function submitLineBuild(id,lineStatus){
    //获得value
    var param = new Object();
    var result = new Object();
    param["campaignId"] = pageCampaign.id;
    param["companyId"] = pageCompany.id;
    param["companyTermId"] = pageCompany.id;
    param["partId"] = pageLineMap[id]["id"];
    param["lineType"] = pageLineMap[id]["produceLineType"];
    param["produceType"] = pageLineMap[id]["produceType"];

      console.log(param);

    var url = "/manufacturing/buildProduceLine.do";
    if (lineStatus == "UN_BUILD") {
      url = "/manufacturing/buildProduceLine.do";
    }else{
      url = "/manufacturing/continueBuildProduceLine.do?"
    };

    var success = function(data){
        pageLineMap[id]= data.model.line;
        result["line"] = pageLineMap[id];
        console.log(data);
        var lineData = data.model.line;
        console.log(lineData);
        PubSub.publish("onSubmitLine"+lineData.id,lineData);
        //出发事件
    }

    ajaxRequest(url,param,success,function(){},"post");

    return result;
  }

  //生产商品
  function submitNewProduce(id){
    //获得value
    var param = new Object();
    var result = new Object();
    param["companyId"] = pageCompany.id;
    param["campaignId"] = pageCampaign.id;
    param["produceLineId"] = pageLineMap[id]["id"];
    param["produceType"] = pageLineMap[id]["produceType"];
      console.log("param: "+JSON.stringify(param));

    var url = host+"/manufacturing/produce.do";

    var success = function(data){
        console.log("productData:"+JSON.stringify(data))
        if(data.status==0){
            alert(data.message);
        }else{
            PubSub.publish('onSubmitProduce'+this.props.data.id, data.model.line);
        }
    }
    ajaxRequest(url,param,success,function(){},"post");
  }

  //产品研发 @TODO 未完成
  function exploitationProduct(productId){
    var url = "";  //@TODO 接口待定
    var param = new Object(); //@TODO 参数待定
    var success = function(data){
        //@TODO 确定返回格式
    }
    ajaxRequest(url,param,success,function(){},"post");
  }

  //选择购买原料的数量 @TODO 未完成
  function selectMaterialCount(resourceId){
    var url = "";  //@TODO 接口待定
    var param = new Object(); //@TODO 参数待定
    var success = function(data){
        //@TODO 确定返回格式
    }
    ajaxRequest(url,param,success,function(){},"post");
  }
// -----------------------------------------------------------

  //生产线
  var Line = function( id, serial, dept, name, status,produceLineType,produceType,lineBuildNeedCycle,produceNeedCycle) {
    this.id = id;
    this.serial = serial;
    this.dept = dept;
    this.produceLineType = produceLineType;
    this.produceType = produceType;
    this.name = name;
    this.status = status;
    this.lineBuildNeedCycle = lineBuildNeedCycle;
    this.produceNeedCycle = produceNeedCycle;
  };

  //当前企业
  var Company = function( id, name, currentCampaignDate) {
    this.id = id;
    this.name = name;
    this.currentCampaignDate = currentCampaignDate;
  };

  //当前竞赛
  var Campaign = function( id, name, currentCampaignDate) {
    this.id = id;
    this.name = name;
    this.currentCampaignDate = currentCampaignDate;
  };

  //资源
  var Resource = function(id,serial,dept,name,status,type,amount,purchasingAmount){
      this.id = id;
      this.serial = serial;
      this.dept = dept;
      this.name = name;
      this.status = status;
      this.type = type;
      this.amount = amount;
      this.purchasingAmount = purchasingAmount;
  }

  //产品
  var Product = function(id,serial,dept,name,status,type,amount,developNeedCycle){
      this.id = id;
      this.serial = serial;
      this.dept = dept;
      this.name = name;
      this.status = status;
      this.type = type;
      this.amount = amount;
      this.developNeedCycle = developNeedCycle;
  }


  var pageLineList = new Array();//生产线列表的全局变量
  var pageResourceList = new Array();//原料库列表
  var pageResourceMap = new Object();//原料库Map
  var pageLineMap = new Object(); //生产线对应id的映射表
  var pageCompany = new Object(); //当前用户对应的企业
  var pageCampaign = new Object();//当前竞赛的对象
  var pageProductList = new Array(); //产品列表
  var pageProductMap = new Object();  //产品对应id的映射表
  var host="http://localhost:8080"; //服务器端通用host 所有请求都需要加该host前缀



  // ---------------------------------------------------------------------------

  //获取初始化数据
  function initPageData(){
    var dataResult = new Object();
     ajaxRequest(host+"/manufacturing/current.do",{"companyId":"1","campaignId":"2"},function(data){
       console.log(data);
       dataResult = data;
         renderPage(dataResult);
     },function(){},"post");
//    dataResult = {"status":1,"message":null,"model":{"companyNum":1,"produceLineList":[{"id":"ikuvo8ap3cgkyrt0","serial":"23261","dept":"PRODUCT","name":"生产线1","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8cca45lwslo","serial":"23262","dept":"PRODUCT","name":"生产线2","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8csikxmge1v","serial":"23263","dept":"PRODUCT","name":"生产线3","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8d61m63mk89","serial":"23264","dept":"PRODUCT","name":"生产线4","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8dj242fzk87","serial":"23265","dept":"PRODUCT","name":"生产线5","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8dx1v0vi06g","serial":"23266","dept":"PRODUCT","name":"生产线6","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8ee2hkzu4v5","serial":"23267","dept":"PRODUCT","name":"生产线7","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0},{"id":"ikuvo8ev14b4d611","serial":"23268","dept":"PRODUCT","name":"生产线8","status":"UN_BUILD","produceLineType":null,"produceType":null,"lineBuildNeedCycle":0,"produceNeedCycle":0}],"shortTermLoan":0,"currentSeason":1,"materialList":[{"id":"ikuvo8f62e2dt8o7","serial":"23269","dept":null,"name":"原料R1","status":"NORMAL","type":"R1","amount":0,"purchasingAmount":0},{"id":"ikuvo8fj1u14sbls","serial":"23270","dept":null,"name":"原料R2","status":"NORMAL","type":"R2","amount":0,"purchasingAmount":0},{"id":"ikuvo8fx3a9ags52","serial":"23271","dept":null,"name":"原料R3","status":"NORMAL","type":"R3","amount":0,"purchasingAmount":0},{"id":"ikuvo8g84shpmbf7","serial":"23272","dept":null,"name":"原料R4","status":"NORMAL","type":"R4","amount":0,"purchasingAmount":0}],"campaign":{"id":"2","name":"制造业1","status":"2","mode":"public","password":null,"createDatetime":1444204438000,"startDatetime":1444204465000,"currentCampaignDate":1,"statusLabel":"进行中","nextCampaignDate":2,"preCampaignDate":0,"formattedCampaignDate":"第1年，第1季度"},"company":{"id":"1","name":"制造业-企业1","createDatetime":null,"capital":null,"deposit":null,"status":"NORMAL","currentCampaignDate":1,"slogan":"加油","memo":null,"result":0},"companyCash":100,"usuriousLoan":0,"productList":[{"id":"ikuvo8gh2ywrrk9k","serial":"23273","dept":null,"name":"产品P1","status":"DEVELOPED","type":"P1","amount":0,"developNeedCycle":0},{"id":"ikuvo8gw1hbfvplo","serial":"23274","dept":null,"name":"产品P2","status":"UNDEVELOPED","type":"P2","amount":0,"developNeedCycle":6},{"id":"ikuvo8h91k6u93wl","serial":"23275","dept":null,"name":"产品P3","status":"UNDEVELOPED","type":"P3","amount":0,"developNeedCycle":6},{"id":"ikuvo8hkzqwvz0op","serial":"23276","dept":null,"name":"产品P4","status":"UNDEVELOPED","type":"P4","amount":0,"developNeedCycle":6}],"longTermLoan":0}}    ;
  }

  //渲染页面
  //此处可放在服务器端渲染，但是客户端与服务端渲染的方式不同，需要分别处理
  function renderPage(data){
    var pagedata = data;
    var data = pagedata.model.produceLineList;
    var resourceData = pagedata.model.materialList;
    var productData = pagedata.model.productList;
    for (var i = 0; i < data.length; i++) {
      var dataTemp = data[i];
      var lineTemp = new  Line(dataTemp.id,dataTemp.serial,dataTemp.dept,dataTemp.name,dataTemp.status,dataTemp.produceLineType,dataTemp.produceType,dataTemp.lineBuildNeedCycle,dataTemp.produceNeedCycle);
      pageLineList.push(lineTemp);
      pageLineMap[lineTemp.id]=lineTemp;
    };
    for (var i = 0; i < resourceData.length; i++) {
      var dataTemp = resourceData[i];
      var resourceTemp = new  Resource(dataTemp.id,dataTemp.serial,dataTemp.dept,dataTemp.name,dataTemp.status,dataTemp.type,dataTemp.amount,dataTemp.purchasingAmount);
      pageResourceList.push(resourceTemp);
      pageResourceMap[resourceTemp.id]=resourceTemp;
    };

    for (var i = 0; i < productData.length; i++) {
      var dataTemp = productData[i];
      var productTemp = new Product(dataTemp.id,dataTemp.serial,dataTemp.dept,dataTemp.name,dataTemp.status,dataTemp.type,dataTemp.amount,dataTemp.developNeedCycle);
      pageProductList.push(productTemp);
      pageProductMap[productTemp.id]=productTemp;
    };
      pageCompany = new Company(pagedata["model"]["company"]["id"],pagedata["model"]["company"]["name"],pagedata["model"]["company"]["currentCampaignDate"]);
      pageCampaign = new Campaign(pagedata["model"]["campaign"]["id"],pagedata["model"]["campaign"]["name"],pagedata["model"]["campaign"]["currentCampaignDate"]);

    console.log(pageProductMap);
    // pageCompany = new Company(data.model.company.id,data.model.company.name,data.model.company.currentCampaignDate);
    // pageCampaign = new Campaign(data.model.campaign.id,data.model.campaign.name,data.model.campaign.currentCampaignDate);

    ReactDOM.render( <div><MyPanel id="panel-1"  ><MainPage/></MyPanel>
                 <MyPanel id="panel-2"></MyPanel>
                 <MyPanel id="panel-3"><MaterialStorageBox resource={pageResourceList}/> <ProductStorageBox product={pageProductList}/> <WorkShopBox name="厂房" data={pageLineList}/></MyPanel>
                 <MyPanel id="panel-4"> </MyPanel></div>, document.getElementById("content"));
    ReactDOM.render( <MyBottomNavBar lis={lis} />, document.getElementById("content2"));

  }

  //用于渲染尾部导航的现实内容的json对象
  var lis = [{"id":"button-1","href":"#","iconClassName":"am-icon-home","label":"主页","targetId":"panel-1"},
  {"id":"button-2","href":"#","iconClassName":"am-icon-sign-in","label":"市场","targetId":"panel-2"},
  {"id":"button-3","href":"#","iconClassName":"am-icon-sign-in","label":"生产","targetId":"panel-3"},
  {"id":"button-4","href":"#","iconClassName":"am-icon-sign-in","label":"财务","targetId":"panel-4"}]


  //页面初始化
  $().ready(function(){
    initPageData();
  })

 // var FinancialPage = React.createClass({

  //   getInitialState:function(){
  //     return {data:this.props.data};
  //   },

  //   componentDidMount:function(){

  //   },

  //   componentWillUnmount:function(){

  //   },

  //   render:function(){
  //     return (
  //       <div>
  //           <div className="am-panel am-panel-default">
  //               <div className="am-panel-hd">公司现金</div>
  //               <div className="am-panel-bd">
  //                   <span id="companyCash">-6</span>M
  //               </div>
  //           </div>
  //           <div className="am-panel am-panel-default">
  //               <div className="am-panel-hd">贷款</div>
  //               <div className="am-panel-bd">
  //                   <table className="am-table">
  //                       <thead>
  //                       <tr>
  //                           <th>贷款类型</th>
  //                           <th>已贷总额(M)</th>
  //                           <th>本期贷款(M)</th>
  //                       </tr>
  //                       </thead>
  //                       <tbody>

  //                           <tr>
  //                               <td>高利贷</td>
  //                               <td><span id="usuriousLoan">0</span></td>
  //                               <td>
  //                                   <select id="usuriousLoan_ifaunpbs2rhxqsun" name="usuriousLoanFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}" style={{"display": "none"}}>
  //                                       <option value="ifaunpbs2rhxqsun#-1">不需要</option>

  //                                           <option value="ifaunpbs2rhxqsun#20">20</option>

  //                                           <option value="ifaunpbs2rhxqsun#40">40</option>

  //                                           <option value="ifaunpbs2rhxqsun#60">60</option>

  //                                           <option value="ifaunpbs2rhxqsun#80">80</option>

  //                                           <option value="ifaunpbs2rhxqsun#100">100</option>

  //                                           <option value="ifaunpbs2rhxqsun#120">120</option>

  //                                           <option value="ifaunpbs2rhxqsun#140">140</option>

  //                                           <option value="ifaunpbs2rhxqsun#160">160</option>

  //                                           <option value="ifaunpbs2rhxqsun#180">180</option>

  //                                   </select><div className="am-selected am-dropdown  am-dropdown-flip" id="am-selected-e2zcb" data-am-dropdown="" style={{"width": "100px"}}>  <button type="button" className="am-selected-btn am-btn am-dropdown-toggle am-btn-sm am-btn-secondary">    <span className="am-selected-status am-fl">120</span>    <i className="am-selected-icon am-icon-caret-down"></i>  </button>  <div className="am-selected-content am-dropdown-content" style={{"min-width": "100px"}}>    <h2 className="am-selected-header"><span className="am-icon-chevron-left">返回</span></h2>       <ul className="am-selected-list">                     <li className="" data-index="0" data-group="0" data-value="ifaunpbs2rhxqsun#-1">         <span className="am-selected-text">不需要</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="1" data-group="0" data-value="ifaunpbs2rhxqsun#20">         <span className="am-selected-text">20</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="2" data-group="0" data-value="ifaunpbs2rhxqsun#40">         <span className="am-selected-text">40</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="3" data-group="0" data-value="ifaunpbs2rhxqsun#60">         <span className="am-selected-text">60</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="4" data-group="0" data-value="ifaunpbs2rhxqsun#80">         <span className="am-selected-text">80</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="5" data-group="0" data-value="ifaunpbs2rhxqsun#100">         <span className="am-selected-text">100</span>         <i className="am-icon-check"></i></li>                                 <li className="am-checked" data-index="6" data-group="0" data-value="ifaunpbs2rhxqsun#120">         <span className="am-selected-text">120</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="7" data-group="0" data-value="ifaunpbs2rhxqsun#140">         <span className="am-selected-text">140</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="8" data-group="0" data-value="ifaunpbs2rhxqsun#160">         <span className="am-selected-text">160</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="9" data-group="0" data-value="ifaunpbs2rhxqsun#180">         <span className="am-selected-text">180</span>         <i className="am-icon-check"></i></li>            </ul>    <div className="am-selected-hint"></div>  </div></div>
  //                               </td>
  //                           </tr>



  //                           <tr>
  //                               <td>短期贷款</td>
  //                               <td><span id="shortTermLoan">0</span></td>
  //                               <td>
  //                                   <select id="shortTermLoan_ifaunpbs2rhxqsum" name="shortTermLoanFee" data-am-selected="{btnWidth: '100px', btnSize: 'sm', btnStyle: 'secondary'}" style={{"display": "none"}}>
  //                                       <option value="ifaunpbs2rhxqsum#-1">不需要</option>

  //                                           <option value="ifaunpbs2rhxqsum#20">20</option>

  //                                           <option value="ifaunpbs2rhxqsum#40">40</option>

  //                                           <option value="ifaunpbs2rhxqsum#60">60</option>

  //                                           <option value="ifaunpbs2rhxqsum#80">80</option>

  //                                           <option value="ifaunpbs2rhxqsum#100">100</option>

  //                                           <option value="ifaunpbs2rhxqsum#120">120</option>

  //                                           <option value="ifaunpbs2rhxqsum#140">140</option>

  //                                           <option value="ifaunpbs2rhxqsum#160">160</option>

  //                                           <option value="ifaunpbs2rhxqsum#180">180</option>

  //                                   </select><div className="am-selected am-dropdown " id="am-selected-7ivmg" data-am-dropdown="" style={{"width": "100px"}}>  <button type="button" className="am-selected-btn am-btn am-dropdown-toggle am-btn-sm am-btn-secondary">    <span className="am-selected-status am-fl">不需要</span>    <i className="am-selected-icon am-icon-caret-down"></i>  </button>  <div className="am-selected-content am-dropdown-content">    <h2 className="am-selected-header"><span className="am-icon-chevron-left">返回</span></h2>       <ul className="am-selected-list">                     <li className="am-checked" data-index="0" data-group="0" data-value="ifaunpbs2rhxqsum#-1">         <span className="am-selected-text">不需要</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="1" data-group="0" data-value="ifaunpbs2rhxqsum#20">         <span className="am-selected-text">20</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="2" data-group="0" data-value="ifaunpbs2rhxqsum#40">         <span className="am-selected-text">40</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="3" data-group="0" data-value="ifaunpbs2rhxqsum#60">         <span className="am-selected-text">60</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="4" data-group="0" data-value="ifaunpbs2rhxqsum#80">         <span className="am-selected-text">80</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="5" data-group="0" data-value="ifaunpbs2rhxqsum#100">         <span className="am-selected-text">100</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="6" data-group="0" data-value="ifaunpbs2rhxqsum#120">         <span className="am-selected-text">120</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="7" data-group="0" data-value="ifaunpbs2rhxqsum#140">         <span className="am-selected-text">140</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="8" data-group="0" data-value="ifaunpbs2rhxqsum#160">         <span className="am-selected-text">160</span>         <i className="am-icon-check"></i></li>                                 <li className="" data-index="9" data-group="0" data-value="ifaunpbs2rhxqsum#180">         <span className="am-selected-text">180</span>         <i className="am-icon-check"></i></li>            </ul>    <div className="am-selected-hint"></div>  </div></div>
  //                               </td>
  //                           </tr>


  //                           <tr>
  //                               <td>长期贷款</td>
  //                               <td><span id="longTermLoan">0</span></td>
  //                               <td>



  //                                           第四季度可贷


  //                               </td>
  //                           </tr>

  //                       </tbody>
  //                   </table>
  //               </div>
  //           </div>

  //           <div className="am-panel am-panel-default">
  //               <div className="am-panel-hd">待还款记录</div>
  //               <div className="am-panel-bd">
  //                   <table className="am-table">
  //                       <thead>
  //                       <tr>
  //                           <th>贷款类型</th>
  //                           <th>金额(M)</th>
  //                           <th>剩余还款周期</th>
  //                       </tr>
  //                       </thead>
  //                       <tbody id="loanListTbody">

  //                       </tbody>
  //                   </table>
  //               </div>
  //           </div>
  //       </div>
  //       )
  //   }


  // })
</script>


</body>
</html>
