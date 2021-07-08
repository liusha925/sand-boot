package com.sand.core.search;

import com.sand.core.search.base.SearchBase;

import java.util.List;

/**
 * 功能说明：全文搜索测试类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/8 9:24 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class SearchTest {
    public static void main(String[] args) {
        // 建立搜索库
        SearchBase searchBase = SearchBase.getSearchBase();
        searchBase.add("1", "你好！", "你好！");
        searchBase.add("2", "你好！我是张三。", "你好！我是张三。");
        searchBase.add("3", "今天的天气挺好的。", "今天的天气挺好的。");
        searchBase.add("4", "你是谁？", "你是谁？");
        searchBase.add("5", "高数这门学科很难", "高数确实很难。");
        searchBase.add("6", "测试", "上面的只是测试");
        // 搜索的内容
        String ids = searchBase.getIds("你的高数");
        System.out.println(ids);
        List<Object> objs = searchBase.getObjects(ids);
        if (objs != null) {
            for (Object obj : objs) {
                System.out.println((String) obj);
            }
        }
    }
}