function radar(data, radarId) {
    let radar = [];
    let indicator = [];
    data.forEach(info => {
        let name = info.name;
        let score = info.radarScore;
        radar[name] = score;
    })

    let score = Object.values(radar)
    //返回最大值
    let scoreMax = Math.max(...score)
    let keys = Object.keys(radar)
    keys.forEach(info => indicator.push({
        'name': info,
        'max': scoreMax
    }));

    let radar_chart = echarts.init(radarId);
    let option = {
        title: {},
        tooltip: {
            trigger: 'axis'
        },
        legend: {},
        radar: {
            // shape: 'circle',
            name: {
                textStyle: {
                    color: '#fff',
                    //backgroundColor: '#999',
                    borderRadius: 3,
                    padding: [3, 5]
                }
            },
            indicator: indicator,
            splitArea: {
                show: false,
                lineStyle: {
                    width: 1,
                    color: '#C2443F', // 设置网格的颜色
                },
            },
            // 设置雷达图中间射线的颜色

        },
        series: [{
            name: '业务画像',
            type: 'radar',
            areaStyle: {color: '#3e48ff',},
            symbolSize: 8, // 拐点的大小
            tooltip: {
                trigger: 'item'
            },
            data: [{
                value: score,
                itemStyle: {
                    normal: {
                        color: '#4d64ff',

                    }
                }
            }]
        }]
    };
    radar_chart.setOption(option);


}

function gauge(item, gaugeId) {
    let data1 = {};
    let gaugeData = [];

    let score = item.gaugeScore;
    if (score >= 0 && score < 50) {
        data1 = {
            "value": score,
            "name": "黄金客户"
        };
    } else if (score >= 50 && score < 80) {
        data1 = {
            "value": score,
            "name": "铂金客户"
        };
    } else if (score >= 80 && score <= 100) {
        data1 = {
            "value": score,
            "name": "钻石客户"
        };
    } else {
        data1 = {
            "value": 0,
            "name": "分值不在 0-100 范围内"
        };
    }
    gaugeData.push(data1);

    let gauge_chart = echarts.init(gaugeId);

    //仪表盘的轴线可以被分成不同颜色的多段。每段的  结束位置(范围是[0,1]) 和  颜色  可以通过一个数组来表示。默认取值：[[0.2, '#91c7ae'], [0.8, '#63869e'], [1, '#c23531']]
    let colorTemplate1 = [
        [0.4999, "rgba(77,100,255)"],
        [0.7999, "rgba(0,255,255)"],
        [1, "rgba(255,182,21)"]
    ];
    option = {
        series: [{
            name: '业务指标',
            type: 'gauge',
            detail: {
                color: "#fff",
                formatter: '{value}'
            },
            data: gaugeData,
            axisLine: {

                lineStyle: {
                    color: colorTemplate1
                }
            },
            title: {
                color: "#fff",
                fontSize: 30
            },

            axisTick: { // 刻度(线)样式。
                show: true, // 是否显示刻度(线),默认 true。
                splitNumber: 5, // 分隔线之间分割的刻度数,默认 5。
                length: 8, // 刻度线长。支持相对半径的百分比,默认 8。
                lineStyle: { // 刻度线样式。
                    color: "#eee", //线的颜色,默认 #eee。
                    opacity: 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
                    width: 1, //线度,默认 1。
                    type: "solid", //线的类型,默认 solid。 此外还有 dashed,dotted
                    shadowBlur: 10, //(发光效果)图形阴影的模糊大小。该属性配合 shadowColor,shadowOffsetX, shadowOffsetY 一起设置图形的阴影效果。
                    shadowColor: "#fff", //阴影颜色。支持的格式同color。
                },
            },

        },


        ]
    };
    gauge_chart.setOption(option);


}


function clickaaa() {
    //window.location.reload();
    // $("#test").empty();
    $("#marketAdvice").empty();
    $("#noOpen").empty();
    $("#riskAdvice").empty();
    $("#cricle").empty();

    getAjax($("#selectId option:selected").val())
}

//调用ajax方法1
function getAjax(data) {
    $.ajax({
        type: "post",
        url: "http://localhost:8888/view",
        //url: "http://localhost:63342/demographic/ls-singleweb/static/json/view.json",
        data: {
            merId: data
        },
        success: function (ret) {
            //总分
            $("#total").text(ret.gauge.gaugeScore);
            //雷达图
            radar(ret.baseList, document.getElementById("chart"));
            //仪表盘
            gauge(ret.gauge, document.getElementById("gauge"));

            //标签图
            ret.detailList.forEach(info => {
                let str = "<div class=\"rounded-circle\" title=\"" + info.radarScore + "\">" + info.name + "</div>";
                $("#cricle").append(str);
            });
            //负面舆情
            let table1 = [];
            //投资监督
            let table2 = [];
            //欠费
            let table3 = [];
            //风险画像table表格
            ret.riskList.forEach(info => {
                if (info.name == "负面舆情") {
                    $("#p1").html(info.name + "</br>" + info.max + "次");
                    table1 = info.detail;
                    let showTable = $("#table1");
                    showTable.empty();
                    showTable.append("<thead> <tr> <th><p>产品名称</p></th><th><p>发生日期</p></th> <th><p>负面舆情</p></th> </tr> </thead>");
                    table1.forEach(info => {
                        var str = "<tr><td><p title='" + info.productName + "'>" + info.productName + "</p></td><td><p  title='" + info.pubOpinDate + "'>" + info.pubOpinDate + "</p></td><td><p  title='" + info.massage + "'>" + info.massage + "</p></td></tr>";
                        //追加到table中
                        showTable.append("<tbody>" + str + "</tbody>");
                    })
                } else if (info.name == "投资监督提示") {
                    table2 = info.detail;
                    $("#p2").html(info.name + "</br>" + info.max + "次");

                    let showTable = $("#table2");
                    showTable.empty();
                    showTable.append("<thead> <tr> <th><p>产品名称<p></th><th><p>发生日期<p></th> <th><p>提示信息<p></th> </tr> </thead>");
                    table2.forEach(info => {
                        var str = "<tr><td><p  title='" + info.productName + "'>" + info.productName + "</p></td><td><p  title='" + info.inveSupDate + "'>" + info.inveSupDate + "</p></td><td><p  title='" + info.inveSupTips + "'>" + info.inveSupTips + "</p></td></tr>";
                        //追加到table中
                        showTable.append("<tbody>" + str + "</tbody>");
                    })

                } else {
                    table3 = info.detail;
                    $("#p3").html(info.name + "</br>" + info.max + "次");
                    let showTable = $("#table3");
                    showTable.empty();
                    showTable.append("<thead> <tr> <th><p>产品名称<p></th><th><p>欠费次数<p></th> </tr> </thead>");
                    table3.forEach(info => {
                        var str = "<tr><td><p  title='" + info.productName + "'>" + info.productName + "</p></td><td><p  title='" + info.arrearsNum + "'>" + info.arrearsNum + "</p></td></tr>";
                        //追加到table中
                        showTable.append("<tbody>" + str + "</tbody>");
                    })
                }

            })

            //营销建议与风险建议
            if (ret.gauge.marketAdvice != null) {
                let str = "<p>" + ret.gauge.marketAdvice + "</p>"
                $("#marketAdvice").append(str);
            }
            if (ret.gauge.riskAdvice != null) {
                let str = "<p>" + ret.gauge.riskAdvice + "</p>"
                $("#riskAdvice").append(str);
            }

            //未开通排行显示
            ret.gauge.list.forEach(info => {
                let str = "<div class=\"col-3 text-center\">" +
                    "<p class='mb-1'>" + info.name + "</p>" +
                    " <p class=\"mb-0\">" + info.radarScore + "</p>" +
                    " </div>";
                $("#noOpen").append(str);
            })

        }

    })
}

function startTime() {
    var today = new Date();
    var y = today.getFullYear();
    var M = today.getMonth() + 1;
    var d = today.getDate();
    var h = today.getHours();
    var m = today.getMinutes();
    var s = today.getSeconds();
    var w = today.getDay();
    var weekday = new Array(7);
    weekday[0] = "星期日";
    weekday[1] = "星期一";
    weekday[2] = "星期二";
    weekday[3] = "星期三";
    weekday[4] = "星期四";
    weekday[5] = "星期五";
    weekday[6] = "星期六";
    // 当分钟和秒为个位数时前面补0
    if (m < 10) {
        m = "0" + m;
    }
    if (s < 10) {
        s = "0" + s;
    }
    document.getElementById('time').innerHTML = y + "-" + M + "-" + d + " " + h +
        ":" + m + ":" + s + " " + weekday[w];
    var t = setTimeout(function () {
        startTime()
    }, 500); /* 每500毫秒执行一次，实现动态显示时间效果 */
}