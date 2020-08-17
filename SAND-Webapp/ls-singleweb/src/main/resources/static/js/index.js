$(document).ready(function () {
    /*加载header.html*/
    $(".header").load("header.html", function () {
        /**
         * left菜单收起
         */
        $("#toggle-btn").click(function () {
            num = $(".side-navbar").attr('class').indexOf('shrinked');
            if (num < 0) {
                $(".side-navbar").addClass('shrinked');
                $("#toggle-btn").prop('class', 'menu-btn');
                $(".content-inner").addClass('active');
            } else {
                $(".side-navbar").prop("class", 'side-navbar');
                $("#toggle-btn").addClass('active');
                $(".content-inner").prop('class', 'content-inner');
            }
        })
    });

    /*加载left.html*/
    $(".left").load("left.html", function () {
        /**
         * 点击left menu时选中项不被重置
         */
        var urlstr = location.href;
        var urlstatus = false;
        $(".left li").each(function () {
            if ((urlstr + '/').indexOf($(this).find('a').prop('href')) > -1 && $(this).find('a').prop('href') != '') {
                $(this).addClass('active');     //当前选中
                if ($(this).prop('class').indexOf('active') != -1 && $(this).find('a').prop('href').indexOf('index') < 0) {
                    $(this).parent().prev().attr('aria-expanded', 'true');   //图标展开
                    $(this).parent().parent().find('ul').addClass('show');   //显示子节点
                }
                urlstatus = true;
            } else {
                $(this).removeClass('active');
            }
            /*if(!urlstatus){
                $(this).find('a').attr('aria-expanded','true');   //图标展开
                //$(this).parent().parent().find('ul').addClass('show');   //显示子节点
                if($(this).find('a').attr('aria-expanded') == 'true') {
                    //$(this).find('a:first').attr('data-toggle','collapse');
                    $(this).find('ul').addClass('show');   //显示子节点
                }
            }*/
        });
        if (!urlstatus) {
            $(".left li").eq(0).addClass('active');
        }
    });

    /*加载foot.html*/
    $(".main-footer").load("foot.html", function () {
        /**
         * copyright自动获取当前年份
         */
        var date = new Date().getFullYear();
        $(".foot_year").html(date);
    });

});
