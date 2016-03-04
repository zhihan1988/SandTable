<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
    <title>Amaze UI React</title>
    <meta name="renderer" content="webkit">
    <!-- No Baidu Siteapp-->
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <meta name="apple-mobile-web-app-title" content="AMUI React"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" href="http://cdn.amazeui.org/amazeui/2.4.0/css/amazeui.min.css"/>
    <script src="amazeui/assets/js/jquery.min.js"></script>
    <script src="react/build/react.js"></script>
    <script src="react/build/react-dom.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/babel-core/5.8.23/browser.min.js"></script>

    <style type="text/css">

        .navbar-button-selected{
            /*background: orange;*/
            background: orangered;
        }

    </style>

</head>
<body>

<script type="text/javascript">
    //每个组件都是页面当中的一部分，声明组件的过程就相当于在写页面，首先需要知道页面由哪些部分组成，然后按照每个部分声明组件，从里层到外层都要声明
    //最后每个小组件都是包含在大组件之中所以说应该是先从小的组件开始
    //小的组件可以参考amazeui现有的组件，Render是最后一步，可以暂时不考虑
    //  先从生产线开始
    //生产线需要的组件
    /*
     1.对生产线的操作（按钮组）
     2.生产线当前的属性（下拉选择按钮）
     3.生产线列表 （panalItem）
     4.生产线所在厂房（panal）

     1.<ButtonToolbar>
     2.<Selected {...props} />
     3.<ProductLine.Item>
     4.<ProductLine>
     */

    //数据刷新的逻辑也需要放在声明中，感觉每个组件之间都有一种完整的功能链条，数据刷新的逻辑也要放在react声明之中，如果放在外面，那么在调用的方法或者js对象的时候就会发现所调用的方法是个未定义的方法
    //如果开发一个应用的话，那么整个页面就相当于一个大组件，js和react混合开发好像是不太兼容的var items = [{"status":"1"},{"status":"2"},{"status":"3"}];

    //在react中传输动态数据需要使用state属性，重新刷新页面的组件只需要调用setState方法更新state属性就可以了，react会自动调用当前组件的render方法

    var items = [{"status":"1"},{"status":"2"},{"status":"3"}];  //测试数据
</script>

<div id="content"></div>
<div id="content2"></div>

<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－test.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->

<script type="text/babel">



    //全局 start

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


    var pageVariable = new Object();


    var testComponent ;


    //全局 end



    function initPageData(){
        //返回生产线列表（暂时的）
        //该函数只处理数据 其他的不管，只做数据的接收
        var data =
                        [
                            {
                                "id": "ik12x74m2a69kpol",
                                "serial": "19550",
                                "dept": "PRODUCT",
                                "name": "生产线1",  //生产线名称
                                "status": "FREE", //生产线状态
                                "produceLineType": "AUTOMATIC", //生产线类型
                                "produceType": "P1",  //生产线允许商品的类型
                                "lineBuildNeedCycle": 3,  //建造生产线所需要的周期
                                "produceNeedCycle": 0 //建造商品所需要的周期
                            },
                            {
                                "id": "ik12x74m2a69kppl",
                                "serial": "19550",
                                "dept": "PRODUCT",
                                "name": "生产线2",  //生产线名称
                                "status": "UN_BUILD", //生产线状态
                                "produceLineType": "AUTOMATIC", //生产线类型
                                "produceType": "P1",  //生产线允许商品的类型
                                "lineBuildNeedCycle": 3,  //建造生产线所需要的周期
                                "produceNeedCycle": 0 //建造商品所需要的周期
                            }
                        ]
                ;

        return data;
    }


    // {
    //     "id": "ik12x74m2a69kppl",
    //     "serial": "19550",
    //     "dept": "PRODUCT",
    //     "name": "生产线2",  //生产线名称
    //     "status": "UN_BUILD", //生产线状态
    //     "produceLineType": "", //生产线类型
    //     "produceType": "P1",  //生产线允许商品的类型
    //     "lineBuildNeedCycle": 3,  //建造生产线所需要的周期
    //     "produceNeedCycle": 0 //建造商品所需要的周期
    // }


    function chooseType(id,value){
        var lineMap = pageVariable["lineMap"];
        var line = lineMap[id];
        line["produceLineType"]=value;
        var result = new Object();
        result["line"]=line;
        return result;
    }


    function renderPage(){
        var data = initPageData();
        var lineList = new Array();
        var lineMap = new Object();

        for (var i = 0; i < data.length; i++) {
            var dataTemp = data[i];
            var lineTemp = new  Line(dataTemp.id,dataTemp.serial,dataTemp.dept,dataTemp.name,dataTemp.status,dataTemp.produceLineType,dataTemp.produceType,dataTemp.lineBuildNeedCycle,dataTemp.produceNeedCycle);
            lineList.push(lineTemp);
            lineMap[lineTemp.id]=lineTemp;
        };

        pageVariable["lineList"]=lineList;
        pageVariable["lineMap"]=lineMap;
    }



    var buttonOnClick = function(){
        alert("buttonOnClick");
    }









    //生产线的初始化状态是一个表单，一个固定的地方，如果投资新生产线，那么就发个请求新建一个生产线，然后显示建造中，提交生产的时候选择新建生产线的类型
    var ProductLineItem = React.createClass({


        getInitialState : function(){
            return {data:this.props.data};
        },

        chooseLineType : function(e){
            //操作生产线 更新数据
            var value = e.target.value;
            console.log(value);
            console.log(chooseType);
            var dataTemp = chooseType(this.props.data.id,value);
            this.setState({data:dataTemp.line});
        },

        submitProduce: function(){
            // var dataTemp = produce(this.props.data.id);
            // this.setState({data:dataTemp.line});  //可以自动刷新数据 必须调用setState方法，直接赋值时不管用的
            pageVariable['lineList'][0].produceType ='P3';
            console.log(pageVariable['lineList'][0].produceType);
            testComponent.forceUpdate();

        },

        submitLine:function(){
            pageVariable['lineList'][0].produceType ='P3';
            console.log(pageVariable['lineList'][0].produceType);
            testComponent.forceUpdate();
        },
        //生产类型，UNBUILD 手工和柔性那么就是所有许可产品，如果是半自动和自动就是指定类型的产品，
        //FREE , 如果是手工和柔性生产线，那么就在生产商品的时候手动选择需要生产的商品（下拉） 如果是自动和半自动，就只显示指定的产品 然后是生产按钮

        //FREE
        //@TODO 需要修改类型的值，可能有变动 目前只判断代表性的两个 ，之后再 把半自动加上
        render: function(){

            var text = this.state.data;
            var lineTypeOptions = [{"value":"AUTOMATIC","label":"全自动"},{"value":"UNAUTOMATIC","label":"手工"}];
            var produceTypeOptions = [{"value":"P1","label":"P1"},{"value":"P2","label":"P2"},{"value":"P3","label":"P3"},{"value":"P4","label":"P4"}];


            return (
                    <div className="am-panel-bd line-panel">
                    <div id="line_iistm2qf2sjj8uec">
                    <div className="line-innerDiv1 line-important">生产线：


          {(()=>{
              if (text.status=='UN_BUILD' || text.status=='FREE') {
                  return text.name;
              }else if (text.status=='BUILDING'|| text.status=='BUILT'){
                  return '建造中';
              };

          })()}



            <span></span></div>
            <div className="line-innerDiv2">

                    {(() =>
                            {
                                if (text.status=='UN_BUILD') {
                                    return <span className="line-innerSpan2">生产线类型：<MySelect active={text.produceLineType} options={lineTypeOptions} selectOnChange={this.chooseLineType}/></span>;
                                }else if(text.status=='BUILDING'){
                                    return <span className="line-innerSpan2">生产线类型：{text.produceLineType}</span>;
                                }else if (text.status=='BUILT'){
                                    return <span className="line-innerSpan2">剩余生产周期:{text.lineBuildNeedCycle}</span>;
                                }else if(text.status=='PRODUCING'){
                                    return <span className="line-innerSpan2">剩余生产周期:{text.lineBuildNeedCycle}</span>;
                                }else{
                                    return <span className="line-innerSpan2">生产线类型:{text.produceLineType}</span>; //..当前生产线的类型
                                };
                            }
                    )()}



            <span style={{margin: "10px"}}></span><span className="line-innerSpan3" id="produceTypeSpan_iistm2qf2sjj8uec">生产类型：

              {(() =>
                      {
                          if (text.status == 'UN_BUILD') {

                              //@TODO 需要修改类型的值，可能有变动
                              if (text.produceLineType=='AUTOMATIC') {
                                  return <MySelect active={text.produceType} options={produceTypeOptions}/> ;
                              }else{
                                  return "所有许可产品";
                              };
                          }else if (text.status == 'FREE'){

                              if (text.produceLineType == 'AUTOMATIC') {
                                  return text.produceType;
                              }else{
                                  return <MySelect active={text.produceType} options={produceTypeOptions} />;
                              };

                          }else if (text.status == 'BUILDING'||text.status=='BUILT') {
                              //@TODO 需要修改类型的值，可能有变动
                              if (text.produceLineType=='AUTOMATIC') {
                                  return text.produceType; ;
                              }else{
                                  return "所有许可产品";
                              };
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
        }
    });



    var MySelect = React.createClass({

        render: function(){

            var createOption = function(option){
                return <option value={option.value}>{option.label}</option> ;
            }
            return (<select value={this.props.active} name={this.props.name} onChange={this.props.selectOnChange}>{this.props.options.map(createOption)}</select>);
        }

    });


    var lis = [{"id":"button-1","href":"#","iconClassName":"am-icon-home","label":"主页","targetId":"panel-1"},
        {"id":"button-2","href":"#","iconClassName":"am-icon-sign-in","label":"市场","targetId":"panel-2"},
        {"id":"button-3","href":"#","iconClassName":"am-icon-sign-in","label":"生产","targetId":"panel-3"},
        {"id":"button-4","href":"#","iconClassName":"am-icon-sign-in","label":"财务","targetId":"panel-4"}]




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

        }
    });


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
        }
    });

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
        }
    });



    var ProductLine = React.createClass({

        getInitialState:function(){
            return {data:pageVariable.lineList};
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
        }
    });



    $().ready(function(){

        renderPage();
        testComponent = ReactDOM.render( <div><MyPanel id="panel-1"  ><ProductLine name="厂房A" /></MyPanel>
                <MyPanel id="panel-2"> panel-2 </MyPanel>
                <MyPanel id="panel-3"> panel-3 </MyPanel>
                <MyPanel id="panel-4"> panel-4 </MyPanel></div>, document.getElementById("content"));
        ReactDOM.render( <MyBottomNavBar lis={lis} />, document.getElementById("content2"));

    })




</script>


<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－logicObject.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->


<script type="text/babel">
    //logicObject 声明model类
    // var CompanyPart = function( id, serial, dept, campaign, company, name, status,templateMap) {
    //   this.id = id;
    //   this.serial = serial;
    //   this.dept = dept;
    //   this.campaign = campaign;
    //   this.company = company;
    //   this.name = name;
    //   this.status = status;
    //   this.templateMap = templateMap;
    // }
</script>


<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－logic.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->


<script type="text/babel">
    // //logic 全局缓存的数据
    //   var logicModel = {
    //       totalCash: "",//template
    //       totalDebt: "",
    //       debtList: "",
    //       humanList: "",
    //       market: "",
    //       product: ""
    //   };
</script>


<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－viewObject.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->

<script type="text/babel">
    //  //viewObject  声明组件
    // var ProductLineItem = React.createClass({
    //   onClick: function(){
    //       //这里的按钮点击事件应该是调用controller里已经写好的逻辑，具体做什么操作在声明的时候不用考虑，但是需要声明点击事件的接口
    //       var items = [{"status":"50"},{"status":"2"},{"status":"3"}];
    //       ReactDOM.render(<ProductLine items={items} />,document.getElementById("content"));
    //   },
    //   render: function(){
    //     return (
    //     <div className="am-panel-bd line-panel">
    //       <div id="line_iistm2qf2sjj8uec">
    //         <div className="line-innerDiv1 line-important">生产线1：<span></span></div>
    //         <div className="line-innerDiv2">
    //             <span className="line-innerSpan2">生产线类型：{this.props.data.status}</span><span style={{margin: "10px"}}></span><span className="line-innerSpan3" id="produceTypeSpan_iistm2qf2sjj8uec">生产类型：所有许可产品</span><button onClick={onClick}>刷新数据</button>
    //         </div>
    //         <div id="lineButtonDiv_iistm2qf2sjj8uec">
    //         </div>
    //       </div>
    //     </div>
    //     );
    //   }
    // });

    // //下拉菜单
    // var MySelect = React.createClass({
    //   render: function(){
    //     var createOption = function(option){
    //         return <option value={option.value}>{option.label}</option> ;
    //     }
    //     return (<select>{this.props.options.map(createOption)}</select>);
    //   }

    // });

    // var lis = [{"id":"button-1","href":"#","iconClassName":"am-icon-home","label":"主页","targetId":"panel-1"},
    // {"id":"button-2","href":"#","iconClassName":"am-icon-sign-in","label":"市场","targetId":"panel-2"},
    // {"id":"button-3","href":"#","iconClassName":"am-icon-sign-in","label":"生产","targetId":"panel-3"},
    // {"id":"button-4","href":"#","iconClassName":"am-icon-sign-in","label":"财务","targetId":"panel-4"}]




    // var MyBottomNavBar = React.createClass({
    //   getInitialState: function(){
    //     return {active:"button-1"};
    //   },
    //   buttonHandleClick: function(e){
    //     var targetId = $(e.target).attr("name");
    //     $(".panel").hide();
    //     $("#"+targetId).show();
    //     var buttonId = $("a[name="+targetId+"]").attr("id");
    //     this.setState({active:buttonId});
    //   },
    //   render: function(){
    //     return (
    //     <div data-am-widget="navbar" className="am-navbar am-cf am-navbar-default " id="">
    //       <ul className="am-navbar-nav am-cf am-avg-sm-4">
    //           <MyBottomNavBar.ButtonItem {...lis[0]} handleClick={this.buttonHandleClick} active={this.state.active}/>
    //           <MyBottomNavBar.ButtonItem {...lis[1]} handleClick={this.buttonHandleClick} active={this.state.active}/>
    //           <MyBottomNavBar.ButtonItem {...lis[2]} handleClick={this.buttonHandleClick} active={this.state.active}/>
    //           <MyBottomNavBar.ButtonItem {...lis[3]} handleClick={this.buttonHandleClick} active={this.state.active}/>
    //       </ul>
    //     </div>);
    //   }
    // });


    // MyBottomNavBar.ButtonItem = React.createClass({
    //   //按钮需要定义状态，是否选中，在初始化的时候通过lis中获得
    //   getInitialState: function(){
    //     return {active:this.props.active};
    //   },
    //   render: function(){
    //     return (
    //       <li key={this.props.id} name={this.props.targetId}>
    //         <a id={this.props.id} href={this.props.href} onClick={this.props.handleClick} name={this.props.targetId}  className={this.props.active==this.props.id?"navbar-button-selected":""}>
    //           <span name={this.props.targetId} className={this.props.iconClassName}></span>
    //           <span name={this.props.targetId} className="am-navbar-label">{this.props.label}</span>
    //         </a>
    //       </li>
    //     )
    //   }
    // });

    // var MyPanel = React.createClass({
    //     render : function(){
    //       return (
    //         <div id={this.props.id} className="panel">
    //           <div className="am-panel am-panel-default">
    //             {
    //               React.Children.map(this.props.children,function(child){
    //                 console.log(child);
    //                 console.log("123456");
    //                 return <div className="am-panel-bd">{child}</div>
    //               })
    //             }
    //           </div>
    //         </div>
    //       );
    //     }
    // });


    // var ProductLine = React.createClass({
    //   getInitialState:function(){
    //     return {data:items};
    //   },
    //   render:function(){
    //     var createItem = function(item) {
    //       return <ProductLineItem key={item.status} data={item} />;
    //     };
    //     return (
    //       <div className="am-panel am-panel-default" id="produceLinesDiv">
    //         <div className="am-panel-hd am-panel-hd-module" >{this.props.name}</div>
    //         {this.props.items.map(createItem)}
    //       </div>
    //     );
    //   }
    // });

</script>



<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－view.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->

<script type="text/babel">
    //view 渲染组件
    // function renderComponent(){
    //   ReactDOM.render( <ProductLine />, document.getElementById("content"));
    // }

</script>


<!-- －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－control.js－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－ -->

<script type="text/babel">
    //control  加载数据  放在最后

    //     var testData = {
    //       "CompanyPart": [{
    //           "id": "id:1",
    //           "serial": "1",
    //           "dept": "1",
    //           "campaign": "1",
    //           "company": "1",
    //           "name": "product",
    //           "status": "1"
    //       }, {
    //           "id": "id:2",
    //           "serial": "1",
    //           "dept": "1",
    //           "campaign": "1",
    //           "company": "1",
    //           "name": "market",
    //           "status": "1"
    //       }, {
    //           "id": "id:3",
    //           "serial": "1",
    //           "dept": "1",
    //           "campaign": "1",
    //           "company": "1",
    //           "name": "human",
    //           "status": "1"
    //       }, {
    //           "id": "id:4",
    //           "serial": "1",
    //           "dept": "1",
    //           "campaign": "1",
    //           "company": "1",
    //           "name": "human",
    //           "status": "1"
    //       }]
    //   };

    // var lis = [{"id":"button-1","href":"#","iconClassName":"am-icon-home","label":"主页","targetId":"panel-1"},
    // {"id":"button-2","href":"#","iconClassName":"am-icon-sign-in","label":"市场","targetId":"panel-2"},
    // {"id":"button-3","href":"#","iconClassName":"am-icon-sign-in","label":"生产","targetId":"panel-3"},
    // {"id":"button-4","href":"#","iconClassName":"am-icon-sign-in","label":"财务","targetId":"panel-4"}]

    // var options = [{"value":"value1","label":"label1"},{"value":"value2","label":"label2"},{"value":"value3","label":"label3"}];

    // function initPage(){
    //   //@TODO 加载数据（ajax）
    //   renderComponent();
    // }

</script>

</body>
</html>